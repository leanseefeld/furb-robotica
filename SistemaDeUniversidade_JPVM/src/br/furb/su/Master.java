package br.furb.su;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.escravo.MensalidadeCenter;

public class Master {

	private static jpvmEnvironment pvm;
	private jpvmTaskId tids[];

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
		int num_workers = Sistema.NUM_ESCRAVOS;
		tids = new jpvmTaskId[num_workers];
		
		// TODO: definir classes e quantidades
		pvm.pvm_spawn(MensalidadeCenter.class.getName(), num_workers, tids);

		/* TODO: caso implementar carregamento remoto de classe para atribuir novas 
		 funções ao escravo durante a execução, "convertê-los" aqui */
	}

	public void distribuirDados() {

	}

}
