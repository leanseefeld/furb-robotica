package br.furb.su;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.slave.Mensalator;

public class Master {

	public static void main(String[] args) throws jpvmException {
		jpvmEnvironment pvm = new jpvmEnvironment();
		int num_workers = 3;
		jpvmTaskId tids[] = new jpvmTaskId[num_workers];
		pvm.pvm_spawn(Mensalator.class.getName(), num_workers, tids);

		for (jpvmTaskId tid : tids) {
			jpvmMessage message = pvm.pvm_recv();
			String content = message.buffer.upkstr();
			System.out.println(content);
		}
		
	}

}
