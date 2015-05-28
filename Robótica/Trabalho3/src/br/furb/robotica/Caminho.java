package br.furb.robotica;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Objeto que armazena a sequência de passos para que o robô se desloque de uma
 * posição até outra.
 */
public class Caminho implements Enumeration<int[]> {

	private List<int[]> caminho;
	private int index;

	public Caminho() {
		caminho = new ArrayList<int[]>();
	}

	public void addPasso(int coluna, int linha) {
		int[] coordenada = new int[2];
		coordenada[Matriz.COLUNA] = coluna;
		coordenada[Matriz.LINHA] = linha;
		addPasso(coordenada);
	}

	public void addPasso(int[] posicao) {
		this.caminho.add(posicao);
	}

	public void imprimeCaminho() {
		Debug.println(toString());
	}

	@Override
	public String toString() {
		StringBuilder saida = new StringBuilder();
		for (int[] is : this.caminho) {
			saida.append("C:").append(is[Matriz.COLUNA]).append("L:")
					.append(is[Matriz.LINHA]).append(' ');
		}
		return saida.substring(0, saida.length() - 1);
	}

	@Override
	protected Caminho clone() {
		Caminho caminhoClone = new Caminho();
		caminhoClone.caminho = new ArrayList<int[]>(this.caminho);
		return caminhoClone;
	}

	@Override
	public boolean hasMoreElements() {
		return index < caminho.size();
	}

	@Override
	public int[] nextElement() {
		if (hasMoreElements()) {
			return caminho.get(index++);
		}
		return null;
	}

	public void toFirst() {
		this.index = 0;
	}

	/**
	 * Retira passos duplicados.
	 */
	public void otimizar() {
		for (int i = 1; i < caminho.size() - 1; i++) {
			if (caminho.get(i - 1)[0] == caminho.get(i + 1)[0]
					&& caminho.get(i - 1)[1] == caminho.get(i + 1)[1]) {
				//Remove a ponta
				caminho.remove(i);
				//Aqui irá ficar 2 celulas com mesmo valor, então remove também
				caminho.remove(i);
				i -= 2;
			}
		}
	}

	public boolean isAfterLast() {
		return this.index >= this.caminho.size();
	}
}
