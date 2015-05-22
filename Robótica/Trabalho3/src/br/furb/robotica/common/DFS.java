package br.furb.robotica.common;


/**
 * Realiza a busca em profundidade em um grafo G a partir de sua matriz de
 * adjacência.
 * 
 * @author Gielez Feldhaus Goulart
 * @author William Leander Seefeld
 * 
 */
public class DFS extends GraphSearcher {

    private static final int I_DES = 0;
    private static final int I_FIN = 1;
    private static final int I_COR = 2;
    private static final int COR_BRANCO = 0;
    private static final int COR_CINZA = 1;
    private static final int COR_PRETO = 2;

    private int tempo;

    public DFS(int[][] graph) {
	super(graph);
    }

    @Override
    public void doSearch() {
	vars = new int[graph.length][3];
	int[] toExplore = getVerticesToExplore();
	for (int i = 0; i < toExplore.length; i++) {
	    int v = toExplore[i];
	    if (vars[v][I_COR] == COR_BRANCO) {
		dfsVisit(v);
	    }
	}
    }

    private void dfsVisit(int v) {
	vars[v][I_COR] = COR_CINZA;
	vars[v][I_DES] = ++tempo;
	int[] adjs = getAdjs(v);
	for (int u : adjs) {
	    if (vars[u][I_COR] == COR_BRANCO) {
		dfsVisit(u);
	    }
	}
	vars[v][I_COR] = COR_PRETO;
	vars[v][I_FIN] = ++tempo;
    }

    protected int[] getVerticesToExplore() {
	int[] toExplore = new int[graph.length];
	for (int i = 0; i < graph.length; i++) {
	    toExplore[i] = i;
	}
	return toExplore;
    }

    /**
     * Verifica se o grafo é transposto aplicando uma busca em profundidade na
     * matriz transposta do grafo.
     * 
     * @return {@code true} se o grafo é completamente conexo, {@code false} do
     *         contrário.
     */
    public boolean isConexo() {
	assert vars != null : "doSearch() deve ser executado primeiro";
	DFSInverseSearcher insS = new DFSInverseSearcher(
		SquareMatrixUtils.getTransposta(graph), vars);
	insS.doSearch();
	boolean closed = false;
	int[][] tVars = insS.vars;
	for (int v = 0; v < tVars.length; v++) {
	    if (tVars[v][I_DES] == tVars[v][I_FIN] - 1) {
		if (closed) {
		    return false;
		}
		closed = true;
	    }
	}
	return true;
    }

    /**
     * Ordena a exploração decrescente dos vértices de acordo com o tempo de
     * finalização dos vértices em uma busca anterior.
     * 
     */
    private static class DFSInverseSearcher extends DFS {

	private int[][] superVars;

	public DFSInverseSearcher(int[][] graph, int[][] superVars) {
	    super(graph);
	    this.superVars = superVars;
	}

	@Override
	protected int[] getVerticesToExplore() {
	    int[][] finalization = new int[graph.length][2];
	    for (int i = 0; i < graph.length; i++) {
		finalization[i][0] = i;
		finalization[i][1] = superVars[i][I_FIN];
	    }
	    SquareMatrixUtils.ordenar(finalization, new Comparator<int[]>() {

		@Override
		public int compare(int[] o1, int[] o2) {
		    return o2[1] - o1[1];
		}
	    });
	    int[] toExplore = new int[graph.length];
	    for (int i = 0; i < graph.length; i++) {
		toExplore[i] = finalization[i][0];
	    }
	    return toExplore;
	}

	@Override
	protected int[] getAdjs(int v) {
	    int[] superAdjs = super.getAdjs(v);
	    Integer[] adjs = new Integer[superAdjs.length];
	    for (int i = 0; i < adjs.length; i++) {
		adjs[i] = superAdjs[i];
	    }
	    SquareMatrixUtils.ordenar(adjs, new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
		    return DFSInverseSearcher.this.superVars[o2][I_FIN]
			    - DFSInverseSearcher.this.superVars[o1][I_FIN];
		}
	    });
	    for (int i = 0; i < adjs.length; i++) {
		superAdjs[i] = adjs[i];
	    }
	    return superAdjs;
	}
    }

}
