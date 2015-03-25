package br.furb.robotica;

import java.util.LinkedList;
import java.util.List;

public class Trapezio {

	private static final int X = -1;
	private static final int R = -2;
	private static final int O = 2;
	private static final int V = 0;

	public static void main(String[] args) {
		int[][] cenarioA = new int[][]
		/* **/{ { V, X, V, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, V, V, V, X, O }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ X, V, X, V, V, V }, //
				{ R, V, V, X, V, X } };
		int[][] cenarioB = new int[][]
		/* **/{ { V, V, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ R, X, V, V, X, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, V, V, X, V, O } };

	}

	private List<int[]> pontosMedios = new LinkedList<>();

	public Trapezio(int[][] mapa) {
		for (int c = 0; c < mapa.length; c++) {
			int ultimaLinLivre = -1;
			for (int l = 0; l < mapa[c].length; l++) {
				if (ehLivre(mapa[c][l])) {
					if (ultimaLinLivre == -1) {
						ultimaLinLivre = l;
					}
				} else if (ultimaLinLivre != -1 || l == mapa[c].length - 1) {
					int medio = (l + ultimaLinLivre) / 2; // trunca pra baixo
					pontosMedios.add(new int[] { c, medio });
					ultimaLinLivre = -1;
				}
			}
		}
	}

	private static boolean ehLivre(int celula) {
		return X != celula && R != celula;
	}

}
