package br.furb.su.mestre;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.Nucleo;
import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.escravo.EscravoBase;

/**
 * Mestre responsável por iniciar e coordenar a aplicação.
 * 
 * @author wseefeld
 * 
 */
public class Master {

	private static jpvmEnvironment pvm;
	private Map<Class<?>, jpvmTaskId[]> tids;

	public static void main(String[] args) throws jpvmException {
		Master m = new Master();
		m.run();
	}

	public void run() throws jpvmException {
		pvm = new jpvmEnvironment();
		Nucleo.carregarDados();
		obterEscravos();
		distribuirDados();
	}

	public void obterEscravos() throws jpvmException {
		/* caso implementar carregamento remoto de classe para atribuir novas 
		 funções ao escravo durante a execução, "convertê-los" aqui */
		tids = new HashMap<Class<?>, jpvmTaskId[]>();

		for (Entry<Class<? extends EscravoBase>, Integer> cfg : Sistema.getConfigEscravos().entrySet()) {
			jpvmTaskId[] newIds = new jpvmTaskId[cfg.getValue()];
			pvm.pvm_spawn(cfg.getKey().getName(), cfg.getValue(), newIds);
			tids.put(cfg.getKey(), newIds);
		}
	}

	public void distribuirDados() {
		InDataset dados = Sistema.inDataset();
		dados.getAlunos();
		// TODO
	}

}
