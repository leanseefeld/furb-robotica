package br.furb.teste.robotica;

import java.util.ArrayList;
import java.util.List;
import br.furb.robotica.Caminho;
import br.furb.robotica.InfoPosicao;
import br.furb.robotica.Lado;
import br.furb.robotica.Matriz;

public class MapaLabirinto {

    private final InfoPosicao[][] posicoes;

    private int[] coordenadaOrigem;
    private int[] coordenadaDestino;

    public MapaLabirinto() {
	posicoes = new InfoPosicao[4][4];
    }

    public void setCoordenadaOrigem(int linha, int col) {
	coordenadaOrigem = new int[2];
	coordenadaOrigem[Matriz.LINHA] = linha;
	coordenadaOrigem[Matriz.COLUNA] = col;
    }

    public void setCoordenadaDestino(int linha, int col) {
	coordenadaDestino = new int[2];
	coordenadaDestino[Matriz.LINHA] = linha;
	coordenadaDestino[Matriz.COLUNA] = col;
    }

    public InfoPosicao getInfoPosicao(int[] coordenada) {
	return posicoes[coordenada[Matriz.LINHA]][coordenada[Matriz.COLUNA]];
    }

    public InfoPosicao criarPosicao(int[] coordenada, Lado ladoOrigem) {
	return posicoes[coordenada[Matriz.LINHA]][coordenada[Matriz.COLUNA]] = new InfoPosicao(ladoOrigem);
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
	if (coord[Matriz.LINHA] < 0 || coord[Matriz.COLUNA] < 0)
	    return false;
	if (coord[Matriz.LINHA] >= posicoes.length)
	    return false;
	if (coord[Matriz.COLUNA] >= posicoes[coord[Matriz.LINHA]].length)
	    return false;
	return true;
    }

    public List<int[]> getVisinhosNaoVisitado(int[] coordenadaAtual) {
	InfoPosicao infoPosicaoAtual = getInfoPosicao(coordenadaAtual);
	List<int[]> coords = new ArrayList<int[]>();

	if (infoPosicaoAtual.isLadoLivre(Lado.DIREITA)) {
	    int[] coord = coordenadaAtual.clone();
	    coord[Matriz.COLUNA]++;
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) == null) {
		coords.add(coord);
	    }
	} else if (infoPosicaoAtual.isLadoLivre(Lado.ESQUERDA)) {
	    int[] coord = coordenadaAtual.clone();
	    coord[Matriz.COLUNA]--;
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) == null) {
		coords.add(coord);
	    }
	} else if (infoPosicaoAtual.isLadoLivre(Lado.FRENTE)) {
	    int[] coord = coordenadaAtual.clone();
	    coord[Matriz.LINHA]--;
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) == null) {
		coords.add(coord);
	    }
	} else if (infoPosicaoAtual.isLadoLivre(Lado.ATRAS)) {
	    int[] coord = coordenadaAtual.clone();
	    coord[Matriz.LINHA]++;
	    if (coordenadaEhValida(coord) && getInfoPosicao(coord) == null) {
		coords.add(coord);
	    }
	}
	return coords;
    }

    public List<int[]> getCoordenadasNaoVisitadas() {
	List<int[]> naoVisitadas = new ArrayList<int[]>();
	for (int lin = 0; lin < this.posicoes.length; lin++) {
	    for (int col = 0; col < this.posicoes[lin].length; col++) {
		if (this.posicoes[lin][col] == null) {
		    int[] coordenada = new int[2];
		    coordenada[Matriz.LINHA] = lin;
		    coordenada[Matriz.COLUNA] = col;
		    naoVisitadas.add(coordenada);
		}
	    }
	}
	return naoVisitadas;
    }

    public Caminho montarCaminho(int[] origem, int[] destino) {
	Wavefront wave = new Wavefront(this, origem, destino);
	return wave.gerarCaminho();
    }

    /**
     * Verifica para qual lado fica a coordenada de destino
     * 
     * @param origem
     *            coordenada de origem
     * @param destino
     *            coordenada visinha de destino
     * @return Direção da coordenada de destino em relação a coordenada de origem
     */
    public Lado getLado(int[] origem, int[] destino) {
	Lado novoLado = null;
	if (origem[Matriz.COLUNA] - destino[Matriz.COLUNA] < 0) {
	    novoLado = Lado.DIREITA;
	} else if (origem[Matriz.COLUNA] - destino[Matriz.COLUNA] > 0) {
	    novoLado = Lado.ESQUERDA;
	}

	if (origem[Matriz.LINHA] - destino[Matriz.LINHA] < 0) {
	    novoLado = Lado.ATRAS;
	} else if (origem[Matriz.LINHA] - destino[Matriz.LINHA] > 0) {
	    novoLado = Lado.FRENTE;
	}

	return novoLado;
    }

    /**
     * Verifica se existe passagem entre duas coordenadas
     * 
     * @param origem
     * @param destino
     * @return
     */
    public boolean existePassagem(int[] origem, int[] destino) {
	Lado lado = this.getLado(origem, destino);
	boolean existe = this.getInfoPosicao(origem).isLadoLivre(lado);
	return existe;
    }

    /**
     * Verifica se a coordenada destá dentro da matriz
     * 
     * @param coordenada
     * @return
     */
    public boolean isIndicesValidos(int[] coordenada) {
	return coordenada[Matriz.COLUNA] < this.posicoes.length && coordenada[Matriz.LINHA] < this.posicoes[0].length // 
		&& coordenada[Matriz.COLUNA] >= 0 && coordenada[Matriz.LINHA] >= 0;
    }

    public InfoPosicao[][] getPosicoes() {
	return this.posicoes;
    }
    
    public boolean comparaCooredenadas(int[] a, int[] b) {
	return a[Matriz.COLUNA] == b[Matriz.COLUNA] && a[Matriz.LINHA] == b[Matriz.LINHA];
    }
}
