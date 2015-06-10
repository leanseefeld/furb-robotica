package br.furb.robotica;

public class InfoPosicao {

    private final EstadoLado[] lados;

    public InfoPosicao() {
	lados = new EstadoLado[4];
    }

    public InfoPosicao(Lado... ladosLivres) {
	this();
	this.setLadoLivre(ladosLivres);
    }

    public boolean isLadoLivre(Lado lado) {
	return getEstadoLado(lado) == EstadoLado.LIVRE;
    }

    public void setLadoLivre(Lado... ladosLivres) {
	Debug.print("Lado livre:");
	for (int i = 0; i < ladosLivres.length; i++) {
	    Debug.print(ladosLivres[i].name() + "  ");
	    setLadoLivre(ladosLivres[i], true);
	}
	Debug.println();
    }

    public void setLadoLivre(Lado lado, boolean livre) {
	lados[lado.ordinal()] = EstadoLado.valueOf(livre);
    }

    private EstadoLado getEstadoLado(Lado lado) {
	return lados[lado.ordinal()];
    }
}
