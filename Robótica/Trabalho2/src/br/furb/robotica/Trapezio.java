package br.furb.robotica;

import java.util.LinkedList;
import java.util.List;

public class Trapezio {

	private static final int X = -1; // Obstáculo
	private static final int R = -2; // Destino
	private static final int O = 2; // Origem
	private static final int V = 0; // Livre/Vazio

	// Inverte as colunas por linhas
	private static int[][] inverteMatriz(int[][] matriz) {
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
		cenarioA = inverteMatriz(cenarioA);
		int[][] cenarioB = new int[][]
		/*  */{ { V, V, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ R, X, V, V, X, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, V, V, X, V, O } };
		cenarioB = inverteMatriz(cenarioB);

		System.out.println("Cenário A");
		new Trapezio(cenarioA).imprimePontosMedios();
		System.out.println("Cenário B");
		new Trapezio(cenarioB).imprimePontosMedios();

		int medio = (1 + 6) / 2;
		System.out.println(medio);
	}

	public void imprimePontosMedios() {
		for (int[] ponto : pontosMedios) {
			System.out.println("Col: " + ponto[0] + " Lin: " + ponto[1]);
		}
	}

	private List<int[]> pontosMedios = new LinkedList<>();

	public Trapezio(int[][] mapa) {
		pontosMedios = new LinkedList<>();
		for (int c = 0; c < mapa.length; c++) {
			int primeiraLinLivre = -1;
			int ultimaLinLivre = -1;
			for (int l = 0; l < mapa[c].length; l++) {
				if (ehLivre(mapa[c][l])) {
					if (primeiraLinLivre == -1) {
						primeiraLinLivre = l;
					}
					ultimaLinLivre = l;
				}

				// Se existe linha livre encontrada E
				// a linha atual não é livre OU essa é a ultima linha do grid
				if ((ultimaLinLivre != -1 && (ultimaLinLivre != l || l == mapa[c].length - 1))) {
					int medio = (ultimaLinLivre + primeiraLinLivre) / 2; // trunca
																			// pra
																			// baixo
					pontosMedios.add(new int[] { c, medio });
					primeiraLinLivre = -1;
					ultimaLinLivre = -1;
				}
			}
		}
	}

	private static boolean ehLivre(int celula) {
		return X != celula;// && R != celula;
	}

}
