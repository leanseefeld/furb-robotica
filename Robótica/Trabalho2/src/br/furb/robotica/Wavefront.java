package br.furb.robotica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Wavefront {

	/**
	 * Robô no mapa.
	 */
	public static final int R = 2;
	/**
	 * Obstáculo no mapa.
	 */
	public static final int O = -1;
	/**
	 * Célula vazia no mapa.
	 */
	public static final int V = 0;
	private static final int COL = 0;
	private static final int LINHA = 1;
	private static final int D = 2;

	private int[][] mapa;
	private int[][] mapaValorado;

	public Wavefront(int[][] mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return lista das células a serem percorridas
	 */
	public int[][] buscarCaminho(int[] origem) {
		reset(this.mapa);
		configurar();
		valorarMapa();
		return gerarCaminho(origem);
	}

	protected int[][] gerarCaminho(int[] origem) {
		List<int[]> caminho = new LinkedList<>();

		int[] passo = origem.clone();
		caminho.add(passo);
		do {
			int[][] vizinhos = acharVizinhos(passo);
			int[] menor = vizinhos[0];
			int menorValor = valorCelula(menor);
			for (int i = 1; i < vizinhos.length; i++) {
				int[] vizinho = vizinhos[i];
				if (valorCelula(vizinho) < menorValor) {
					menor = vizinho;
					menorValor = valorCelula(vizinho);
				}
			}

			passo = menor;
			caminho.add(passo);
		} while (valorCelula(passo) != D);

		return caminho.toArray(new int[caminho.size()][2]);
	}

	private int[][] acharVizinhos(int[] celula) {
		List<int[]> vizinhos = new ArrayList<>(4);
		List<int[]> possiveisVizinhos = criarLista( //
				new int[] { celula[COL] + 1, celula[LINHA] }, // 
				new int[] { celula[COL] - 1, celula[LINHA] }, //
				new int[] { celula[COL], celula[LINHA] + 1 }, //
				new int[] { celula[COL], celula[LINHA] - 1 });

		for (int[] vizinho : possiveisVizinhos) {
			if (isIndicesValidos(vizinho) && caminhoLivre(vizinho)) {
				vizinhos.add(vizinho);
			}
		}

		return vizinhos.toArray(new int[vizinhos.size()][2]);
	}

	private boolean caminhoLivre(int[] celula) {
		return mapaValorado[celula[COL]][celula[LINHA]] != O;
	}

	private int valorCelula(int[] celula) {
		return mapaValorado[celula[COL]][celula[LINHA]];
	}

	protected final boolean isIndicesValidos(int[] indices) {
		return indices[COL] < mapaValorado.length
				&& indices[LINHA] < mapaValorado[0].length && indices[COL] >= 0
				&& indices[LINHA] >= 0;
	}

	public void reset(int[][] mapa) {
		mapaValorado = null;
		this.mapa = mapa;
	}

	protected void configurar() {
		this.mapaValorado = new int[this.mapa.length][this.mapa[0].length];
	}

	protected void valorarMapa() {
		int[] robo = localizarNoMapa(R);
		if (robo == null) {
			throw new IllegalStateException("robô não está no mapa");
		}
		valorarQ1(robo);
		valorarQ2(robo);
		valorarQ3(robo);
		valorarQ4(robo);
		escorrerValores();
	}

	private void valorarQ1(int[] robo) {

	}

	private void valorarQ2(int[] robo) {

	}

	private void valorarQ3(int[] robo) {

	}

	private void valorarQ4(int[] robo) {

	}

	/**
	 * Faz com que os valores do mapa valorado "escorram" pras células ainda não
	 * valoradas.
	 */
	private void escorrerValores() {
		// escorrer linhas
		for (int col = 0; col < mapaValorado.length; col++) {
			int ultimoValor = 0;
			for (int linha = 0; linha < mapaValorado.length; linha++) {
				int valor = mapaValorado[col][linha];
				if (valor != V && valor != R) {
					ultimoValor = valor;
				} else if (valor == V) {
					mapaValorado[col][linha] = ++ultimoValor;
				}
			}
		}

		// escorrer colunas
		for (int linha = 0; linha < mapaValorado.length; linha++) {
			int ultimoValor = 0;
			for (int col = 0; col < mapaValorado.length; col++) {
				int valor = mapaValorado[col][linha];
				if (valor != V && valor != R) {
					ultimoValor = valor;
				} else if (valor == V) {
					mapaValorado[col][linha] = ++ultimoValor;
				}
			}
		}
	}

	protected int[] localizarNoMapa(final int valor) {
		int[] celula = null;
		for (int col = 0; col < mapa.length; col++) {
			for (int linha = 0; linha < mapa[col].length; linha++) {
				if (valor == mapa[col][linha]) {
					celula = new int[] { col, linha };
					break;
				}
			}
		}
		return celula;
	}

	public static final List<int[]> criarLista(int[]... cells) {
		List<int[]> ret = new ArrayList<>();
		for (int[] cell : cells) {
			ret.add(cell);
		}
		return ret;
	}

}
