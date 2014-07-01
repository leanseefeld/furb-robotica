package br.furb.su.escravo;

import jpvm.jpvmEnvironment;
import jpvm.jpvmException;

public abstract class EscravoBase {
	
	final jpvmEnvironment pvm;
	
	public EscravoBase() throws jpvmException {
		pvm = new jpvmEnvironment(true);
	}

}
