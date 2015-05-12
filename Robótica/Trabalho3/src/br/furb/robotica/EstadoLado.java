package br.furb.robotica;

public enum EstadoLado {

	LIVRE, PAREDE;

	public static EstadoLado valueOf(boolean livre) {
		return livre ? LIVRE : PAREDE;
	}

}
