package br.furb.teste.robotica;

import java.util.LinkedList;
import br.furb.robotica.MinhaPilha;

public class MinhaLinkedList<T> extends LinkedList<T> implements MinhaPilha<T> {

    @Override
    public void empilhar(T objeto) {
	this.push(objeto);
    }

    @Override
    public T pegar() {
	return super.poll();
    }

    @Override
    public boolean estaVazia() {
	return this.isEmpty();
    }

}
