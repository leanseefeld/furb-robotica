package br.furb.robotica;

import br.furb.robotica.common.GraphSearcher;
import br.furb.robotica.estruturas.lista.Lista;

public class Dijkstra extends GraphSearcher {

    private static final int V_D = 0;
    private static final int V_PARENT = 1;
    private static final int NIL = -1;
    private static final String PATH_TO = " -> ";

    private final int origin;
    private final int destiny;

    public Dijkstra(int[][] graph, int origin, int destiny) {
	super(graph);
	this.origin = origin;
	this.destiny = destiny;
    }

    @Override
    public void doSearch() {
	// ----- INITIALIZE-SINGLE-SOURCE
	int vCount = graph.length;
	Integer[] q = new Integer[vCount];
	vars = new int[vCount][3];
	for (int v = 0; v < vCount; v++) {
	    vars[v][V_D] = GraphSearcher.INFINITE;
	    vars[v][V_PARENT] = NIL;
	    q[v] = v;
	}
	vars[origin][V_D] = 0;
	// ----- DIJKSTRA
	while (hasNextVertex(q)) {
	    int u = extractMin(q);
	    int[] adjs = getAdjs(u);
	    for (int v : adjs) {
		relax(u, v);
	    }
	}
    }

    private void relax(int u, int v) {
	if (vars[u][V_D] != INFINITE) {
	    int duPlusW = vars[u][V_D] + getWeight(u, v);
	    if (vars[v][V_D] > duPlusW) {
		vars[v][V_D] = duPlusW;
		vars[v][V_PARENT] = u;
	    }
	}
    }

    private int getWeight(int u, int v) {
	return graph[u][v];
    }

    private int extractMin(Integer[] q) {
	int minIndex = -1;
	for (int v = 0; v < q.length; v++) {
	    if (q[v] != INFINITE // eh um vertice nao-explorado
		    && (minIndex < 0 || vars[minIndex][V_D] > vars[q[v]][V_D])) {
		minIndex = v;
	    }
	}
	if (minIndex > -1) {
	    q[minIndex] = INFINITE;
	}
	return minIndex;
    }

    private boolean hasNextVertex(Integer[] q) {
	for (int v = 0; v < q.length; v++) {
	    if (q[v] != INFINITE) {
		return true;
	    }
	}
	return false;
    }

    public int[] getMinPathRoute() {
	Lista route = new Lista();
	int parent = destiny;
	while (parent != NIL) {
	    route.insere(parent);
	    parent = vars[parent][V_PARENT];
	}
	int[] intRoute = new int[route.comprimento()];
	for (int i = 0; i < intRoute.length; i++) {
	    intRoute[i] = route.buscarIndice(i).getInfo();
	}
	return intRoute;
    }

    public String minPathToString() {
	StringBuilder sb = new StringBuilder();
	int[] minPath = getMinPathRoute();
	sb.append(vertexToToken(minPath[0]));
	for (int i = 1; i < minPath.length; i++) {
	    sb.append(PATH_TO).append(vertexToToken(minPath[i]));
	}
	return sb.toString();
    }

    public int minPathWeight() {
	int[] minPath = getMinPathRoute();
	int w = 0;
	for (int i = 0; i < minPath.length - 1; i++) {
	    w += getWeight(minPath[i], minPath[i + 1]);
	}
	return w;
    }

    private static String vertexToToken(int v) {
	return String.valueOf((char) (v + 'A'));
    }
}
