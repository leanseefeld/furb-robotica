package br.furb.robotica;

import java.util.List;

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
	return posicoes[coordenada[LINHA]][coordenada[COLUNA]] = new InfoPosicao(ladoOrigem);
    }

    public boolean existePosicaoNaoVisitada() {
	for (int i = 0; i < this.posicoes.length; i++) {
	    for (int j = 0; j < this.posicoes[i].length; j++) {
		if (this.posicoes[i][j] == null)
		    return true;
	    }
	}
	return false;
    }

    public boolean coordenadaEhValida(int[] coord) {
	if (coord[LINHA] >= posicoes.length) {
	    return false;
	}
	if (coord[COLUNA] >= posicoes[coord[LINHA]].length) {
	    return false;
	}
	return true;
    }

    public int[] getVisinhoNaoVisitado(int[] coordenadaAtual) {
	InfoPosicao infoPosicaoAtual = getInfoPosicao(coordenadaAtual);
	int[] coord = new int[2];

	if (infoPosicaoAtual.isLadoLivre(Lado.DIREITA)) {
	    coord[MapaLabirinto.COLUNA] = coordenadaAtual[MapaLabirinto.COLUNA]++;
	    coord[MapaLabirinto.LINHA] = coordenadaAtual[MapaLabirinto.LINHA];
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) != null) {
		return coord;
	    }
	} else if (infoPosicaoAtual.isLadoLivre(Lado.ESQUERDA)) {
	    coord[MapaLabirinto.COLUNA] = coordenadaAtual[MapaLabirinto.COLUNA]--;
	    coord[MapaLabirinto.LINHA] = coordenadaAtual[MapaLabirinto.LINHA];
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) != null) {
		return coord;
	    }
	} else if (infoPosicaoAtual.isLadoLivre(Lado.FRENTE)) {
	    coord[MapaLabirinto.COLUNA] = coordenadaAtual[MapaLabirinto.COLUNA];
	    coord[MapaLabirinto.LINHA] = coordenadaAtual[MapaLabirinto.LINHA]++;
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) != null) {
		return coord;
	    }
	} else {
	    coord[MapaLabirinto.COLUNA] = coordenadaAtual[MapaLabirinto.COLUNA];
	    coord[MapaLabirinto.LINHA] = coordenadaAtual[MapaLabirinto.LINHA]--;
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) != null) {
		return coord;
	    }
	}
	return null;
    }

    public List<int[]> getCoordenadasNaoVisitadas() {
	// TODO Auto-generated method stub
	return null;
    }

    public Caminho montarCaminho(int[] origem, int[] destino) {
	// TODO Auto-generated method stub
	return null;
    }
}
