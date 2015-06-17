package br.furb.robotica;

import java.util.ArrayList;
import java.util.List;
import util.Dijkstra;

@Deprecated
public class MapaLabirinto {

    public static final List<int[]> criarLista(int[]... cells) {
	List<int[]> ret = new ArrayList<>();
	for (int[] cell : cells) {
	    ret.add(cell);
	}
	return ret;
    }

    private final InfoPosicao[][] posicoes;

    private int[] coordenadaDestino;

    public MapaLabirinto(int tamanho) {
	posicoes = new InfoPosicao[tamanho][tamanho];
    }

    public boolean comparaCooredenadas(int[] a, int[] b) {
	return a[Matriz.COLUNA] == b[Matriz.COLUNA] && a[Matriz.LINHA] == b[Matriz.LINHA];
    }

    public int[] converteCoordenada(int indexGrafo) {
	int[] coordenada = new int[2];
	coordenada[Matriz.LINHA] = indexGrafo / this.posicoes.length;
	coordenada[Matriz.COLUNA] = indexGrafo % this.posicoes.length;
	return coordenada;
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

    public boolean coordenadaEhValida(int[] coord) {
	if (coord[Matriz.LINHA] < 0 || coord[Matriz.COLUNA] < 0)
	    return false;
	if (coord[Matriz.LINHA] >= posicoes.length)
	    return false;
	if (coord[Matriz.COLUNA] >= posicoes[coord[Matriz.LINHA]].length)
	    return false;
	return true;
    }

    public InfoPosicao criarPosicao(int[] coordenada) {
	return posicoes[coordenada[Matriz.LINHA]][coordenada[Matriz.COLUNA]] = new InfoPosicao();
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

    public boolean existePosicaoNaoVisitada() {
	for (int i = 0; i < this.posicoes.length; i++) {
	    for (int j = 0; j < this.posicoes[i].length; j++) {
		if (this.posicoes[i][j] == null)
		    return true;
	    }
	}
	return false;
    }

    public int[] getCoordenadaDestino() {
	return this.coordenadaDestino;
    }

    /**
     * Retorna todas as coordenadas que não foram exploradas
     * 
     * @return
     */
    public List<int[]> getCoordenadasNaoExploradas() {
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

    public InfoPosicao getInfoPosicao(int[] coordenada) {
	return posicoes[coordenada[Matriz.LINHA]][coordenada[Matriz.COLUNA]];
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

    public InfoPosicao[][] getPosicoes() {
	return this.posicoes;
    }

    public List<int[]> getTodosVizinhos(int[] celula) {
	List<int[]> possiveisVizinhos = criarLista( //
		new int[] { celula[Matriz.LINHA] + 1, celula[Matriz.COLUNA] }, // 
		new int[] { celula[Matriz.LINHA] - 1, celula[Matriz.COLUNA] }, //
		new int[] { celula[Matriz.LINHA], celula[Matriz.COLUNA] + 1 }, //
		new int[] { celula[Matriz.LINHA], celula[Matriz.COLUNA] - 1 });
	return possiveisVizinhos;
    }

    /**
     * Retorna todos as coordenadas vizinhas conexas que não foram explordas
     * 
     * @param coordenada
     *            coordenada base
     * @return
     */
    public int[][] getVisinhosConexosNaoExplorados(int[] coordenada) {
	List<int[]> vizinhos = new ArrayList<>(4);

	if (!this.isIndicesValidos(coordenada) || this.getInfoPosicao(coordenada) == null) {
	    return new int[0][2];
	}

	List<int[]> possiveisVizinhos = getTodosVizinhos(coordenada);

	for (int[] vizinho : possiveisVizinhos) {
	    if (this.isIndicesValidos(vizinho)) {
		if (getInfoPosicao(vizinho) == null) {
		    if (existePassagem(coordenada, vizinho)) {
			vizinhos.add(vizinho);
		    }
		}
	    }
	}
	return vizinhos.toArray(new int[vizinhos.size()][2]);
    }

    /**
     * Retorna todos as coordenadas vizinhas conexas (que possuem passagem entre si) <br>
     * 
     * @param coordenada
     *            Coordenada cujo os vizinhos se deseja saber.
     * @param apenasConexos
     *            Se true, busca apenas as coordenadas conexas <br>
     *            <span style="color:red">Não funciona se esta celula não foi explorada</span>
     * @param apenasExplorados
     *            Se true, busca apenas as coordenadas que já foram exploradas
     * @return vizinhos conexos
     */
    public int[][] getVizinhos(int[] coordenada, boolean apenasConexos, boolean apenasExplorados) {
	List<int[]> vizinhos = new ArrayList<>(4);

	if (!this.isIndicesValidos(coordenada) || (apenasConexos && this.getInfoPosicao(coordenada) == null)) {
	    return new int[0][2];
	}

	List<int[]> possiveisVizinhos = getTodosVizinhos(coordenada);

	for (int[] vizinho : possiveisVizinhos) {
	    if (this.isIndicesValidos(vizinho)) {
		if (!apenasExplorados || getInfoPosicao(vizinho) != null) {
		    if (!apenasConexos || existePassagem(coordenada, vizinho)) {
			vizinhos.add(vizinho);
		    }
		}
	    }
	}
	return vizinhos.toArray(new int[vizinhos.size()][2]);
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

    public void setCoordenadaDestino(int linha, int col) {
	coordenadaDestino = new int[2];
	coordenadaDestino[Matriz.LINHA] = linha;
	coordenadaDestino[Matriz.COLUNA] = col;
    }

    public void setCoordenadaDestino(int[] coordenadaDestino) {
	this.coordenadaDestino = coordenadaDestino;
    }

    private int converteIndexGrafo(int linha, int coluna) {
	return this.posicoes.length * linha + coluna;
    }

    private int converterIndexGrafo(int[] coordenada) {
	return converteIndexGrafo(coordenada[Matriz.LINHA], coordenada[Matriz.COLUNA]);
    }
}
