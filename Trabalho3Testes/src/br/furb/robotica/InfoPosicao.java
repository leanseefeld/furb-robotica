package br.furb.robotica;

public class InfoPosicao {

    private final EstadoLado[] lados;

    public InfoPosicao() {
	lados = new EstadoLado[4];
    }

    public InfoPosicao(Lado... ladosLivres) {
	this();
	for (int i = 0; i < ladosLivres.length; i++) {
	    setLadoLivre(ladosLivres[i], true);
	}
    }
    
    private EstadoLado getEstadoLado(Lado lado) {
	return lados[lado.ordinal()];
    }

    public boolean isLadoLivre(Lado lado) {
	return getEstadoLado(lado) == EstadoLado.LIVRE;
    }

    public void setLadoLivre(Lado lado, boolean livre) {
	lados[lado.ordinal()] = EstadoLado.valueOf(livre);
    }

}
