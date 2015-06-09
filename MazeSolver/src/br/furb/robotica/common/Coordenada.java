package br.furb.robotica.common;

import br.furb.robotica.Matriz;

public class Coordenada {

	public static boolean comparar(int[] a, int linha, int coluna) {
		return equals(a, criar(linha, coluna));
	}

	public static final int[] criar(int linha, int coluna) {
		int[] coordenada = new int[2];
		coordenada[Matriz.LINHA] = linha;
		coordenada[Matriz.COLUNA] = coluna;
		return coordenada;
	}

	public static boolean equals(int[] a, int[] b) {
		return a[Matriz.LINHA] == b[Matriz.LINHA]
				&& a[Matriz.COLUNA] == b[Matriz.COLUNA];
	}

	public static String toString(int[] coordenadaAtual) {
		return "C:" + coordenadaAtual[Matriz.COLUNA] + " L:"
				+ coordenadaAtual[Matriz.LINHA];
	}

	private Coordenada() {
	}
}
