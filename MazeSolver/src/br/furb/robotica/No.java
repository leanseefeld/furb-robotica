package br.furb.robotica;

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

    public void setVizinho(Lado lado, No vizinho) {
	switch (lado) {
	    case ATRAS:
		setAtras(vizinho);
		break;
	    case FRENTE:
		setFrente(vizinho);
		break;
	    case DIREITA:
		setDireita(vizinho);
		break;
	    case ESQUERDA:
		setEsquerda(vizinho);
		break;
	}
    }

    public No getVizinho(Lado lado) {
	switch (lado) {
	    case ATRAS:
		return getAtras();
	    case FRENTE:
		return getFrente();
	    case DIREITA:
		return getDireita();
	    case ESQUERDA:
		return getEsquerda();
	}
	return null;
    }

}
