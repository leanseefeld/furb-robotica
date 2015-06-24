package br.furb.robotica;

public class InfoPosicao {

    private final EstadoLado[] lados;

    public InfoPosicao() {
	lados = new EstadoLado[4];
    }

    public InfoPosicao(Sentido... ladosLivres) {
	this();
	this.setLadoLivre(ladosLivres);
    }

    public boolean isLadoLivre(Sentido lado) {
	return getEstadoLado(lado) == EstadoLado.LIVRE;
    }

    public void setLadoLivre(Sentido... ladosLivres) {
	Debug.print("Lado livre:");
	for (int i = 0; i < ladosLivres.length; i++) {
	    Debug.print(ladosLivres[i].name() + "  ");
	    setLadoLivre(ladosLivres[i], true);
	}
	Debug.println();
    }

    public void setLadoLivre(Sentido lado, boolean livre) {
	lados[lado.ordinal()] = EstadoLado.valueOf(livre);
    }

    private EstadoLado getEstadoLado(Sentido lado) {
	return lados[lado.ordinal()];
    }
}
