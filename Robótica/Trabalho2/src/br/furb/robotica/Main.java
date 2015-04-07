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
			
			/*System.out.println("Cenário A");
			new Trapezio(cenarioA);
			System.out.println("\r\n\r\n");
			System.out.println("Cenário B");
			new Trapezio(cenarioB);
			System.out.println("\r\n\r\n");
			System.out.println("Cenário C");
			new Trapezio(cenarioC);
			System.out.println("\r\n\r\n");
			System.out.println("Cenário D");*/
			new Trapezio(cenarioD);
		} catch (Exception ex) {
			System.out.println("Erro:\r\n" + ex.getMessage());

		}
	}

}
