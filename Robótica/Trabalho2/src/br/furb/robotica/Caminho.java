package br.furb.robotica;

import java.util.ArrayList;
import java.util.List;

public class Caminho {
	private List<int[]> caminho;
	private int numeroPassos;

	public Caminho() {
		caminho = new ArrayList<int[]>();
	}

	public void addPasso(int coluna, int linha) {
		this.caminho.add(new int[] { coluna, linha });
		System.out.println(this.toString());
	}

	public void addPasso(int[] posicao) {
		this.caminho.add(posicao);
		this.numeroPassos++;
	}

	public void imprimeCaminho() throws Exception {
		try {
			System.out.println(this.toString());
		} catch (Exception ex) {
			// System.out.println("Erro ao montar o caminho\r\n" +
			// ex.getMessage());
			throw ex;
		}
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
		caminhoClone.numeroPassos = this.numeroPassos;
		return caminhoClone;
	}
}
