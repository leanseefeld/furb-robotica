package br.furb.robotica;

public class MapaLabirinto {

	public static final int LINHA = 0; // i | "for" de fora
	public static final int COLUNA = 1; // j | "for" de dentro
	private final InfoPosicao[][] posicoes;

	private int[] coordenadaOrigem;
	private int[] coordenadaDestino;

	public MapaLabirinto() {
		posicoes = new InfoPosicao[4][4];
	}

	public void setCoordenadaOrigem(int linha, int col) {
		coordenadaOrigem = new int[2];
		coordenadaOrigem[LINHA] = linha;
		coordenadaOrigem[COLUNA] = col;
	}

	public void setCoordenadaDestino(int linha, int col) {
		coordenadaDestino = new int[2];
		coordenadaDestino[LINHA] = linha;
		coordenadaDestino[COLUNA] = col;
	}

	public InfoPosicao getInfoPosicao(int[] coordenada) {
		return posicoes[coordenada[LINHA]][coordenada[COLUNA]];
	}

	public InfoPosicao criarPosicao(int[] coordenada, Lado ladoOrigem) {
		return posicoes[coordenada[LINHA]][coordenada[COLUNA]] = new InfoPosicao(
				ladoOrigem);
	}

}
