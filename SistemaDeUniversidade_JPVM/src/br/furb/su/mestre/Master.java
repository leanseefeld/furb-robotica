package br.furb.su.mestre;

import java.util.HashMap;
import java.util.Map;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.DiplomaCenter;
import br.furb.su.escravo.EscravoBase;
import br.furb.su.escravo.MatriculaCenter;
import br.furb.su.escravo.MensalidadeCenter;
import br.furb.su.modelo.dados.Curso;
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

	private static jpvmEnvironment pvm;
	private Map<Class<?>, jpvmTaskId> idEscravos;
	private CursoCenterControle cursoControle;
	private DiplomaCenterControle diplomaControle;
	private MensalidadeCenterControle mensalidadeControle;
	private MatriculaCenterControle matriculaControle;

	public static void main(String[] args) throws jpvmException {
		Master m = new Master();
		m.run();
	}

	public void run() throws jpvmException {
		pvm = new jpvmEnvironment();
		Sistema.inicializar();
		obterEscravos();
		distribuirDados();
		processar();
	}

	public void obterEscravos() throws jpvmException {
		idEscravos = new HashMap<>();

		for (Class<? extends EscravoBase> classeEscravo : Sistema.getEscravos()) {
			Sistema.debug("obtendo escravo: " + classeEscravo.getName());
			jpvmTaskId[] newIds = new jpvmTaskId[1];
			pvm.pvm_spawn(classeEscravo.getName(), 1, newIds);
			idEscravos.put(classeEscravo, newIds[0]);
		}
	}

	public void distribuirDados() throws jpvmException {
		InDataset dados = Sistema.inDataset();

		jpvmTaskId ccId = idEscravos.get(CursoCenter.class.getName());
		cursoControle = new CursoCenterControle(pvm, ccId);
		// cursos
		for (Curso curso : dados.getCursosMap().values()) {
			cursoControle.insereCurso(curso);
		}
		// históricos
		for (Historico historico : dados.getHistoricos()) {
			cursoControle.insereHistorico(historico);
		}
		// disciplinas
		for (Disciplina disciplina : dados.getDisciplinasMap().values()) {
			cursoControle.insereDisciplina(disciplina);
		}

		jpvmTaskId dcId = idEscravos.get(DiplomaCenter.class);
		diplomaControle = new DiplomaCenterControle(pvm, dcId);
		// solicitações de diploma
		for (SolicitacaoDiploma solDip : dados.getSolicitacoesDiploma()) {
			diplomaControle.insereSolicitacaoDiploma(solDip);
		}

		jpvmTaskId mcId = idEscravos.get(MensalidadeCenter.class);
		mensalidadeControle = new MensalidadeCenterControle(pvm, mcId);
		// mensalidades
		for (Mensalidade m : dados.getMensalidades()) {
			mensalidadeControle.insereMensalidade(m);
		}

		jpvmTaskId macId = idEscravos.get(MatriculaCenter.class);
		matriculaControle = new MatriculaCenterControle(pvm, macId);
		// solicitações de matricula
		for (SolicitacaoMatricula solMac : dados.getSolicitacoesMatricula()) {
			matriculaControle.insereSolicitacaoMatricula(solMac);
		}

		doHandshake();
	}

	private void doHandshake() throws jpvmException {
		matriculaControle.setCursoCenter(cursoControle.getTaskId());
		matriculaControle.setMensalidadeCenter(mensalidadeControle.getTaskId());
		diplomaControle.setCursoCenter(cursoControle.getTaskId());
		diplomaControle.setMensalidadeCenter(mensalidadeControle.getTaskId());
	}

	public void processar() {

	}

}
