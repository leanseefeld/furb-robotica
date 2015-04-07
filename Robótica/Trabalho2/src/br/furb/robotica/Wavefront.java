package br.furb.robotica;

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

	private int[][] mapa;
	private int[][] mapaValorado;

	public Wavefront(int[][] mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return lista das células a serem percorridas
	 */
	public int[][] buscarCaminho() {
		reset(this.mapa);
		configurar();
		valorarMapa();
		return gerarCaminho();
	}

	protected int[][] gerarCaminho() {
		List<int[]> caminho = new LinkedList<>();
		// TODO
		return caminho.toArray(new int[caminho.size()][2]);
	}

	public void reset(int[][] mapa) {
		mapaValorado = null;
		this.mapa = mapa;
	}

	protected void configurar() {
		this.mapaValorado = new int[this.mapa.length][this.mapa[0].length];
	}

	protected void valorarMapa() {
		int[] robo = localizar(R);
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

	protected int[] localizar(final int valor) {
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
}
