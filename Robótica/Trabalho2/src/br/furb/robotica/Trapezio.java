package br.furb.robotica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Trapezio {

	private static final int X = -1; // Obstáculo
	private static final int R = -2; // Origem - Robo
	private static final int O = 2; // Objetivo
	private static final int V = 0; // Livre/Vazio

	private List<int[]> pontosMedios;
	private int[][] mapa;
	private final int[] inicio;
	private final int[] fim;

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
		Trapezio trap = new Trapezio(cenarioA);
		System.out.println("\r\n\r\n");
		System.out.println("Cenário B");
		trap = new Trapezio(cenarioB);
	}

	public void imprimePontosMedios() {
		for (int[] ponto : pontosMedios) {
			System.out.println("Col: " + ponto[0] + " Lin: " + ponto[1]);
		}
	}

	// Traz os pontos médios da coluna
	private List<int[]> pontosMediosDaColuna(int linhaAtual, int colunaDestino) {
		ArrayList<int[]> linhasDestinos = new ArrayList<>();
		for (int[] ponto : pontosMedios) {
			if (ponto[0] == colunaDestino) {
				linhasDestinos.add(ponto);
			}
		}
		// TODO: Ordernar pela proximidade com a linha atual
		return linhasDestinos;
	}

	private Caminho montaCaminhoRecursivo(int[] posicaoAtual, int[] destino, Caminho caminho) throws Exception {
		int linhaAtual = posicaoAtual[1];
		int colunaAtual = posicaoAtual[0];
		int incrementCol = movimentoMatrisColuna(colunaAtual, destino[0]);
		int incremetLin = movimentoMatrisLinha(linhaAtual, destino[1]);

		// monta o caminho até o ponto de destino
		while (true) {

			caminho.addPasso(colunaAtual, linhaAtual);
			
			if (linhaAtual == destino[1] && colunaAtual == destino[0]) {
				System.out.println("Chegou a coluna e linha esperada");
				break;
			}

			if (linhaAtual != destino[1] && ehLivre(mapa[colunaAtual][linhaAtual + incremetLin])) {
				linhaAtual += incremetLin;
			} else if (colunaAtual != destino[0] && ehLivre(mapa[colunaAtual + incrementCol][linhaAtual])) {
				colunaAtual += incrementCol;
			} else {
				System.out.println("Caminho inválido");
				return null; // Sem caminho livre
			}

		}

		if (colunaAtual == this.fim[0] && linhaAtual != this.fim[1]) {
			incremetLin = this.movimentoMatrisLinha(linhaAtual, this.fim[1]);
			while (linhaAtual != this.fim[1]) {
				linhaAtual += incremetLin;
				caminho.addPasso(colunaAtual, linhaAtual);
			}
		}

		// Verifica se chegou ao destino
		if (colunaAtual == this.fim[0] && linhaAtual == this.fim[1]) {
			System.out.println("Solução encontrada!");
			return caminho;
		}

		List<int[]> pontosDestinos = pontosMediosDaColuna(linhaAtual, colunaAtual + incrementCol);
		for (int[] pontoDestino : pontosDestinos) {
			Caminho caminhoEncontrado = montaCaminhoRecursivo(new int[] { colunaAtual, linhaAtual }, pontoDestino, caminho.clone());
			if (caminhoEncontrado != null)
				return caminhoEncontrado;
		}

		return null; // nenhum caminho livre por aqui
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

		int[][] origemDestino = BuscarOrigemEDestino();
		this.inicio = origemDestino[0];
		this.fim = origemDestino[1];

		pontosMedios = new LinkedList<>();

		montarPontosMedios();
		montaCaminhoRecursivo(this.inicio, this.inicio, new Caminho());
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

	private int movimentoMatrisLinha(int linhaOrigem, int linhaDestino) {
		if (linhaOrigem > linhaDestino) {
			return -1;
		} else {
			return +1;
		}
	}

	private int movimentoMatrisColuna(int colunaOrigem, int colunaDestino) {
		if (colunaOrigem > colunaDestino) {
			return -1;
		} else {
			return +1;
		}
	}
	/*
	 * private Caminho montaCaminho() throws Exception { Caminho caminho = new
	 * Caminho(); int incrementCol = movimentoMatrisColuna(this.inicio[0],
	 * this.fim[0]);
	 * 
	 * System.out.println("Origem " + inicio[0] + " " + inicio[1]);
	 * System.out.println("Destino " + fim[0] + " " + fim[1]);
	 * 
	 * caminho.addPasso(inicio);
	 * 
	 * int linhaAtual = inicio[1]; int colunaAtual = inicio[0]; while (true) {
	 * int colunaDestino = colunaAtual + 1; int linhaDestino =
	 * pontoMedioMaisProximo(linhaAtual, colunaDestino);
	 * 
	 * int incremetLin = 0; if (linhaAtual > linhaDestino) { incremetLin = -1;
	 * // sobe } else { incremetLin = +1; // desce } // monta o caminho até esse
	 * ponto encontrado while (true) { if (linhaAtual != linhaDestino &&
	 * ehLivre(mapa[colunaAtual][linhaAtual + incremetLin])) { linhaAtual +=
	 * incremetLin; } else if (colunaAtual != colunaDestino &&
	 * ehLivre(mapa[colunaAtual + incrementCol][linhaAtual])) { colunaAtual +=
	 * incrementCol; } else { throw new
	 * Exception("Não foi possível formar um caminho de col:" + colunaAtual +
	 * " lin:" + linhaAtual + " até col:" + colunaDestino + " lin:" +
	 * linhaDestino); } caminho.addPasso(new int[] { colunaAtual, linhaAtual });
	 * 
	 * System.out.println(colunaAtual + " " + linhaAtual + " - " + colunaDestino
	 * + " " + linhaDestino);
	 * 
	 * if (linhaAtual == linhaDestino && colunaAtual == colunaDestino) {
	 * System.out.println("OK :D"); break; } }
	 * 
	 * if (colunaAtual == fim[0] && linhaAtual != linhaDestino) { // E se tiver
	 * algum obstáculo no caminho? throw new Exception(
	 * "Não implementado!\r\nFazer robo descer ou subir para alcançar o objetivo"
	 * ); }
	 * 
	 * if (colunaAtual == fim[0] && linhaAtual == fim[1]) break; }
	 * 
	 * return caminho; }
	 * 
	 * private int pontoMedioMaisProximo(int linhaAtual, int colunaDestino) { //
	 * Procura o ponto médio mais proximo na proxima coluna int linhaDestino =
	 * Integer.MAX_VALUE; int menorDiferenca = Integer.MAX_VALUE; for (int[]
	 * ponto : pontosMedios) { if (ponto[0] == colunaDestino) { int diferenca =
	 * Math.abs(linhaAtual - ponto[1]); if (diferenca < menorDiferenca) {
	 * menorDiferenca = diferenca; linhaDestino = ponto[1]; } // Da pra melhorar
	 * quando for igual // Se for igual pegar o ponto que está mais proximo do
	 * // destino } } return linhaDestino; }
	 */

}
