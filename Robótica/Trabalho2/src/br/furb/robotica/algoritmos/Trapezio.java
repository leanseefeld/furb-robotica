package br.furb.robotica.algoritmos;

import static br.furb.robotica.Constantes.O;
import static br.furb.robotica.Constantes.R;
import static br.furb.robotica.Constantes.X;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.furb.robotica.Cenarios;

public class Trapezio implements GeradorCaminho {

	/*public static void main(String[] args)
	{
		System.out.println(
				new Trapezio(Cenarios.getCenarioA()).gerarCaminho().toString());
	}*/
	
	private List<int[]> pontosMedios;
	private int[][] mapa;
	private final int[] inicio;
	private final int[] fim;

	/**
	 * @param mapa
	 * Mapa que será buscado os pontos médios para montar o caminho
	 */
	public Trapezio(int[][] mapa) {
		this.mapa = mapa;

		int[][] origemDestino = BuscarOrigemEDestino();
		this.inicio = origemDestino[0];
		this.fim = origemDestino[1];

		pontosMedios = new LinkedList<>();

		montarPontosMedios();
	}

	/**
	 * Retorna uma lista com os pontos médios da coluna
	 * @param linhaAtual 
	 * Usada para ordernar a lista com os pontos mais próximos da linha atual
	 * @param colunaDestino
	 * Coluna em que será buscado os pontos médios
	 * @return 
	 * Lista com os pontos médios ordenados pela aproximidade com a linha atual e a linha de destino
	 */
	private List<int[]> pontosMediosDaColuna(int linhaAtual, int colunaDestino) {
		ArrayList<int[]> linhasDestinos = new ArrayList<>();
		for (int[] ponto : pontosMedios) {
			if (ponto[0] == colunaDestino) {
				linhasDestinos.add(ponto);
			}
		}

		int[] pesos = new int[linhasDestinos.size()];

		//Adiciona um peso para cada linha de destino
		//O peso é a aproximidade com o nó atual e a Aproximidade com o nó Destino
		for (int i = 0; i < linhasDestinos.size(); i++) {
			pesos[i] = Math.abs(linhaAtual - linhasDestinos.get(i)[1]);
			pesos[i] += Math.abs(linhasDestinos.get(i)[1] - fim[1]);
		}

		//Ordena pelo peso crescente
		for (int i = 0; i < pesos.length; i++) {
			for (int j = i; j < pesos.length; j++) {
				if (pesos[i] > pesos[j]) {
					int aux = pesos[i];
					pesos[i] = pesos[j];
					pesos[j] = aux;

					int[] aux2 = linhasDestinos.get(i);
					linhasDestinos.set(i, linhasDestinos.get(j));
					linhasDestinos.set(j, aux2);
				}
			}
		}

		return linhasDestinos;
	}

	/**
	 * Gera o caminho a ser seguido baseado nos pontos médios gerados
	 */
	public Caminho gerarCaminho() {
		Caminho caminho = new Caminho();
		caminho.addPasso(this.inicio);
		caminho = montaCaminhoRecursivo(this.inicio, this.inicio, caminho);
		return caminho;
	}

	private Caminho montaCaminhoRecursivo(int[] posicaoAtual, int[] destino,
			Caminho caminho) {
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

			if (linhaAtual != destino[1]
					&& ehLivre(mapa[colunaAtual][linhaAtual + incremetLin])) {
				linhaAtual += incremetLin;
			} else if (colunaAtual != destino[0]
					&& ehLivre(mapa[colunaAtual + incrementCol][linhaAtual])) {
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

		List<int[]> pontosDestinos = pontosMediosDaColuna(linhaAtual,
				colunaAtual + incrementCol);
		for (int[] pontoDestino : pontosDestinos) {
			Caminho caminhoEncontrado = montaCaminhoRecursivo(new int[] {
					colunaAtual, linhaAtual }, pontoDestino, caminho.clone());
			if (caminhoEncontrado != null)
				return caminhoEncontrado;
		}

		return null; // nenhum caminho livre por aqui
	}

	/**
	 * Busca todos os pontos médios do mapa
	 */
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
	
	/**
	 * Imprime os pontos médios do mapa passado no construtor
	 */
	public void imprimePontosMedios() {
		for (int[] ponto : pontosMedios) {
			System.out.println("Col: " + ponto[0] + " Lin: " + ponto[1]);
		}
	}

	/**
	 * Localiza o Inicio(Robô) e o Fim(Destino) do mapa
	 * @return 
	 * coluna inicio = int[0][0] linha inicio = int[0][1]
	 * coluna fim = int[1][0] linha fim = int[1][1]
	 */
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
	
	/**
	 * Verifica se a celula atual é está livre
	 * @param celula ponto X e Y do mapa
	 * @return
	 */
	private static boolean ehLivre(int celula) {
		return X != celula;// && R != celula;
	}

	/**
	 * Verifica se a linhaDestino está acima ou abaixo da linhaOrigem
	 * @param linhaOrigem
	 * @param linhaDestino
	 * @return
	 */
	private int movimentoMatrisLinha(int linhaOrigem, int linhaDestino) {
		if (linhaOrigem > linhaDestino) {
			return -1;
		} else {
			return +1;
		}
	}

	/**
	 * Verifica se a colunaOrigem está acima ou abaixo da colunaDestino
	 * @param colunaOrigem
	 * @param colunaDestino
	 * @return
	 */
	private int movimentoMatrisColuna(int colunaOrigem, int colunaDestino) {
		if (colunaOrigem > colunaDestino) {
			return -1;
		} else {
			return +1;
		}
	}
	
	/**
	 * Apenas para testes
	 * @param msg Mensagem que será exibida no console
	 */
	private void log(String msg) {
		System.out.println(msg);
	}

}
