package br.furb.robotica.estruturas;

import java.util.Queue;

public class MinhaQueue<T> extends Queue<T> implements MinhaPilha<T> {

	@Override
	public void empilhar(T objeto) {
		this.push(objeto);
	}

	@Override
	public boolean estaVazia() {
		return this.isEmpty();
	}

	@Override
	public T pegar() {
		if (isEmpty()) {
			return null;
		}
		return (T) this.pop();
	}

}
