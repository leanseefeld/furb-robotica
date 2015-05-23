package br.furb.robotica;

import br.furb.robotica.estruturas.MinhaPilha;

public class Wavefront {

    static boolean log = true;

    private MapaLabirinto mapaOriginal;
    private int[][] mapaValorado;
    private MinhaPilha<int[]> vizinhosPendentes;

    private final int[] coordenadaAtual;
    private final int[] coordenadaObjetivo;

    /**
     * @param mapa
     * @param coordenadaAtual
     * @param coordenadaObjetivo
     * @param tipoDePila
     *            Criada para poder ser utilizada para teste em um projeto sem a biblioteca do Lejos
     */
    public Wavefront(MapaLabirinto mapa, int[] coordenadaAtual, int[] coordenadaObjetivo, MinhaPilha<int[]> tipoDePila) {
	vizinhosPendentes = tipoDePila;
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
		int[][] vizinhos = this.mapaOriginal.getVizinhos(passo, true, true);
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
	for (int[] is : this.mapaOriginal.getTodosVizinhos(celula)) {
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
	int[][] visinhosExplorados = this.mapaOriginal.getVizinhos(celulaObjetivo, false, true);
	for (int[] v : visinhosExplorados) {
	    if (this.mapaOriginal.existePassagem(v, celulaObjetivo)) {
		this.mapaValorado[v[Matriz.LINHA]][v[Matriz.COLUNA]] = 2;
		vizinhosPendentes.empilhar(v);
	    }
	}

	while (!vizinhosPendentes.estaVazia()) {
	    int[] vizinho = (int[]) vizinhosPendentes.pegar();
	    this.imprimirCenario(this.mapaValorado);
	    System.out.println("C: " + vizinho[Matriz.LINHA] + "  L:" + vizinho[Matriz.COLUNA]);
	    escorrerValores(vizinho);
	}
	this.imprimirCenario(this.mapaValorado);
    }

    private void escorrerValores(int[] celula) {
	// se não tem vizinhos, ignora
	int[][] vizinhos = this.mapaOriginal.getVizinhos(celula, true, true);
	if (vizinhos.length == 0) {
	    return;
	}

	// atribuir o valor do menor vizinho + 1
	int[] menorVizinho = getMenorVizinhoValorado(vizinhos);
	mapaValorado[celula[Matriz.LINHA]][celula[Matriz.COLUNA]] = valorCelula(menorVizinho) + 1;

	// após atribuir o valor, ir para os próximos vizinhos vazios
	for (int[] vizinho : vizinhos) {
	    if (valorCelula(vizinho) == 0) {
		vizinhosPendentes.empilhar(vizinho);
	    }
	}
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
