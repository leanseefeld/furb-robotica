package br.furb.robotica.algoritmos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.furb.robotica.Cenarios;

public class Wavefront {

	static boolean log = true;

	/**
	 * Robô no mapa.
	 */
	public static final int R = -2;
	/**
	 * Obstáculo no mapa.
	 */
	public static final int X = -1;
	/**
	 * Célula vazia no mapa.
	 */
	public static final int V = 0;
	private static final int COL = 0;
	private static final int LINHA = 1;
	private static final int O = 2;

	private int[][] mapaOriginal;
	private int[][] mapaValorado;
	private Queue<int[]> vizinhosPendentes = new Queue<int[]>();

	public Wavefront(int[][] mapa) {
		this.mapaOriginal = mapa;
	}

	public Caminho buscarCaminho() {
		return buscarCaminho(localizarNoMapa(mapaOriginal, R));
	}

	/**
	 * @return lista das células a serem percorridas
	 */
	public Caminho buscarCaminho(int[] origem) {
		reset(this.mapaOriginal);
		configurar();
		valorarMapa();
		return gerarCaminho(origem);
	}

	protected Caminho gerarCaminho(int[] origem) {
		Caminho caminho = new Caminho();

		int[] passo = origem.clone();
		caminho.addPasso(passo);
		do {
			int[][] vizinhos = acharVizinhos(passo);
			passo = getMenorVizinhoValorado(vizinhos);
			caminho.addPasso(passo);
		} while (valorCelula(passo) != O);

		caminho.imprimeCaminho();
		return caminho;
	}

	private int[] getMenorVizinhoValorado(int[][] vizinhos) {
		int[] menor = vizinhos[0];
		int menorValor = valorCelula(menor);
		for (int i = 1; i < vizinhos.length; i++) {
			int[] vizinho = vizinhos[i];
			int valor = valorCelula(vizinho);
			if (menorValor < 2 || valor >= 2 && valor < menorValor) {
				menor = vizinho;
				menorValor = valorCelula(vizinho);
			}
		}
		return menor;
	}

	/**
	 * Retorna todos os vizinhos que não sejam obstáculos.
	 * 
	 * @param celula
	 *            celula cujo os vizinhos se deseja saber.
	 * @return vizinhos que não sejam obstáculos.
	 */
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
		return mapaValorado[celula[COL]][celula[LINHA]] != X;
	}

	private int valorCelula(int... celula) {
		return mapaValorado[celula[COL]][celula[LINHA]];
	}

	protected final boolean isIndicesValidos(int[] indices) {
		return indices[COL] < mapaValorado.length
				&& indices[LINHA] < mapaValorado[0].length // 
				&& indices[COL] >= 0 && indices[LINHA] >= 0;
	}

	public void reset(int[][] mapa) {
		mapaValorado = null;
		this.mapaOriginal = mapa;
	}

	protected void configurar() {
		this.mapaValorado = new int[this.mapaOriginal.length][this.mapaOriginal[0].length];
		for (int col = 0; col < mapaOriginal.length; col++) {
			for (int linha = 0; linha < mapaOriginal[col].length; linha++) {
				if (mapaOriginal[col][linha] == X) {
					mapaValorado[col][linha] = X;
				}
			}
		}
	}

	protected void valorarMapa() {
		int[] objetivo = localizarNoMapa(mapaOriginal, O);
		if (objetivo == null) {
			throw new IllegalStateException("objetivo não está no mapa");
		}
		mapaValorado[objetivo[COL]][objetivo[LINHA]] = O;

		//		valorarComFors(objetivo);

		escorrerValores();

		if (log) {
			System.out.println();
			Cenarios.imprimirCenario(mapaValorado);
			System.out.println();
		}
	}

	/**
	 * Faz com que os valores do mapa valorado "escorram" pras células ainda não
	 * valoradas.
	 */
	private void escorrerValores() {
		// encontrar célula vazia
		//		int[] celula = localizarNoMapa(mapaValorado, V);
		//		if (celula == null) {
		//			return;
		//		}
		int[] celula = localizarNoMapa(mapaOriginal, O);
		for (int[] v : acharVizinhos(celula)) {
			vizinhosPendentes.push(v);
		}

		while (!vizinhosPendentes.isEmpty()) {
			int[] vizinho = (int[]) vizinhosPendentes.pop();
			if (valorCelula(vizinho) == V) {
				escorrerValores(vizinho);
			}
		}
	}

	private void escorrerValores(int[] celula) {
		// se não tem vizinhos, ignora
		int[][] vizinhos = acharVizinhos(celula);
		if (vizinhos.length == 0) {
			return;
		}

		// atribuir o valor do menor vizinho + 1
		int[] menorVizinho = getMenorVizinhoValorado(vizinhos);
		mapaValorado[celula[COL]][celula[LINHA]] = valorCelula(menorVizinho) + 1;

		// após atribuir o valor, ir para os próximos vizinhos vazios
		for (int[] vizinho : vizinhos) {
			if (valorCelula(vizinho) == V) {
				vizinhosPendentes.push(vizinho);
			}
		}
	}

	private static int[] localizarNoMapa(int[][] mapa, final int valor) {
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
