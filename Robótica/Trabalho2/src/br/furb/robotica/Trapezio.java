package br.furb.robotica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Trapezio {

	private static final int X = -1; // Obstáculo
	private static final int R = -2; // Destino - Robo
	private static final int O = 2; // Origem
	private static final int V = 0; // Livre/Vazio

	private List<int[]> pontosMedios;
	private int[][] mapa;
	private List<int[]> caminho;

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

	public static void main(String[] args) throws Exception {
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
		new Trapezio(cenarioA).imprimeCaminho();
		System.out.println("Cenário B");
		new Trapezio(cenarioB).imprimeCaminho();

		int medio = (1 + 6) / 2;
		System.out.println(medio);
	}

	public void imprimePontosMedios() {
		for (int[] ponto : pontosMedios) {
			System.out.println("Col: " + ponto[0] + " Lin: " + ponto[1]);
		}
	}

	public void imprimeCaminho() throws Exception {
		try {
			for (int[] is : this.caminho) {
				System.out.print("C:" + is[0] + "L:" + is[1] + " ");
			}
		} catch (Exception ex) {
			// System.out.println("Erro ao montar o caminho\r\n" +
			// ex.getMessage());
			throw ex;
		}
	}

	private List<int[]> montaCaminho() throws Exception {
		ArrayList<int[]> caminho = new ArrayList<>();
		int incrementCol = 0;
		int[][] origemDestino = BuscarOrigemEDestino();
		int[] origem = origemDestino[0];
		int[] destino = origemDestino[1];

		System.out.println("Origem " + origem[0] + " " + origem[1]);
		System.out.println("Destino " + destino[0] + " " + destino[1]);

		caminho.add(origem);
		if (origem[0] < destino[0]) {
			// Origem está a esquerda do destino
			incrementCol = 1;
		} else {
			// Origem está a direita do destino
			incrementCol = -1;
		}

		int linhaAtual = origem[1];
		int colunaAtual = origem[0];
		while (true) {
			int colunaDestino = colunaAtual + 1;

			// Procura o ponto médio mais proximo na proxima coluna
			int linhaDestino = Integer.MAX_VALUE;
			int menorDiferenca = Integer.MAX_VALUE;
			for (int[] ponto : pontosMedios) {
				if(ponto[0] == colunaDestino)
				{
					int diferenca = Math.abs(linhaAtual - ponto[1]);
					if (diferenca < menorDiferenca) {
						menorDiferenca = diferenca;
						linhaDestino = ponto[1];
					}
					// Da pra melhorar quando for igual
					// Se for igual pegar o ponto que está mais proximo do destino
				}
			}

			int incremetLin = 0;
			if (linhaAtual > linhaDestino) {
				incremetLin = -1; // sobe
			} else {
				incremetLin = +1; // desce
			}
			// monta o caminho até esse ponto encontrado
			while (true) {
				if (linhaAtual != linhaDestino && ehLivre(mapa[colunaAtual][linhaAtual + incremetLin])) {
					linhaAtual += incremetLin;
				} else if (colunaAtual != colunaDestino && ehLivre(mapa[colunaAtual + incrementCol][linhaAtual])) {
					colunaAtual += incrementCol;
				} else {
					throw new Exception("Não foi possível formar um caminho de col:" + colunaAtual + " lin:" + linhaAtual + " até col:" + colunaDestino
							+ " lin:" + linhaDestino);
				}
				caminho.add(new int[] { colunaAtual, linhaAtual });

				System.out.println(colunaAtual + " " + linhaAtual + " - " + colunaDestino + " " + linhaDestino);
				
				if (linhaAtual == linhaDestino && colunaAtual == colunaDestino) {
					System.out.println("OK :D");
					break;
				}
			}
			
			if(colunaAtual == destino[0])
			{
				//E se tiver algum obstáculo no caminho?
				throw new Exception("Não implementado!\r\nFazer robo descer ou subir para alcançar o objetivo");
			}
			
			if (colunaAtual == destino[0] && linhaAtual == destino[1])
				break;
		}

		return caminho;
	}

	private int[][] BuscarOrigemEDestino() {
		int[][] origemDestino = new int[2][2];
		for (int i = 0; i < mapa.length; i++) {
			for (int j = 0; j < mapa[i].length; j++) {
				if (mapa[i][j] == O)
					origemDestino[1] = new int[] { i, j };// Objetivo
				else if (mapa[i][j] == R)
					origemDestino[0] = new int[] { i, j };// Robo
			}
		}
		return origemDestino;
	}

	public Trapezio(int[][] mapa) throws Exception {
		this.mapa = mapa;
		pontosMedios = new LinkedList<>();
		
		montarPontosMedios();
		this.caminho = montaCaminho();
	}

	private void montarPontosMedios() {
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
