package util;

import java.util.LinkedList;
import java.util.List;

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

	public int getVar(int v, int var) {
		return vars[v][var];
	}

	protected int[] getAdjs(int v) {
		List<Integer> lista = new LinkedList<>();
		for (int i = 0; i < graph.length; i++) {
			if (graph[v][i] != 0 && graph[v][i] != INFINITE) {
				lista.add(i);
			}
		}
		int[] adjs = new int[lista.size()];
		for (int i = 0; i < adjs.length; i++) {
			adjs[i] = lista.get(i);
		}
		return adjs;
	}

}
