package br.furb.robotica;

public enum Sentido {

    NORTE,
    LESTE,
    SUL,
    OESTE;

    public static Sentido valueOf(int i) {
	return Sentido.values()[i];
    }

    public Sentido getOposto() {
	switch (this) {
	    case NORTE:
		return SUL;
	    case SUL:
		return NORTE;
	    case LESTE:
		return OESTE;
	    case OESTE:
		return LESTE;
	    default:
		throw new UnsupportedOperationException("lado não mapeado: " + toString());
	}
    }
}
