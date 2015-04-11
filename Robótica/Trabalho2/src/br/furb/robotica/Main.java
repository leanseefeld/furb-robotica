package br.furb.robotica;

public class Main {

	public static final int X = -1; // Obstáculo
	public static final int R = -2; // Origem - Robo
	public static final int O = 2; // Objetivo
	public static final int V = 0; // Livre/Vazio

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
		try {

			// new Trapezio(getCenarioA());
			// new Trapezio(cenarioB);
			// new Trapezio(cenarioC);
			// new Trapezio(cenarioD);
			// new Trapezio(cenarioE);
			// new Trapezio(cenarioF);

			imprimirCenario(getCenarioY());
		} catch (Exception ex) {
			System.out.println("Erro:\r\n" + ex.getMessage());
		}
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

	public static int[][] getCenarioE() {
		int[][] cenarioE = new int[][]
		/*  */{ { V, X, V, V, V, V, V, V }, //
				{ V, V, V, V, X, V, V, V }, //
				{ V, X, V, X, V, V, V, V }, //
				{ V, X, V, V, V, V, V, V }, //
				{ V, V, X, V, V, V, V, V }, //
				{ V, X, V, V, V, X, V, V }, //
				{ V, X, V, V, V, X, X, V }, //
				{ R, X, V, V, V, V, V, O } };
		cenarioE = inverteMatriz(cenarioE);
		return cenarioE;
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

	public static int[][] getCenarioC() {
		int[][] cenarioC = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, X, V, X, V, O }, //
				{ V, V, V, X, V, X }, //
				{ V, X, V, X, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, X, V, X, V, V }, //
				{ R, X, V, X, V, V } };
		cenarioC = inverteMatriz(cenarioC);
		return cenarioC;
	}

	public static int[][] getCenarioB() {
		int[][] cenarioB = new int[][]
		/*  */{ { V, V, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ R, X, V, V, X, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, V, V, X, V, O } };
		cenarioB = inverteMatriz(cenarioB);
		return cenarioB;
	}

	public static int[][] getCenarioA() {
		int[][] cenarioA = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, V, V, V, X, O }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ X, V, X, V, V, V }, //
				{ R, V, V, X, V, X } };
		cenarioA = inverteMatriz(cenarioA);
		return cenarioA;
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
