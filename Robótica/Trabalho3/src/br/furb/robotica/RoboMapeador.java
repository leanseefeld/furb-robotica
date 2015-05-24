package br.furb.robotica;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.common.Coordenada;
import br.furb.robotica.estruturas.MinhaPilha;
import br.furb.robotica.estruturas.MinhaQueue;

public class RoboMapeador {

    private static final int VELOCIDADE = 600;
    private static final int PASSO = 600;
    private static final int DISTANCIA_OBSTACULO = 50;
    private static final int _90GRAUS = 90;

    public static void main(String[] args) {
	System.out.println("Pressione ENTER");
	Button.ENTER.waitForPressAndRelease();

	MapaLabirinto mapa = new MapaLabirinto();
	mapa.setCoordenadaDestino(0, 0);
	RoboMapeador robo = new RoboMapeador(mapa, Lado.FRENTE, Coordenada.criar(3, 3));

	Behavior analizarPosicao = new BehaviorAnalizarPosicao(robo);
	Behavior montarTrajeto = new BehaviorMontarTrajeto(robo);
	Behavior seguirTrajeto = new BehaviorSeguirTrajeto(robo);
	Behavior mapeamentoCompleto = new BehaviorMapeamentoCompleto(robo);

	Behavior[] comportamentos = { analizarPosicao, montarTrajeto, seguirTrajeto, mapeamentoCompleto };
	Arbitrator arb = new Arbitrator(comportamentos);
	arb.start();
    }

    private int[] coordenadaAtual;
    private Lado ladoAtual;
    private MapaLabirinto mapa;
    private UltrasonicSensor sensor;
    private Caminho caminho;
    private boolean mapaCompleto;
    private MinhaPilha<int[]> coordenadasNaoVisitados;

    public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual, int[] coordenadaAtual) {
	Motor.A.setSpeed(VELOCIDADE);
	Motor.B.setSpeed(VELOCIDADE);

	this.coordenadaAtual = coordenadaAtual;
	this.mapa = mapa;
	this.ladoAtual = ladoAtual;
	this.coordenadasNaoVisitados = new MinhaQueue<int[]>();
	this.sensor = new UltrasonicSensor(SensorPort.S4);
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

	InfoPosicao infoPosicao = mapa.criarPosicao(coordenadaAtual);

	Motor.C.rotate(-_90GRAUS); //ESQUERDA DO ROBO
	Lado ladoSensor = Lado.valueOf(Math.abs((this.ladoAtual.ordinal() - 1)) % 4);
	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);

	Motor.C.rotate(+_90GRAUS); //FRENTE DO ROBO
	ladoSensor = Lado.valueOf(this.ladoAtual.ordinal());
	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);

	Motor.C.rotate(+_90GRAUS); //ESQUERDA DO ROBO
	ladoSensor = Lado.valueOf((this.ladoAtual.ordinal() + 1) % 4);
	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);

	//Aponta o sensor para frente novamente
	Motor.C.rotate(-_90GRAUS, true);
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

    /**
     * Move para a proxima posição do caminho
     */
    public void moverProximaPosicao() {
	int[] proximaPosicao = null;
	printPosicaoAtual();

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
	Motor.A.rotate(PASSO, true);
	Motor.B.rotate(PASSO);
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
	Motor.A.rotate((quantidadeGirar * _90GRAUS), true);
	Motor.B.rotate(-(quantidadeGirar * _90GRAUS));

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
	System.out.println("Posicao Atual: Coluna:" + this.coordenadaAtual[Matriz.COLUNA] + " Linha:"
		+ this.coordenadaAtual[Matriz.LINHA]);
	System.out.println("Lado Atual: " + this.ladoAtual.name());
    }
}
