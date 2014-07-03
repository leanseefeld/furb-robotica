package br.furb.su.mestre;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.Estatisticas;
import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.dataset.reader.HistoricosWriter;
import br.furb.su.dataset.writer.DiplomasWriter;
import br.furb.su.dataset.writer.EstatisticasWriter;
import br.furb.su.dataset.writer.MensagensWriter;
import br.furb.su.dataset.writer.MensalidadesWriter;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.DiplomaCenter;
import br.furb.su.escravo.EscravoBase;
import br.furb.su.escravo.MatriculaCenter;
import br.furb.su.escravo.MensalidadeCenter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.Mensagem;
import br.furb.su.modelo.dados.Aluno;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Diploma;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.Mensalidade;
import br.furb.su.modelo.dados.SolicitacaoDiploma;
import br.furb.su.modelo.dados.SolicitacaoMatricula;

/**
 * Mestre responsável por iniciar e coordenar a aplicação.
 * 
 * @author wseefeld
 * 
 */
public class Master {

	private jpvmEnvironment pvm;
	private Map<Class<?>, jpvmTaskId> idEscravos;
	private CursoCenterControle cursoControle;
	private DiplomaCenterControle diplomaControle;
	private MensalidadeCenterControle mensalidadeControle;
	private MatriculaCenterControle matriculaControle;

	public static void main(String[] args) throws jpvmException {
		new Master().run();
	}

	public void run() throws jpvmException {
		pvm = new jpvmEnvironment();
		Sistema.inicializar();
		try {
			obterEscravos();
			distribuirDados();
			doHandshake();
			processar();
			persistir();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			shutdownEscravos();
			pvm.pvm_exit();
		}
	}

	private void persistir() throws jpvmException, IOException {
		Collection<Historico> historicos = cursoControle.downloadHistorico();
		Collection<Diploma> diplomas = diplomaControle.downloadDiplomas();
		Collection<Mensalidade> mensalidades = mensalidadeControle.downloadMensalidades();
		Estatisticas estatisticas = mensalidadeControle.downloadEstatisticas();

		Collection<Mensagem> mensagens = new ArrayList<>();
		mensagens.addAll(cursoControle.downloadMensagens());
		mensagens.addAll(diplomaControle.downloadMensagens());
		mensagens.addAll(mensalidadeControle.downloadMensagens());
		mensagens.addAll(matriculaControle.downloadMensagens());

		final File pastaSaida = Sistema.getPastaSaida();

		new HistoricosWriter(pastaSaida).gravarArquivo(historicos);
		new DiplomasWriter(pastaSaida).gravarArquivo(diplomas);
		new MensalidadesWriter(pastaSaida).gravarArquivo(mensalidades);
		new EstatisticasWriter(pastaSaida).gravarArquivo(estatisticas.financeiras().toList());
		new MensagensWriter(pastaSaida).gravarArquivo(mensagens);
	}

	private void shutdownEscravos() {
		Sistema.debug("encerrando escravos");
		for (jpvmTaskId id : idEscravos.values()) {
			try {
				pvm.pvm_send(new jpvmBuffer(), id, RequestEscravo.KILL.tag());
			} catch (jpvmException e) {
				e.printStackTrace();
			}
		}
		Sistema.debug("escravos encerrados");
	}

	public void obterEscravos() throws jpvmException {
		idEscravos = new HashMap<>();

		Sistema.debug("obtendo escravos");
		for (Class<? extends EscravoBase> classeEscravo : Sistema.getEscravos()) {
			Sistema.debug("obtendo escravo: " + classeEscravo.getName());
			jpvmTaskId[] newIds = new jpvmTaskId[1];
			pvm.pvm_spawn(classeEscravo.getName(), 1, newIds);
			idEscravos.put(classeEscravo, newIds[0]);
		}
		Sistema.debug("todos os escravos obtidos");
	}

	public void distribuirDados() throws jpvmException {
		Sistema.debug("distribuindo dados");
		InDataset dados = Sistema.inDataset();

		jpvmTaskId ccId = idEscravos.get(CursoCenter.class);
		cursoControle = new CursoCenterControle(pvm, ccId);
		// cursos
		Sistema.debug("distribuindo cursos");
		for (Curso curso : dados.getCursosMap().values()) {
			cursoControle.insereCurso(curso);
		}
		// históricos
		Sistema.debug("distribuindo históricos");
		for (Historico historico : dados.getHistoricos()) {
			cursoControle.insereHistorico(historico);
		}
		// disciplinas
		Sistema.debug("distribuindo disciplinas");
		for (Disciplina disciplina : dados.getDisciplinasMap().values()) {
			cursoControle.insereDisciplina(disciplina);
		}

		jpvmTaskId dcId = idEscravos.get(DiplomaCenter.class);
		diplomaControle = new DiplomaCenterControle(pvm, dcId);
		// solicitações de diploma
		Sistema.debug("distribuindo solicitações de diploma");
		for (SolicitacaoDiploma solDip : dados.getSolicitacoesDiploma()) {
			diplomaControle.insereSolicitacaoDiploma(solDip);
		}

		jpvmTaskId mcId = idEscravos.get(MensalidadeCenter.class);
		mensalidadeControle = new MensalidadeCenterControle(pvm, mcId);
		// mensalidades
		Sistema.debug("distribuindo mensalidades");
		for (Mensalidade m : dados.getMensalidades()) {
			mensalidadeControle.insereMensalidade(m);
		}

		jpvmTaskId macId = idEscravos.get(MatriculaCenter.class);
		matriculaControle = new MatriculaCenterControle(pvm, macId);
		// solicitações de matricula
		Sistema.debug("distribuindo solicitações de matricula");
		for (SolicitacaoMatricula solMac : dados.getSolicitacoesMatricula()) {
			matriculaControle.insereSolicitacaoMatricula(solMac);
		}

		Sistema.debug("dados distribuídos");
	}

	private void doHandshake() throws jpvmException {
		Sistema.debug("iniciando handshake");
		matriculaControle.setCursoCenter(cursoControle.getTaskId());
		matriculaControle.setMensalidadeCenter(mensalidadeControle.getTaskId());
		diplomaControle.setCursoCenter(cursoControle.getTaskId());
		diplomaControle.setMensalidadeCenter(mensalidadeControle.getTaskId());
		mensalidadeControle.setCursoCenter(cursoControle.getTaskId());
		Sistema.debug("handshake concluído");
	}

	public void processar() throws jpvmException {
		Sistema.debug("iniciando processamento");

		Sistema.debug("processamento mensalidades atrasadas");
		mensalidadeControle.processaMensalidadesAtrasadas();
		Sistema.debug("aguardando mensalidades atrasadas");
		mensalidadeControle.waitResposta();

		Sistema.debug("processamento matrículas");
		matriculaControle.processarMatriculas();
		Sistema.debug("processamento diplomas");
		diplomaControle.processaDiplomas(Sistema.getDataAtual());

		Sistema.debug("aguardando matrículas");
		matriculaControle.waitResposta();
		List<Aluno> alunosAtivos = Sistema.filtraAlunosAtivos(Sistema.inDataset());
		Sistema.debug("processando novas mensalidades");
		mensalidadeControle.processarNovasMensalidades(alunosAtivos, Sistema.getDataAtual());

		Sistema.debug("aguardando diplomas");
		diplomaControle.waitResposta();
		mensalidadeControle.waitResposta();

		Sistema.debug("processamento concluído");
	}

}
