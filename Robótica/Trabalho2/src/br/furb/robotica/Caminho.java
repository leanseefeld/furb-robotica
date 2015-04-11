package br.furb.robotica;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Caminho implements Enumeration<int[]> {

	private List<int[]> caminho;
	private int index;

	public Caminho() {
		caminho = new ArrayList<int[]>();
	}

	public void addPasso(int coluna, int linha) {
		addPasso(new int[] { coluna, linha });
	}

	public void addPasso(int[] posicao) {
		this.caminho.add(posicao);
		// System.out.println(this.toString());
	}

	public void imprimeCaminho() {
		System.out.println(toString());
	}

	@Override
	public String toString() {
		String saida = "";
		for (int[] is : this.caminho) {
			saida += "C:" + is[0] + "L:" + is[1] + " ";
		}
		return saida;
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
	 * Retirar passos duplicados
	 */
	public void Otimizar() {
		for (int i = 1; i < caminho.size() - 1; i++) {
			if (caminho.get(i - 1)[0] == caminho.get(i + 1)[0] && 
					caminho.get(i - 1)[1] == caminho.get(i + 1)[1]) {
				//Remove a ponta
				caminho.remove(i);
				//Aqui irá ficar 2 celulas com mesmo valor, então remove também
				caminho.remove(i);
				i-=2;
			}
		}
	}
}
