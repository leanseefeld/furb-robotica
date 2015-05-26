package br.furb.teste.robotica;

import br.furb.robotica.Caminho;
import br.furb.robotica.InfoPosicao;
import br.furb.robotica.Lado;
import br.furb.robotica.MapaLabirinto;
import br.furb.robotica.common.Coordenada;
import br.furb.robotica.estruturas.MinhaPilha;

public class RoboMapeador {

    //    private static final int VELOCIDADE = 600;
    //    private static final int PASSO = 600;
    //    private static final int DISTANCIA_OBSTACULO = 50;
    //    private static final int _90GRAUS = 90;

    private int[] coordenadaAtual;
    private Lado ladoAtual;
    private MapaLabirinto mapa;
    //    private UltrasonicSensor sensor;
    private Caminho caminho;
    private boolean mapaCompleto;
    private MinhaPilha<int[]> coordenadasNaoVisitados;
    private final int[] coordenadaInicial;
    private final Lado ladoInicial;

    public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual, int[] coordenadaAtual) {
	//	Motor.A.setSpeed(VELOCIDADE);
	//	Motor.B.setSpeed(VELOCIDADE);

	this.coordenadaInicial = coordenadaAtual;
	this.coordenadaAtual = coordenadaAtual;
	this.ladoAtual = ladoAtual;
	this.ladoInicial = ladoAtual;
	this.mapa = mapa;
	this.coordenadasNaoVisitados = new MinhaLinkedList<int[]>();
	//	this.sensor = new UltrasonicSensor(SensorPort.S4);
    }

    /**
     * @return Retorna o caminho que o robo deve seguir
     */
    public Caminho getCaminho() {
	return this.caminho;
    }

    /**
     * Insere um caminho para o robo seguir
     * 
     * @param caminho
     */
    public void setCaminho(Caminho caminho) {
	this.caminho = caminho;
    }

    public int[] getCoordenadaAtual() {
	return coordenadaAtual;
    }

    /**
     * @return Retorna a posição atual do robo no mapa
     */
    public InfoPosicao getPosicaoAtual() {
	return this.mapa.getInfoPosicao(this.coordenadaAtual);
    }

    public void addCoordenadaNaoExplorada(int[] coordenada) {
	this.coordenadasNaoVisitados.empilhar(coordenada);
    }

    /**
     * Pega informações da posição atual do robo
     */
    public void analisarPosicao() {
	if (mapa.getInfoPosicao(coordenadaAtual) != null) {
	    //Apenas para testes... depois remover isso
	    throw new UnsupportedOperationException("analisarPosicao não deveria ter sido chamado");
	}

	InfoPosicao infoPosicao = mapa.criarPosicao(coordenadaAtual);

	if (Terreno.isDestino(coordenadaAtual)) {
	    this.mapa.setCoordenadaDestino(coordenadaAtual);
	}
	Terreno.verificarPosicao(coordenadaAtual, infoPosicao);

	//	Motor.C.rotate(-_90GRAUS); //ESQUERDA DO ROBO
	//	Lado ladoSensor = Lado.valueOf(Math.abs(this.ladoAtual.ordinal() - 1) % 4);
	//	System.out.println("Olhou para o lado" + ladoSensor.name());
	//	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);
	//
	//	Motor.C.rotate(+_90GRAUS); //FRENTE DO ROBO
	//	ladoSensor = Lado.valueOf(this.ladoAtual.ordinal());
	//	System.out.println("Olhou para o lado" + ladoSensor.name());
	//	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);
	//
	//	Motor.C.rotate(+_90GRAUS); //ESQUERDA DO ROBO
	//	ladoSensor = Lado.valueOf((this.ladoAtual.ordinal() + 1) % 4);
	//	System.out.println("Olhou para o lado" + ladoSensor.name());
	//	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);
	//
	//	//Aponta o sensor para frente novamente
	//	Motor.C.rotate(-_90GRAUS, true);
    }

    /**
     * Monta um caminho para uma posição desejada para que o robo siga
     */
    public Caminho montarCaminhoAteProximaPosicao() {
	Caminho caminho = null;
	int[][] coordenadasVisinhas = mapa.getVisinhosConexosNaoExplorados(coordenadaAtual);

	if (coordenadasVisinhas.length != 0) {
	    caminho = new Caminho();
	    caminho.addPasso(coordenadasVisinhas[0]);
	    for (int i = 1; i < coordenadasVisinhas.length; i++) {
		coordenadasNaoVisitados.empilhar(coordenadasVisinhas[i]);
	    }
	} else {
	    int[] coord = null;
	    while ((coord = coordenadasNaoVisitados.pegar()) != null) {
		if (mapa.getInfoPosicao(coord) == null) {
		    caminho = mapa.montarCaminhoDijkstra(coordenadaAtual, coord);
		    //caminho = mapa.montarCaminhoWaveFront(coordenadaAtual, coord, new MinhaLinkedList<int[]>());
		    break;
		}
	    }

	    if (caminho == null) {
		this.mapaCompleto = true;
		return null;
	    } else {
		caminho.toFirst();
		caminho.nextElement();
	    }
	}
	return caminho;
    }
    
    public Caminho montarMenorCaminho() {
	Caminho caminho = mapa.montarCaminhoDijkstra(this.coordenadaAtual, this.mapa.getCoordenadaDestino());
	caminho.nextElement();
	return caminho;
    }

    /**
     * Move para a proxima posição do caminho
     */
    public void moverProximaPosicao() {
	int[] proximaPosicao = null;

	proximaPosicao = caminho.nextElement();
	Lado novoSentido = mapa.getLado(this.coordenadaAtual, proximaPosicao);
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
	//	Motor.A.rotate(PASSO, true);
	//	Motor.B.rotate(PASSO);
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
	//	Motor.A.rotate((quantidadeGirar * _90GRAUS), true);
	//	Motor.B.rotate(-(quantidadeGirar * _90GRAUS));

	if (this.ladoAtual != ladoDestino) {
	    if (quantidadeGirar > 0) {
		System.out.print("Giou " + (quantidadeGirar * 90) + "º à direita");
	    } else {
		System.out.print("Girou " + (quantidadeGirar * 90 * -1) + "º à esquerda");
	    }
	    System.out.println(" - Novo lado " + ladoDestino.name());
	    this.ladoAtual = ladoDestino;
	}
    }

    /**
     * Exibe a posição e o lado atual do robo
     */
    public void printPosicaoAtual() {
	System.out.println("Posicao Atual: " + Coordenada.toString(coordenadaAtual) + "   Lado Atual: "
		+ this.ladoAtual.name());
    }

    public void moveRoboParaPontoInicial() {
	this.coordenadaAtual = this.coordenadaInicial;
	this.ladoAtual = this.ladoInicial;
    }
}
