package br.furb.robotica;

import java.util.LinkedList;
import java.util.List;

public class Trapezio {

	private static final int X = -1; // Obstáculo
	private static final int R = -2; // Destino
	private static final int O = 2; // Origem
	private static final int V = 0; // Livre/Vazio

	// Inverte as colunas por linhas
	private static int[][] InverteMatriz(int[][] matriz) {
		int[][] novaMatriz = new int[matriz[0].length][matriz.length];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				novaMatriz[j][i] = matriz[i][j];
			}
		}
		return novaMatriz;
	}

	public static void main(String[] args) {
		int[][] cenarioA = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, V, V, V, X, O }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ X, V, X, V, V, V }, //
				{ R, V, V, X, V, X } };
		cenarioA = InverteMatriz(cenarioA);
		int[][] cenarioB = new int[][]
		/*  */{ { V, V, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ R, X, V, V, X, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, V, V, X, V, O } };
		cenarioB = InverteMatriz(cenarioB);

		Trapezio(cenarioA);
		System.out.println("Cenário A");
		ImprimePontosMedios();
		Trapezio(cenarioB);
		System.out.println("Cenário B");
		ImprimePontosMedios();

		int medio = (1 + 6) / 2;
		System.out.println(medio);
	}

	private static void ImprimePontosMedios() {
		for (int[] ponto : pontosMedios) {
			System.out.println("Col: " + ponto[0] + " Lin: " + ponto[1]);
		}
	}

	private static List<int[]> pontosMedios = new LinkedList<>();

	public static void Trapezio(int[][] mapa) {
		pontosMedios = new LinkedList<>();
		for (int c = 0; c < mapa.length; c++) {
			int primeiraLinLivre = -1;
			for (int l = 0; l < mapa[c].length; l++) {
				if (ehLivre(mapa[c][l])) {
					if (primeiraLinLivre == -1) {
						primeiraLinLivre = l;
					}
				}

				// Se existe linha livre encontrada E
				// Essa é a ultima linha do grid OU a proxima linha do grid não é livro
				if ((primeiraLinLivre != -1 && (l == mapa[c].length - 1 || !ehLivre(mapa[c][l + 1])))) {
					int medio = (l + primeiraLinLivre) / 2; // trunca pra baixo
					pontosMedios.add(new int[] { c, medio });
					primeiraLinLivre = -1;
				}
			}
		}
	}

	private static boolean ehLivre(int celula) {
		return X != celula;// && R != celula;
	}

}
