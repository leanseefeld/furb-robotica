package br.furb.robotica;

public enum Lado {

	FRENTE, DIREITA, ATRAS, ESQUERDA;

	public static Lado valueOf(int i) {
		return Lado.values()[i];
	}

	public Lado getOposto() {
		switch (this) {
		case FRENTE:
			return ATRAS;
		case ATRAS:
			return FRENTE;
		case DIREITA:
			return ESQUERDA;
		case ESQUERDA:
			return DIREITA;
		default:
			throw new UnsupportedOperationException("lado não mapeado: "
					+ toString());
		}
	}
}
