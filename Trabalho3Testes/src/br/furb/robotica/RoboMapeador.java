package br.furb.robotica;

import java.util.List;

public class RoboMapeador {

    private int[] coordenadaAtual;
    private Lado ladoAtual;
    private MapaLabirinto mapa;
    private Caminho caminho;
    private boolean mapaCompleto;

    public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual) {
	this.mapa = mapa;
	this.ladoAtual = ladoAtual;
    }

    /**
     * @return Retorna o caminho que o robo deve seguir
     */
    public Caminho getCaminho() {
	return this.caminho;
    }

    /**
     * @return Retorna a posição atual do robo no mapa
     */
    public InfoPosicao getPosicaoAtual() {
	return this.mapa.getInfoPosicao(this.coordenadaAtual);
    }

    /**
     * Pega informações da posição atual do robo
     */
    public void analisarPosicao() {
	if (mapa.getInfoPosicao(coordenadaAtual) != null) {
	    //Apenas para testes... depois remover isso
	    throw new UnsupportedOperationException("analisarPosicao não deveria ter sido chamado");
	}

	System.out.println("Posição analizada");
    }

    /**
     * Monta um caminho para uma posição desejada para que o robo siga
     */
    public void montarCaminhoAteProximaPosicao() {
	int[] coordenadaVisinho = this.mapa.getVisinhoNaoVisitado(this.coordenadaAtual);

	//TODO: Fazer isso usando busca em profundidade... deve ficar mais facil
	if (coordenadaVisinho != null) {
	    this.caminho = new Caminho();
	    this.caminho.addPasso(this.coordenadaAtual);
	    this.caminho.addPasso(coordenadaVisinho);
	} else {
	    List<int[]> coordenadas = this.mapa.getCoordenadasNaoVisitadas();
	    Caminho caminho = null;
	    for (int[] coord : coordenadas) {
		caminho = this.mapa.montarCaminho(this.coordenadaAtual, coord);
		if (caminho != null)
		    break;
	    }
	    if (caminho == null)
		mapaCompleto = true;
	}
	caminho.toFirst();
	//Move um para ignorar a posição atual
	caminho.nextElement();
    }

    /**
     * Move para a proxima posição do caminho
     */
    public void moverProximaPosicao() {
	int[] proximaPosicao = null;
	printPosicaoAtual();

	proximaPosicao = caminho.nextElement();
	Lado novoSentido =  mapa.getLado(this.coordenadaAtual, proximaPosicao);
	girar(novoSentido);
	moverEmFrente();
	this.ladoAtual = novoSentido;
	this.coordenadaAtual = proximaPosicao;
	printPosicaoAtual();
    }

    /**
     * Verifica se todo o mapa ja foi mapeado
     * 
     * @return
     */
    public boolean mapeamentoEstaCompleto() {
	return mapaCompleto;
    }

    /**
     * Move o robo uma posição para frente
     */
    private void moverEmFrente() {
	System.out.println("Moveu para frente");
    }

    /**
     * Gira o robo no para o lado desejado
     * 
     * @param ladoDestino
     */
    private void girar(Lado ladoDestino) {
	int quantidadeGirar = ladoDestino.ordinal() - this.ladoAtual.ordinal();

	// Se for 3, muda para 1 e muda a direção da rotação
	// se for 4, fica parado
	if (quantidadeGirar > 2) {
	    quantidadeGirar = -(quantidadeGirar % 2);
	}

	// Obs: Se quantidadeGirar for positiva, irá girar para direita
	// se for negativa, irá ser para a esquerda

	if (this.ladoAtual != ladoDestino) {
	    System.out.print("Novo sentido " + ladoDestino.name());
	    if (quantidadeGirar > 0) {
		System.out.println(" - GIROU " + (quantidadeGirar * 90) + "º à direita");
	    } else {
		System.out.println(" - GIROU " + (quantidadeGirar * 90 * -1) + "º à esquerda");
	    }
	    this.ladoAtual = ladoDestino;
	}
    }

    /**
     * Exibe a posição e o lado atual do robo
     */
    public void printPosicaoAtual() {
	System.out.println("Posicao Atual: Coluna:" + this.coordenadaAtual[0] + " Linha:" + this.coordenadaAtual[1]);
	System.out.println("Lado Atual: " + this.ladoAtual.name());
    }
}
