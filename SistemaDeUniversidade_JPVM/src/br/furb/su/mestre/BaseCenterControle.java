package br.furb.su.mestre;

import br.furb.su.escravo.LockException;
import br.furb.su.escravo.ResponseEscravo;
import br.furb.su.escravo.SlaveException;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;

public abstract class BaseCenterControle {

	protected final jpvmTaskId tid;
	protected final jpvmEnvironment pvm;

	public BaseCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		this.tid = tid;
		this.pvm = pvm;
	}

	protected static void checkErrorResponse(jpvmMessage msg) throws jpvmException {
		if (msg.messageTag == ResponseEscravo.LOCKED.tag()) {
			throw new LockException(msg.buffer.upkstr());
		}
		if (msg.messageTag == ResponseEscravo.FAILURE.tag()) {
			throw new SlaveException(msg.buffer.upkstr());
		}
	}

	public jpvmTaskId getTaskId() {
		return tid;
	}

}
