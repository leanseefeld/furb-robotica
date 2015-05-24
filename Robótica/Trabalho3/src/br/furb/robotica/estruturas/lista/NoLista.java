package br.furb.robotica.estruturas.lista;

/**
 * Nota ao professor: mesma classe utilizada em 2013/I como exercício/trabalho.<br>
 * Não precisa imprimir.
 */
public class NoLista {

    private int info;
    private NoLista prox;

    public NoLista() {
	// vazio
    }

    public NoLista(int info, NoLista prox) {
	setInfo(info);
	setProx(prox);
    }

    public void setInfo(int info) {
	this.info = info;
    }

    public int getInfo() {
	return info;
    }

    public NoLista getProx() {
	return this.prox;
    }

    public void setProx(NoLista prox) {
	this.prox = prox;
    }

    @Override
    public String toString() {
	return String.valueOf(getInfo());
    }

}
