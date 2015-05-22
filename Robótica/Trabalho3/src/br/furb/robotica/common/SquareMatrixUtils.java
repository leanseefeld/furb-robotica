package br.furb.robotica.common;


/**
 * Classe com métodos utilitários para utilização em matriz quadradas.<br>
 * Caso os métodos presentes nesta classe sejam utilizados em matrizes
 * não-quadradas, podem ocorrer exceções como
 * {@code ArrayIndexOutOfBoundsException} ou resultar em comportamento
 * indeterminado.
 * 
 * @author Gielez Feldhaus Goulart
 * @author William Leander Seefeld
 * 
 */
public final class SquareMatrixUtils {

    private SquareMatrixUtils() {
	// You're not allowed IN the matrix
    }

    public static int[][] getTransposta(int[][] matriz) {
	if (null == matriz) {
	    return null;
	}
	int dim = matriz.length;
	int[][] t = new int[dim][dim];
	for (int i = 0; i < dim; i++) {
	    for (int j = 0; j < dim; j++) {
		t[i][j] = matriz[j][i];
	    }
	}
	return t;
    }

    public static void ordenar(int[][] matriz, Comparator<int[]> c) {
	bubbleSort(matriz, c);
    }

    public static void ordenar(Integer[] adjs, Comparator<Integer> c) {
	bubbleSort(adjs, c);
    }

    private static <T> void bubbleSort(T[] v, Comparator<T> c) {
	int fim = v.length - 1;
	int j;
	boolean trocou;
	do {
	    trocou = false;
	    for (j = 0; j < fim; j++) {
		if (c.compare(v[j + 1], v[j]) < 0) {
		    T temp = v[j];
		    v[j] = v[j + 1];
		    v[j + 1] = temp;
		    trocou = true;
		}
	    }
	    fim--;
	} while (trocou);
    }

    public static Integer[] intToInteger(int[] q) {
	Integer[] values = new Integer[q.length];
	for (int i = 0; i < q.length; i++) {
	    values[i] = q[i];
	}
	return values;
    }

}
