package br.furb.su.mestre;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	private Map<Class<?>, jpvmTaskId[]> idsEscravos;

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
		idsEscravos = new HashMap<>();

		for (Entry<Class<? extends EscravoBase>, Integer> cfg : Sistema.getConfigEscravos().entrySet()) {
			int qtEscravos = cfg.getValue().intValue();
			jpvmTaskId[] newIds = new jpvmTaskId[qtEscravos];
			final Class<? extends EscravoBase> classeEscravo = cfg.getKey();
			pvm.pvm_spawn(classeEscravo.getName(), qtEscravos, newIds);
			idsEscravos.put(classeEscravo, newIds);
		}
	}

	public void distribuirDados() {
		InDataset dados = Sistema.inDataset();

		jpvmTaskId[] ids = idsEscravos.get(CursoCenter.class.getName());
		CursoCenterControle ccc = new CursoCenterControle(ids);
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
