package br.furb.robotica;

import java.util.Queue;

public class MinhaQueue<T> extends Queue<T> implements MinhaPilha<T> {

    @Override
    public void empilhar(T objeto) {
	this.push(objeto);
    }

    @Override
    public T pegar() {
	return (T) this.pop();
    }

    @Override
    public boolean estaVazia() {
	return this.isEmpty();
    }

}
