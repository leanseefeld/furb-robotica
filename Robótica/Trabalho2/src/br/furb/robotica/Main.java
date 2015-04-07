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

			int[][] cenarioC = new int[][]
			/*  */{ { V, X, V, V, V, V }, //
					{ V, X, V, X, V, O }, //
					{ V, V, V, X, V, X }, //
					{ V, X, V, X, V, V }, //
					{ V, V, V, X, V, V }, //
					{ V, X, V, X, V, V }, //
					{ R, X, V, X, V, V } };
			cenarioC = inverteMatriz(cenarioC);

			int[][] cenarioD = new int[][]
			/*  */{ { V, X, V, V, V, V }, //
					{ V, V, V, V, X, V }, //
					{ V, X, V, X, V, V }, //
					{ V, X, V, V, V, V }, //
					{ V, V, X, V, V, V }, //
					{ V, X, V, V, V, X }, //
					{ R, X, V, V, V, O } };
			cenarioD = inverteMatriz(cenarioD);

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

			// new Trapezio(cenarioA);
			// new Trapezio(cenarioB);
			// new Trapezio(cenarioC);
			// new Trapezio(cenarioD);
			// new Trapezio(cenarioE);
			// new Trapezio(cenarioF);

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

			ImprimirCenario(cenarioY);
		} catch (Exception ex) {
			System.out.println("Erro:\r\n" + ex.getMessage());

		}
	}

	public static void ImprimirCenario(int[][] mapa) {
		for (int col = 0; col < mapa.length; col++) {
			for (int lin = 0; lin < mapa[col].length; lin++) {
				System.out.print(celulaToString(mapa[col][lin]));
			}
			System.out.println();
		}
	}

	private static String celulaToString(int valorCelula) {
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
			saida = String.valueOf(valorCelula);
		}
		return saida;
	}
}
