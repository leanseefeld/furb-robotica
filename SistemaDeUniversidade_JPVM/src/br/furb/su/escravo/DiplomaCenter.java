package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.writer.DiplomasWriter;
import br.furb.su.mestre.CursoCenterControle;
import br.furb.su.mestre.MensalidadeCenterControle;
import br.furb.su.modelo.Mensagem;
import br.furb.su.modelo.dados.Diploma;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.SituacaoDisciplina;
import br.furb.su.modelo.dados.SolicitacaoDiploma;
import br.furb.su.operacoes.Operacao;
import br.furb.su.operacoes.OperacaoFactory;

public class DiplomaCenter extends EscravoBase {

	public static final String GET_DIPLOMA = "diploma";

	private final SolicitacoesDiplomaReader reader = new SolicitacoesDiplomaReader();
	private final List<SolicitacaoDiploma> sols = new ArrayList<>();
	private final List<Diploma> diplomas = new ArrayList<>();
	private final DiplomasWriter writer = new DiplomasWriter();

	private MensalidadeCenterControle mensalidadeCenter;
	private CursoCenterControle cursoCenter;

	public DiplomaCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) throws jpvmException {
		new DiplomaCenter().run();
	}

	@Override
	protected void doSetSlave(String slaveName, jpvmTaskId taskId) {
		if (slaveName.equals("mensalidadeCenter")) {
			mensalidadeCenter = new MensalidadeCenterControle(pvm, taskId);
		} else if (slaveName.equals("cursoCenter")) {
			cursoCenter = new CursoCenterControle(pvm, taskId);
		}
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doUpload(String buffer) {
		sols.addAll(reader.ler(buffer));
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doDownload(String buffer) {
		if (buffer.equals("diplomas")) {
			tryResponder(ResponseEscravo.OK, writeToString(writer, diplomas));
		} else {
			super.doDownload(buffer);
		}
	}

	@Override
	protected void doGet(String buffer) {
		int idx = Math.max(buffer.indexOf('\n'), buffer.length());
		String item = buffer.substring(0, idx).toLowerCase();
		String paramsStr;
		String[] params;
		String[][] values;
		switch (item) {
		case GET_DIPLOMA:
			paramsStr = buffer.substring(idx + 1);
			params = paramsStr.split(";");
			values = new String[params.length][];
			Long codAluno = null;
			Integer codCurso = null;
			for (int i = 0; i < params.length; i++) {
				values[i] = params[i].split("=");
				if (values[i][0].equals("codAluno")) {
					codAluno = Long.parseLong(values[i][1]);
				} else if (values[i][0].equals("codCurso")) {
					codCurso = Integer.parseInt(values[i][1]);
				} else {
					tryResponder(ResponseEscravo.FAILURE, String.format(MSG_PARAM_NAO_RECONHECIDO, values[0], item));
					return;
				}
			}

			if (codAluno == null) {
				tryResponder(ResponseEscravo.FAILURE, String.format(MSG_PARAM_NAO_ENCONTRADO, "codAluno", item));
				return;
			}
			if (codCurso == null) {
				tryResponder(ResponseEscravo.FAILURE, String.format(MSG_PARAM_NAO_ENCONTRADO, "codCurso", item));
				return;
			}

			// TODO: recuperar e responder
			tryResponder(ResponseEscravo.OK, "TODO");
			break;
		default:
			super.doGet(item);
			return;
		}
	}

	@Override
	protected void doOperation(Operacao op) {
		if (op.getNome().equals(OperacaoFactory.NOME_PROCESS_DIPLOMAS)) {
			processaDiplomas(op);
		} else {
			tryResponder(ResponseEscravo.FAILURE, MSG_OP_NAO_RECONHECIDA);
		}
	}

	private void processaDiplomas(Operacao op) {
		Calendar dataAtual = (Calendar) op.getParam("dataAtual");
		try {
			for (SolicitacaoDiploma solic : sols) {
				boolean emissaoNegada = false;
				long aluno = solic.getCodAluno();
				if (mensalidadeCenter.alunoPossuiAtraso(aluno)) {
					mensagens.add(new Mensagem(aluno, "Não é possível emitir o diploma pois há mensalidade(s) atrasada(s)."));
				} else {
					int curso = solic.getCodCurso();
					List<Historico> historicos = cursoCenter.getHistoricoMaisRecente(aluno, curso);
					if (historicos.size() < cursoCenter.getTotalDisciplinasCurso(curso)) {
						mensagens.add(new Mensagem(aluno, "Não é possível emitir o diploma pois há disciplinas não cursadas."));
					} else {
						for (Historico historico : historicos) {
							if (historico.getSituacao() != SituacaoDisciplina.APROVADO) {
								mensagens.add(new Mensagem(aluno, "Não é possível emitir o diploma pois não há aprovação em todas as disciplinas do curso."));
								emissaoNegada = true;
								break;
							}
						}
						if (!emissaoNegada) {
							Diploma diploma = new Diploma(aluno, curso, dataAtual);
							diplomas.add(diploma);
						}
					}
				}
			}
		} catch (jpvmException e) {
			throw new SlaveException("Não foi possível obter o histórico dos alunos", e);
		}
		tryResponder(ResponseEscravo.OK);
	}

}
