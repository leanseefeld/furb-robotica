package br.furb.su.escravo;

import jpvm.jpvmException;

public class MatriculaCenter extends EscravoBase {

	public MatriculaCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) {
		try {
			new MatriculaCenter().run();
		} catch (jpvmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doUpload(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doDownload(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doGet(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doOperation(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetSlave(String buffer) {
		// TODO Auto-generated method stub

	}

}
