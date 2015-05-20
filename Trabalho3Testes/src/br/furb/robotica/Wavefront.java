package br.furb.robotica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Wavefront {

    static boolean log = true;

    private MapaLabirinto mapaOriginal;
    private int[][] mapaValorado;
    private Queue<int[]> vizinhosPendentes = new LinkedList<int[]>();

    private final int[] coordenadaInicial;
    private final int[] coordenadaFinal;

    public Wavefront(MapaLabirinto mapa, int[] coordenadaInicial, int[] coordenadaFinal) {
	this.mapaOriginal = mapa;
	this.coordenadaInicial = coordenadaInicial;
	this.coordenadaFinal = coordenadaFinal;
	mapaValorado = new int[mapa.getPosicoes().length][mapa.getPosicoes()[0].length];
    }

    public Caminho gerarCaminho() {
	return gerarCaminho(coordenadaInicial);
    }

    /**
     * Gera um caminho de {@code origem} até o objetivo definido no mapa.
     * 
     * @param origem
     *            coordenada da origem. Acessada pelos índices {@link #COLUNA} e {@link #LINHA}.
     * @return caminho de {@code origem} até o objetivo definido no mapa.
     */
    public Caminho gerarCaminho(int[] origem) {
	valorarMapa();

	Caminho caminho = new Caminho();

	int[] passo = origem.clone();
	caminho.addPasso(passo);
	do {
	    int[][] vizinhos = acharVizinhosConexos(passo);
	    passo = getMenorVizinhoValorado(vizinhos);
	    caminho.addPasso(passo);
	} while (!isCelulaDestino(passo));

	caminho.imprimeCaminho();
	return caminho;
    }

    private boolean isCelulaDestino(int[] passo) {
	return passo[Matriz.COLUNA] == this.coordenadaFinal[Matriz.COLUNA]
		&& passo[Matriz.LINHA] == this.coordenadaFinal[Matriz.LINHA];
    }

    private int[] getMenorVizinhoValorado(int[][] vizinhos) {
	int[] menor = vizinhos[0];
	int menorValor = valorCelula(menor);
	for (int i = 1; i < vizinhos.length; i++) {
	    int[] vizinho = vizinhos[i];
	    int valor = valorCelula(vizinho);
	    if (menorValor < 2 || valor >= 2 && valor < menorValor) {
		menor = vizinho;
		menorValor = valorCelula(vizinho);
	    }
	}
	return menor;
    }

    /**
     * Retorna todos os vizinhos que não sejam obstáculos.
     * 
     * @param celula
     *            celula cujo os vizinhos se deseja saber.
     * @return vizinhos que não sejam obstáculos.
     */
    private int[][] acharVizinhosConexos(int[] celula) {
	List<int[]> vizinhos = new ArrayList<>(4);

	if (this.mapaOriginal.isIndicesValidos(celula) && this.mapaOriginal.getInfoPosicao(celula) != null) {
	    List<int[]> possiveisVizinhos = criarLista( //
		    new int[] { celula[Matriz.COLUNA] + 1, celula[Matriz.LINHA] }, // 
		    new int[] { celula[Matriz.COLUNA] - 1, celula[Matriz.LINHA] }, //
		    new int[] { celula[Matriz.COLUNA], celula[Matriz.LINHA] + 1 }, //
		    new int[] { celula[Matriz.COLUNA], celula[Matriz.LINHA] - 1 });

	    for (int[] vizinho : possiveisVizinhos) {
		if (this.mapaOriginal.isIndicesValidos(vizinho) && mapaOriginal.existePassagem(celula, vizinho)) {
		    vizinhos.add(vizinho);
		}
	    }
	}
	return vizinhos.toArray(new int[vizinhos.size()][2]);
    }

    private int valorCelula(int... celula) {
	return mapaValorado[celula[Matriz.COLUNA]][celula[Matriz.LINHA]];
    }

    protected void valorarMapa() {
	int[] objetivo = this.coordenadaFinal;
	if (objetivo == null) {
	    throw new IllegalStateException("objetivo não está no mapa");
	}

	escorrerValores();

	if (log) {
	    System.out.println();
	    //Wavefront.imprimirCenario(mapaValorado);
	    System.out.println();
	}
    }

    /**
     * Faz com que os valores do mapa valorado "escorram" pras células ainda não valoradas.
     */
    private void escorrerValores() {
	int[] celula = this.coordenadaInicial;
	for (int[] v : acharVizinhosConexos(celula)) {
	    vizinhosPendentes.add(v);
	}

	while (!vizinhosPendentes.isEmpty()) {
	    int[] vizinho = (int[]) vizinhosPendentes.poll();
	    escorrerValores(vizinho);
	}
    }

    private void escorrerValores(int[] celula) {
	// se não tem vizinhos, ignora
	int[][] vizinhos = acharVizinhosConexos(celula);
	if (vizinhos.length == 0) {
	    return;
	}

	// atribuir o valor do menor vizinho + 1
	int[] menorVizinho = getMenorVizinhoValorado(vizinhos);
	mapaValorado[celula[Matriz.COLUNA]][celula[Matriz.LINHA]] = valorCelula(menorVizinho) + 1;

	// após atribuir o valor, ir para os próximos vizinhos vazios
	for (int[] vizinho : vizinhos) {
	    if (valorCelula(vizinho) == 0) {
		vizinhosPendentes.add(vizinho);
	    }
	}
    }

    public static final List<int[]> criarLista(int[]... cells) {
	List<int[]> ret = new ArrayList<>();
	for (int[] cell : cells) {
	    ret.add(cell);
	}
	return ret;
    }
}
