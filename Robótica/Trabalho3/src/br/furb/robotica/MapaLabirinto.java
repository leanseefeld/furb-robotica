package br.furb.robotica;

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

    /**
     * Busca o menor caminho até uma posição utilizando o wave front
     * 
     * @param origem
     * @param destino
     * @param minhaPilha
     * @return
     */
    public Caminho montarCaminhoWaveFront(int[] origem, int[] destino, MinhaPilha<int[]> minhaPilha) {
	Wavefront wave = new Wavefront(this, origem, destino, minhaPilha);
	return wave.gerarCaminho();
    }

    /**
     * Busca o menor caminho até uma posição utilizando o algoritmo de Dijkstra
     * 
     * @param origem
     * @param destino
     * @return
     */
    public Caminho montarCaminhoDijkstra(int[] origem, int[] destino) {
	int[][] grafo = this.converterParaGrafo();
	int origemGrafo = this.converterIndexGrafo(origem);
	int origemDestino = this.converterIndexGrafo(destino);
	Dijkstra dijkstra = new Dijkstra(grafo, origemGrafo, origemDestino);
	dijkstra.doSearch();
	int[] menorCaminhoGrafo = dijkstra.getMinPathRoute();

	Caminho caminho = new Caminho();
	for (int indexGrafo : menorCaminhoGrafo) {
	    int[] coordenada = this.converteCoordenada(indexGrafo);
	    caminho.addPasso(coordenada);
	}
	return caminho;
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

    /**
     * Retorna todos as coordenadas vizinhas conexas (que possuem passagem entre si) <br>
     * 
     * @param celula
     *            Coordenada cujo os vizinhos se deseja saber.
     * @param apenasConexos
     *            Se true, busca apenas as coordenadas conexas <br>
     *            <span style="color:red">Não funciona se esta celula não foi explorada</span>
     * @param apenasExplorados
     *            Se true, busca apenas as coordenadas que já foram exploradas
     * @return vizinhos conexos
     */
    public int[][] getVizinhos(int[] celula, boolean apenasConexos, boolean apenasExplorados) {
	List<int[]> vizinhos = new ArrayList<>(4);

	if (!this.isIndicesValidos(celula) || (apenasConexos && this.getInfoPosicao(celula) == null)) {
	    return new int[0][2];
	}

	List<int[]> possiveisVizinhos = getTodosVizinhos(celula);

	for (int[] vizinho : possiveisVizinhos) {
	    if (this.isIndicesValidos(vizinho)) {
		if (!apenasExplorados || getInfoPosicao(vizinho) != null) {
		    if (!apenasConexos || existePassagem(celula, vizinho)) {
			vizinhos.add(vizinho);
		    }
		}
	    }
	}
	return vizinhos.toArray(new int[vizinhos.size()][2]);
    }

    public List<int[]> getTodosVizinhos(int[] celula) {
	List<int[]> possiveisVizinhos = criarLista( //
		new int[] { celula[Matriz.LINHA] + 1, celula[Matriz.COLUNA] }, // 
		new int[] { celula[Matriz.LINHA] - 1, celula[Matriz.COLUNA] }, //
		new int[] { celula[Matriz.LINHA], celula[Matriz.COLUNA] + 1 }, //
		new int[] { celula[Matriz.LINHA], celula[Matriz.COLUNA] - 1 });
	return possiveisVizinhos;
    }

    public int[][] converterParaGrafo() {
	int[][] grafo = new int[this.posicoes.length * this.posicoes[0].length][this.posicoes.length
		* this.posicoes[0].length];

	for (int lin = 0; lin < this.posicoes.length; lin++) {
	    for (int col = 0; col < this.posicoes[lin].length; col++) {
		int[] coordenada = new int[] { lin, col };
		int[][] vizinhos = this.getVizinhos(coordenada, true, false);

		int u = converteIndexGrafo(lin, col);

		for (int[] vizinho : vizinhos) {
		    int v = converterIndexGrafo(vizinho);
		    grafo[u][v] = 1;
		}
	    }
	}
	return grafo;
    }

    private int converteIndexGrafo(int linha, int coluna) {
	return this.posicoes.length * linha + coluna;
    }

    private int converterIndexGrafo(int[] coordenada) {
	return converteIndexGrafo(coordenada[Matriz.LINHA], coordenada[Matriz.COLUNA]);
    }

    public int[] converteCoordenada(int indexGrafo) {
	int[] coordenada = new int[2];
	coordenada[Matriz.LINHA] = indexGrafo / this.posicoes.length;
	coordenada[Matriz.COLUNA] = indexGrafo % this.posicoes.length;
	return coordenada;
    }

    public static final List<int[]> criarLista(int[]... cells) {
	List<int[]> ret = new ArrayList<>();
	for (int[] cell : cells) {
	    ret.add(cell);
	}
	return ret;
    }
}
