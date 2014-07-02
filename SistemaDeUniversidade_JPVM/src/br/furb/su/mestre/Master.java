package br.furb.su.mestre;

import java.util.HashMap;
import java.util.Map;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.CursoCenterControle;
import br.furb.su.escravo.EscravoBase;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;

/**
 * Mestre responsável por iniciar e coordenar a aplicação.
 * 
 * @author wseefeld
 * 
 */
public class Master {

	private static jpvmEnvironment pvm;
	private Map<Class<?>, jpvmTaskId> idEscravos;

	public static void main(String[] args) throws jpvmException {
		Master m = new Master();
		m.run();
	}

	public void run() throws jpvmException {
		pvm = new jpvmEnvironment();
		Sistema.inicializar();
		obterEscravos();
		distribuirDados();
	}

	public void obterEscravos() throws jpvmException {
		idEscravos = new HashMap<>();

		for (Class<? extends EscravoBase> classeEscravo : Sistema.getEscravos()) {
			jpvmTaskId[] newIds = new jpvmTaskId[1];
			pvm.pvm_spawn(classeEscravo.getName(), 1, newIds);
			idEscravos.put(classeEscravo, newIds[0]);
		}
	}

	public void distribuirDados() throws jpvmException {
		InDataset dados = Sistema.inDataset();

		jpvmTaskId id = idEscravos.get(CursoCenter.class.getName());
		CursoCenterControle ccc = new CursoCenterControle(pvm, id);
		for (Curso curso : dados.getCursosMap().values()) {
			ccc.insereCurso(curso);
		}
		for (Historico historico : dados.getHistoricos()) {
			ccc.insereHistorico(historico);
		}
		for (Disciplina disciplina : dados.getDisciplinasMap().values()) {
			ccc.insereDisciplina(disciplina);
		}
		// TODO
	}

}
