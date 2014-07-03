package br.furb.su.escravo;

@SuppressWarnings("serial")
public class SlaveException extends RuntimeException {

	public SlaveException(String message) {
		super(message);
	}
	
	public SlaveException(String message, Throwable cause) {
		super(message, cause);
	}

}
