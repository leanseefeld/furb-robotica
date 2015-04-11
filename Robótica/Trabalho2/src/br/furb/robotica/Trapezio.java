package br.furb.robotica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Trapezio {

	private List<int[]> pontosMedios;
	private int[][] mapa;
	private final int[] inicio;
	private final int[] fim;

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
	
	public Caminho montaCaminhoOtimizado()
	{
		//TODO: Reduzir o caminho quando o robo passa pelo mesmo lugar 2 vezes
		return null;
	}

	public Caminho montaCaminho()
	{
		/*int incrementCol = movimentoMatrisColuna(this.inicio[0], this.fim[1]);
		List<int[]> pontosDestinos = pontosMediosDaColuna(this.inicio[1], this.inicio[0] + incrementCol);
		for (int[] pontoDestino : pontosDestinos) {
			Caminho caminhoEncontrado = montaCaminhoRecursivo(this.inicio.clone(), pontoDestino, new Caminho());
			if (caminhoEncontrado != null)
				return caminhoEncontrado;
		}
		System.out.println("Não foi encontrado nenhum caminho possível");
		return null;*/
		Caminho caminho = new Caminho();
		caminho.addPasso(this.inicio);
		return montaCaminhoRecursivo(this.inicio, this.inicio, caminho);
	}
	
	private Caminho montaCaminhoRecursivo(int[] posicaoAtual, int[] destino, Caminho caminho) {
		int linhaAtual = posicaoAtual[1];
		int colunaAtual = posicaoAtual[0];
		int incrementCol = movimentoMatrisColuna(colunaAtual, destino[0]);
		int incremetLin = movimentoMatrisLinha(linhaAtual, destino[1]);

		// monta o caminho até o ponto de destino
		while (true) {

			if (linhaAtual == destino[1] && colunaAtual == destino[0]) {
				log("Chegou a coluna e linha esperada");
				break;
			}

			if (linhaAtual != destino[1] && ehLivre(mapa[colunaAtual][linhaAtual + incremetLin])) {
				linhaAtual += incremetLin;
			} else if (colunaAtual != destino[0] && ehLivre(mapa[colunaAtual + incrementCol][linhaAtual])) {
				colunaAtual += incrementCol;
			} else {
				log("Caminho inválido");
				return null; // Sem caminho livre
			}
			
			caminho.addPasso(colunaAtual, linhaAtual);

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
			log("Solução encontrada!");
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

	private void log(String msg) {
		//System.out.println(msg);
	}

	private int[][] BuscarOrigemEDestino() {
		int[][] origemDestino = new int[2][2];
		for (int i = 0; i < mapa.length; i++) {
			for (int j = 0; j < mapa[i].length; j++) {
				if (mapa[i][j] == Main.O)
					origemDestino[1] = new int[] { i, j };// Objetivo
				else if (mapa[i][j] == Main.R)
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
		//montaCaminhoRecursivo(this.inicio, this.inicio, new Caminho());
		montaCaminho();
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
		return Main.X != celula;// && R != celula;
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
}
