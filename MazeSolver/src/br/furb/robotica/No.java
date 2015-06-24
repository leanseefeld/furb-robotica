package br.furb.robotica;

import java.util.ArrayList;
import java.util.List;

public class No {

    private No frente, atras, esquerda, direita;
    private boolean visitado;
    private int x, y;

    public No(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public No getFrente() {
	return frente;
    }

    public void setFrente(No frente) {
	this.frente = frente;
    }

    public No getAtras() {
	return atras;
    }

    public void setAtras(No atras) {
	this.atras = atras;
    }

    public No getEsquerda() {
	return esquerda;
    }

    public void setEsquerda(No esquerda) {
	this.esquerda = esquerda;
    }

    public No getDireita() {
	return direita;
    }

    public void setDireita(No direita) {
	this.direita = direita;
    }

    public boolean isVisitado() {
	if (!visitado) {
	    // se ele não foi visitado, mas todos os seus vizinhos foram,
	    // então não há necessidade de explorar ele
	    visitado = this.direita != null // 
		    && this.esquerda != null //
		    && this.frente != null //
		    && this.atras != null;
	}
	return visitado;
    }

    public void setVisitado(boolean visitado) {
	this.visitado = visitado;
    }

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    public void setVizinho(Sentido lado, No vizinho) {
	switch (lado) {
	    case SUL:
		setAtras(vizinho);
		break;
	    case NORTE:
		setFrente(vizinho);
		break;
	    case LESTE:
		setDireita(vizinho);
		break;
	    case OESTE:
		setEsquerda(vizinho);
		break;
	}
    }

    public No getVizinho(Sentido lado) {
	switch (lado) {
	    case SUL:
		return getAtras();
	    case NORTE:
		return getFrente();
	    case LESTE:
		return getDireita();
	    case OESTE:
		return getEsquerda();
	}
	return null;
    }

    @Override
    public String toString() {
	return "X:" + getX() + " Y:" + getY();
    }

    public List<No> getVizinhosConexos(boolean somenteNaoVisitados) {
	List<No> vizinhos = new ArrayList<>(4);
	for (Sentido sentido : Sentido.values()) {
	    No possivelVizinho = getVizinho(sentido);
	    if (isVisitado() || !somenteNaoVisitados) {
		vizinhos.add(possivelVizinho);
	    }
	}
	return vizinhos;
    }
}
