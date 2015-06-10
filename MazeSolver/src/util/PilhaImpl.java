package util;

import java.util.Stack;

public class PilhaImpl<T> extends Stack<T> implements Pilha<T> {

    @Override
    public void empilhar(T objeto) {
	push(objeto);
    }

    @Override
    public boolean estaVazia() {
	return isEmpty();
    }

    @Override
    public T pegar() {
	if (isEmpty()) {
	    return null;
	}
	return (T) pop();
    }

}
