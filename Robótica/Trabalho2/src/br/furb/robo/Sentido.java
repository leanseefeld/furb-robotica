package br.furb.robo;

public enum Sentido {
	Norte(1), Leste(2), Sul(3), Oeste(4);

	public int value;

	private Sentido(int value) {
		this.value = value;
	}

}
