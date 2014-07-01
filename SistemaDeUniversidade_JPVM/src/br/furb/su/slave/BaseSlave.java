package br.furb.su.slave;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;

public abstract class BaseSlave {
	
	final jpvmEnvironment pvm;
	
	public BaseSlave() throws jpvmException {
		pvm = new jpvmEnvironment(true);
	}

}
