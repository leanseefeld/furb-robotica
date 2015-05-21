package br.furb.robotica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Wavefront {

    static boolean log = true;

    private MapaLabirinto mapaOriginal;
    private int[][] mapaValorado;
    private Queue<int[]> vizinhosPendentes = new Queue<int[]>();

    private final int[] coordenadaAtual;
    private final int[] coordenadaObjetivo;

    public Wavefront(MapaLabirinto mapa, int[] coordenadaAtual, int[] coordenadaObjetivo) {
	this.mapaOriginal = mapa;
	this.coordenadaAtual = coordenadaAtual;
	this.coordenadaObjetivo = coordenadaObjetivo;
	mapaValorado = new int[mapa.getPosicoes().length][mapa.getPosicoes()[0].length];
    }

    public Caminho gerarCaminho() {
	return gerarCaminho(coordenadaAtual);
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

	this.imprimirCenario(this.mapaValorado);

	Caminho caminho = new Caminho();
	boolean fim = false;
	int[] passo = origem.clone();
	caminho.addPasso(passo);
	print(passo);
	do {
	    int[] objetivo = getProcuraObjetivoVisinho(passo);
	    if (objetivo != null) {
		fim = true;
		caminho.addPasso(objetivo);
		print(objetivo);
	    } else {
		int[][] vizinhos = acharVizinhosExplorados(passo, true);
		passo = getMenorVizinhoValorado(vizinhos);
		caminho.addPasso(passo);
		print(passo);
	    }
	} while (!fim);

	caminho.imprimeCaminho();
	return caminho;
    }

    public void print(int[] objetivo) {
	System.out.println(objetivo[Matriz.COLUNA] + "  " + objetivo[Matriz.LINHA]);
    }

    /**
     * Procura se algum dos visinhos é o objetivo e se encontrar retorna o mesmo
     * 
     * @param celula
     * @return Coordenadad objetivo
     */
    private int[] getProcuraObjetivoVisinho(int[] celula) {
	for (int[] is : getTodosVizinhos(celula)) {
	    if (this.mapaOriginal.isIndicesValidos(is)) {
		if (this.mapaOriginal.comparaCooredenadas(is, this.coordenadaObjetivo)) {
		    if (this.mapaOriginal.existePassagem(celula, is)) {
			return is;
		    }
		}
	    }
	}
	return null;
    }

    private int[] getMenorVizinhoValorado(int[][] vizinhos) {
	int[] menor = vizinhos[0];
	int menorValor = valorCelula(menor);
	for (int i = 1; i < vizinhos.length; i++) {
	    int[] vizinho = vizinhos[i];
	    int valor = valorCelula(vizinho);
	    if (menorValor < 1 || valor >= 1 && valor < menorValor) {
		menor = vizinho;
		menorValor = valorCelula(vizinho);
	    }
	}
	return menor;
    }

    /**
     * Retorna todos os vizinhos que esta célula tenha ligação. <br>
     * 
     * @param celula
     *            celula cujo os vizinhos se deseja saber.
     * @param apenasConexos
     *            Se true, busca apenas as células conexas(que possuem passagem entre si) <br>
     *            <span style="color:red">Não funciona se esta celula não foi explorada</span>
     * @return vizinhos conexos
     */
    private int[][] acharVizinhosExplorados(int[] celula, boolean apenasConexos) {
	List<int[]> vizinhos = new ArrayList<>(4);

	if (!this.mapaOriginal.isIndicesValidos(celula)
		|| (apenasConexos && this.mapaOriginal.getInfoPosicao(celula) == null)) {
	    return new int[0][2];
	}

	List<int[]> possiveisVizinhos = getTodosVizinhos(celula);

	for (int[] vizinho : possiveisVizinhos) {
	    if (this.mapaOriginal.isIndicesValidos(vizinho)) {
		if (mapaOriginal.getInfoPosicao(vizinho) != null) {
		    if (!apenasConexos || mapaOriginal.existePassagem(celula, vizinho)) {
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

    private int valorCelula(int... celula) {
	return mapaValorado[celula[Matriz.LINHA]][celula[Matriz.COLUNA]];
    }

    protected void valorarMapa() {
	int[] objetivo = this.coordenadaObjetivo;
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
	int[] celulaObjetivo = this.coordenadaObjetivo;
	System.out.println("C: " + celulaObjetivo[Matriz.LINHA] + "  L:" + celulaObjetivo[Matriz.COLUNA]
		+ " - Objetivo");
	this.mapaValorado[celulaObjetivo[Matriz.LINHA]][celulaObjetivo[Matriz.COLUNA]] = 1;
	int[][] visinhosExplorados = acharVizinhosExplorados(celulaObjetivo, false);
	for (int[] v : visinhosExplorados) {
	    if (this.mapaOriginal.existePassagem(v, celulaObjetivo)) {
		this.mapaValorado[v[Matriz.LINHA]][v[Matriz.COLUNA]] = 2;
		vizinhosPendentes.push(v);
	    }
	}

	while (!vizinhosPendentes.isEmpty()) {
	    int[] vizinho = (int[]) vizinhosPendentes.pop();
	    this.imprimirCenario(this.mapaValorado);
	    System.out.println("C: " + vizinho[Matriz.LINHA] + "  L:" + vizinho[Matriz.COLUNA]);
	    escorrerValores(vizinho);
	}
	this.imprimirCenario(this.mapaValorado);
    }

    private void escorrerValores(int[] celula) {
	// se não tem vizinhos, ignora
	int[][] vizinhos = acharVizinhosExplorados(celula, true);
	if (vizinhos.length == 0) {
	    return;
	}

	// atribuir o valor do menor vizinho + 1
	int[] menorVizinho = getMenorVizinhoValorado(vizinhos);
	mapaValorado[celula[Matriz.LINHA]][celula[Matriz.COLUNA]] = valorCelula(menorVizinho) + 1;

	// após atribuir o valor, ir para os próximos vizinhos vazios
	for (int[] vizinho : vizinhos) {
	    if (valorCelula(vizinho) == 0) {
		vizinhosPendentes.push(vizinho);
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

    public void imprimirCenario(int[][] mapa) {
	for (int lin = 0; lin < mapa.length; lin++) {
	    for (int col = 0; col < mapa[lin].length; col++) {
		System.out.print(celulaToString(mapa[lin][col],
			this.mapaOriginal.getInfoPosicao(new int[] { lin, col })));
	    }
	    System.out.println();
	}
    }

    private String celulaToString(int valor, InfoPosicao posicao) {
	String saida = "  -  ";
	if (posicao == null) {
	    saida = "  ?" + valor + " ";
	} else {
	    saida = "  " + valor + "  ";
	}
	return saida;
    }
}
