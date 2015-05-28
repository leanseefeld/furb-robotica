package br.furb.robotica.estruturas.lista;

import br.furb.robotica.Debug;

/**
 * Nota ao professor: mesma classe utilizada em 2013/I como exercício/trabalho.<br>
 * Não precisa imprimir.
 */
public class Lista {

	private NoLista prim;

	public void insere(int v) {
		this.prim = new NoLista(v, this.prim);
	}

	public void imprime() {
		Debug.println(toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		NoLista no = this.prim;
		boolean comma = false;
		while (no != null) {
			if (comma)
				builder.append(", ");
			builder.append(no.toString());
			no = no.getProx();
			comma = true;
		}
		builder.append('}');
		return builder.toString();
	}

	public boolean vazia() {
		return prim == null;
	}

	public NoLista buscar(int v) {
		NoLista no = this.prim;
		while (no != null && no.getInfo() != v)
			no = no.getProx();
		return no;
	}

	public NoLista buscarIndice(int index) {
		if (index < 0)
			throw new ArrayIndexOutOfBoundsException();
		if (prim == null)
			return null;
		if (index == 0)
			return prim;
		NoLista no = prim.getProx();
		for (int i = 1; i < index; i++)
			no = no.getProx();
		return no;
	}

	public int comprimento() {
		int count = 0;
		NoLista no = prim;
		while (no != null) {
			count++;
			no = no.getProx();
		}
		return count;
	}

	public NoLista ultimo() {
		NoLista no = this.prim;
		if (no == null)
			return null;
		NoLista proximo;
		while ((proximo = no.getProx()) != null) {
			no = proximo;
		}
		return no;
	}

	public void retira(int v) {
		NoLista p = prim;
		NoLista ant = null;
		if (p != null) {
			while (p != null && p.getInfo() != v) {
				ant = p;
				p = p.getProx();
			}
			if (p == null)
				return;
			if (ant == null) {
				prim = p.getProx();
			} else {
				ant.setProx(p.getProx());
			}
		}
	}

	public boolean igual(Lista outra) {
		if (outra == null)
			return false;
		NoLista a = this.prim;
		NoLista b = outra.prim;
		while ((a != null && b != null) && (a.getInfo() == b.getInfo())) {
			a = a.getProx();
			b = b.getProx();
		}
		return a == null && b == null;
	}

}
