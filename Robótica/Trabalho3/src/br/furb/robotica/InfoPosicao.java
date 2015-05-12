package br.furb.robotica;

public class InfoPosicao {

	private final EstadoLado[] lados;

	public InfoPosicao() {
		lados = new EstadoLado[4];
	}

	public InfoPosicao(Lado origem) {
		this();
		setLadoLivre(origem.getOposto(), true);
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
