package br.furb.robotica;

public class Passo {

    private No no;
    private Passo pai;
    private Sentido sentidoOrigem;

    public Passo(No no, Passo pai, Sentido sentidoOrigem) {
        this.no = no;
        this.pai = pai;
        this.sentidoOrigem = sentidoOrigem;
    }

    public No getNo() {
        return no;
    }

    public void setNo(No no) {
        this.no = no;
    }

    public Passo getPai() {
        return pai;
    }

    public void setPai(Passo pai) {
        this.pai = pai;
    }

    public Sentido getSentidoOrigem() {
        return sentidoOrigem;
    }

    public void setSentidoOrigem(Sentido sentidoOrigem) {
        this.sentidoOrigem = sentidoOrigem;
    }

}