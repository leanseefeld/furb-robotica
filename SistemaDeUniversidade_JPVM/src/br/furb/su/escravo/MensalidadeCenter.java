package br.furb.su.escravo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpvm.jpvmBuffer;
import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.Estatisticas;
import br.furb.su.Sistema;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.dataset.writer.MensalidadesWriter;
import br.furb.su.mestre.CursoCenterControle;
import br.furb.su.modelo.Mensagem;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.Mensalidade;
import br.furb.su.modelo.dados.SituacaoDisciplina;
import br.furb.su.operacoes.Operacao;
import br.furb.su.operacoes.OperacaoFactory;

public class MensalidadeCenter extends EscravoBase {

	public static final String GET_ALUNO_POSSUI_ATRASO = "aluno_possui_ptraso";
	public static final String GET_MENSALIDADE = "mensalidade";
	public static final String PARAM_COD_ALUNO = "codAluno";

	private final Map<Long, Collection<Mensalidade>> mensalidades = new HashMap<>();
	private final MensalidadesReader mensReader = new MensalidadesReader();
	private final MensalidadesWriter mensWriter = new MensalidadesWriter();
	private final Collection<Mensalidade> novasMensalidades = new ArrayList<>();
	private CursoCenterControle cursoCenter;

	public MensalidadeCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) throws jpvmException {
		new MensalidadeCenter().run();
	}

	@Override
	protected void doUpload(String buffer) {
		List<Mensalidade> lido = mensReader.ler(buffer);
		for (Mensalidade mensalidade : lido) {
			Collection<Mensalidade> mensalidadesAluno = mensalidades.get(mensalidade.getCodAluno());
			if (mensalidadesAluno == null) {
				mensalidadesAluno = new ArrayList<>();
				mensalidades.put(mensalidade.getCodAluno(), mensalidadesAluno);
			}
			mensalidadesAluno.add(mensalidade);
		}
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doDownload(String buffer) {
		if (buffer.equals("estatisticas")) {
			byte[] bytes = serializar(Sistema.estatisticas());
			jpvmBuffer jBuffer = new jpvmBuffer();
			jBuffer.pack(bytes.length);
			jBuffer.pack(bytes, bytes.length, 1);
			tryResponder(ResponseEscravo.OK, jBuffer);
		} else if (buffer.equals("mensalidades")) {
			List<Mensalidade> todasMensalidades = new ArrayList<>();
			for (Collection<Mensalidade> mensAluno : mensalidades.values()) {
				todasMensalidades.addAll(mensAluno);
			}
			tryResponder(ResponseEscravo.OK, gravarMensalidades(todasMensalidades));
		} else {
			super.doDownload(buffer);
		}

	}

	@Override
	protected void doGet(String buffer) {
		Operacao op = converterGetParaOperacao(buffer);
		switch (op.getNome()) {
		case GET_MENSALIDADE:
			Collection<Mensalidade> mensAluno = mensalidades.get(Long.parseLong((String) op.getParam(PARAM_COD_ALUNO)));
			tryResponder(ResponseEscravo.OK, gravarMensalidades(mensAluno));
			break;
		case GET_ALUNO_POSSUI_ATRASO:
			mensAluno = mensalidades.get(Long.parseLong((String) op.getParam(PARAM_COD_ALUNO)));
			String response = Boolean.FALSE.toString();
			if (mensAluno != null) {
				for (Mensalidade m : mensAluno) {
					if (m.isAtrasada()) {
						response = Boolean.TRUE.toString();
						break;
					}
				}
			}
			tryResponder(ResponseEscravo.OK, response);
			break;
		default:
			super.doGet(buffer);
			return;
		}
	}

	private String gravarMensalidades(Collection<Mensalidade> mensAluno) {
		String response;
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			mensWriter.gravarDados(mensAluno, pw);
			response = sw.getBuffer().toString();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gravar resposta", e);
		}
		return response;
	}

	@Override
	protected void doSetSlave(String slaveName, jpvmTaskId taskId) {
		if (slaveName.equals("cursoCenter")) {
			cursoCenter = new CursoCenterControle(pvm, taskId);
		}
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doOperation(Operacao op) {
		if (op.getNome().equals(OperacaoFactory.NOME_PROCESS_MENSALIDADES_ATRASADAS)) {
			processaMensalidadesAtrasadas(op);
		} else if (op.getNome().equals(OperacaoFactory.NOME_PROCESS_NOVAS_MENSALIDADES)) {
			processaNovasMensalidades(op);
		} else {
			tryResponder(ResponseEscravo.FAILURE, MSG_OP_NAO_RECONHECIDA);
		}
	}

	private void processaNovasMensalidades(Operacao op) {
		List<Long> alunos = lerCodigos((String) op.getParam("alunosAtivos"));
		Calendar dataAtual = (Calendar) op.getParam("dataReferencia");
		double totalMensalidades = 0;
		double totalMatriculas = 0;

		try {
			for (Long aluno : alunos) {
				int mensalidadeAluno = 0;
				int matriculaAluno = 0;
				List<Historico> historicos = cursoCenter.getHistoricosAtivos(aluno);
				for (Historico historico : historicos) {
					final Disciplina disciplina = cursoCenter.getDisciplina(historico.getCodDisciplina());
					if (historico.getSituacao() == SituacaoDisciplina.MATRICULADO) {
						matriculaAluno += disciplina.getValorMatricula();
					} else {
						mensalidadeAluno += disciplina.getValorMensal();
					}
				}
				Mensalidade mensalidade = new Mensalidade(aluno, mensalidadeAluno + matriculaAluno, dataAtual, Sistema.proxVecto(dataAtual), false);
				novasMensalidades.add(mensalidade);
				totalMatriculas += matriculaAluno;
				totalMensalidades += mensalidadeAluno;
			}
		} catch (jpvmException e) {
			throw new SlaveException("Não foi possível obter o histórico dos alunos", e);
		}
		Estatisticas.Financeiras financeiras = Sistema.estatisticas().financeiras();
		financeiras.setTotalProxMensalidades(totalMensalidades);
		financeiras.setTotalMatriculas(totalMatriculas);

		tryResponder(ResponseEscravo.OK);
	}

	private static List<Long> lerCodigos(String lista) {
		List<Long> cods = new ArrayList<>();
		for (String cod : Arrays.asList(lista.split(";"))) {
			cods.add(Long.parseLong(cod));
		}
		return cods;
	}

	private void processaMensalidadesAtrasadas(Operacao op) {
		Calendar dataAtual = (Calendar) op.getParam("dataReferencia");
		double totalMulta = 0;
		for (Collection<Mensalidade> mensAluno : mensalidades.values()) {
			for (Mensalidade mensalidade : mensAluno) {
				if (!mensalidade.isPaga() && dataAtual.after(mensalidade.getVencimento())) {
					totalMulta += mensalidade.calculaMulta();
					mensalidade.setAtrasada(true);
					mensagens.add(new Mensagem(mensalidade.getCodAluno(), //
							String.format("A mensalidade do dia %s está atrasada e gerou multa de R$ %.2f", Sistema.formatarData(mensalidade.getCompetencia()), totalMulta)));
				}
			}
		}
		Sistema.estatisticas().financeiras().setTotalMulta(totalMulta);
		tryResponder(ResponseEscravo.OK);
	}

}
