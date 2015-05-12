package br.furb.robotica;

public class RoboMapeador {

	public static void main(String[] args) {
	}

	private int[] coordenadaAtual;
	private Lado ladoAtual;
	private MapaLabirinto mapa;

	public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual) {
		this.mapa = mapa;
		this.ladoAtual = ladoAtual;
	}

	public void analizarPosicao() {
		InfoPosicao infoPosicao = mapa.getInfoPosicao(coordenadaAtual);
		if (infoPosicao == null) {
			infoPosicao = mapa.criarPosicao(coordenadaAtual, ladoAtual);
		}
	}

}
