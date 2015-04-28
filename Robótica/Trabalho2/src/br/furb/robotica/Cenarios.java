package br.furb.robotica;

import static br.furb.robotica.Constantes.O;
import static br.furb.robotica.Constantes.R;
import static br.furb.robotica.Constantes.V;
import static br.furb.robotica.Constantes.X;

public class Cenarios {

	// Inverte as colunas por linhas
	public static int[][] inverteMatriz(int[][] matriz) {
		int[][] novaMatriz = new int[matriz[0].length][matriz.length];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				novaMatriz[j][i] = matriz[i][j];
			}
		}
		return novaMatriz;
	}

	public static int[][] getCenarioA() {
		int[][] mapa = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, V, V, V, X, O }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ X, V, X, V, V, V }, //
				{ R, V, V, X, V, X } };
		mapa = inverteMatriz(mapa);
		return mapa;
	}

	public static int[][] getCenarioB() {
		int[][] mapa = new int[][]
		/*  */{ { V, V, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ R, X, V, V, X, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, V, V, X, V, O } };
		mapa = inverteMatriz(mapa);
		return mapa;
	}

	public static int[][] getCenarioC() {
		int[][] mapa = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, X, V, X, V, O }, //
				{ V, V, V, X, V, X }, //
				{ V, X, V, X, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, X, V, X, V, V }, //
				{ R, X, V, X, V, V } };
		mapa = inverteMatriz(mapa);
		return mapa;
	}

	public static int[][] getCenarioD() {
		int[][] cenarioD = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, X, V, X, V, V }, //
				{ V, X, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, X, V, V, V, X }, //
				{ R, X, V, V, V, O } };
		cenarioD = inverteMatriz(cenarioD);
		return cenarioD;
	}

	public static int[][] getCenarioE() {
		int[][] mapa = new int[][]
		/*  */{ { V, V, V, V, V, O }, //
				{ V, X, V, X, V, V }, //
				{ V, X, V, X, V, V }, //
				{ V, X, V, X, V, V }, //
				{ V, X, V, X, V, V }, //
				{ V, X, V, X, V, V }, //
				{ R, X, V, X, V, V } };
		mapa = inverteMatriz(mapa);
		return mapa;
	}

	public static int[][] getCenarioF() {
		int[][] cenarioF = new int[][]
		/*  */{ { V, V, V, V, V, V, V, V }, //
				{ V, X, V, V, V, V, V, V }, //
				{ V, V, X, V, X, V, V, V }, //
				{ V, X, V, X, R, X, V, V }, //
				{ V, X, V, V, V, V, X, V }, //
				{ V, V, X, V, V, X, V, V }, //
				{ V, X, V, X, V, X, V, V }, //
				{ V, V, V, V, V, X, V, O } };
		cenarioF = inverteMatriz(cenarioF);
		return cenarioF;
	}

	public static int[][] getCenarioG() {
		int[][] mapa = new int[][]
		/*  */{ { V, X, V, V, V, V, V, V }, //
				{ V, V, V, V, X, V, V, V }, //
				{ V, X, V, X, V, V, V, V }, //
				{ V, X, V, V, V, V, V, V }, //
				{ V, V, X, V, V, V, V, V }, //
				{ V, X, V, V, V, X, V, V }, //
				{ V, X, V, V, V, X, X, V }, //
				{ R, X, V, V, V, V, V, O } };
		mapa = inverteMatriz(mapa);
		return mapa;
	}

	public static int[][] getCenarioY() {
		int[][] cenarioY = new int[][]
		/*  */{ { V, V, V, V, V, V, V, V }, //
				{ 4, X, V, V, V, V, V, V }, //
				{ V, V, X, V, X, V, V, V }, //
				{ V, X, V, X, R, X, V, V }, //
				{ V, X, V, V, V, V, 50, V }, //
				{ V, V, X, V, 10, X, V, V }, //
				{ V, X, V, X, V, X, V, V }, //
				{ V, V, V, V, V, X, V, O } };
		cenarioY = inverteMatriz(cenarioY);
		return cenarioY;
	}

	public static void imprimirCenario(int[][] mapa) {
		for (int col = 0; col < mapa.length; col++) {
			for (int lin = 0; lin < mapa[col].length; lin++) {
				System.out.print(celulaToString(mapa[col][lin]));
			}
			System.out.println();
		}
	}

	public static String celulaToString(int valorCelula) {
		String saida = " - ";
		switch (valorCelula) {
		case X:
			saida = " X ";
			break;
		case V:
			saida = " V ";
			break;
		case R:
			saida = " R ";
			break;
		case O:
			saida = " O ";
			break;
		default:
			saida = " " + valorCelula + " ";
		}
		return saida;
	}
}
