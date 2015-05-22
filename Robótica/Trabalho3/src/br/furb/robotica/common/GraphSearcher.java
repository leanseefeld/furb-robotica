package br.furb.robotica.common;

import br.furb.robotica.estruturas.lista.Lista;


public abstract class GraphSearcher {

    public static final int INFINITE = Integer.MAX_VALUE;
    public static final String INFINITE_STR = "I";
    protected int[][] graph;
    protected int[][] vars;

    public GraphSearcher(int[][] graph) {
	if (graph == null) {
	    throw new IllegalArgumentException("informe um grafo");
	}
	this.graph = graph;
    }

    /**
     * Realiza a busca desta classe e disponibiliza as variáveis de resultado
     * para acesso através de métodos auxiliares.
     */
    public abstract void doSearch();

    protected int[] getAdjs(int v) {
	Lista lista = new Lista();
	for (int i = 0; i < graph.length; i++) {
	    if (graph[v][i] != 0 && graph[v][i] != INFINITE) {
		lista.insere(i);
	    }
	}
	int[] adjs = new int[lista.comprimento()];
	for (int i = 0; i < adjs.length; i++) {
	    adjs[i] = lista.buscarIndice(i).getInfo();
	}
	return adjs;
    }

    public int getVar(int v, int var) {
	return vars[v][var];
    }

}
