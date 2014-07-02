package br.furb.su.escravo;

import jpvm.jpvmException;
import jpvm.jpvmMessage;

public abstract class BaseCenterControle {
	
	protected static void checkErrorResponse(jpvmMessage msg) throws jpvmException {
		if (msg.messageTag == ResponseEscravo.LOCKED.tag()) {
			throw new LockException(msg.buffer.upkstr());
		}
		if (msg.messageTag == ResponseEscravo.FAILURE.tag()) {
			throw new SlaveException(msg.buffer.upkstr());
		}
	}

}
