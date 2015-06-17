package br.furb.robotica;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.behavior.BehaviorAnalizarPosicao;
import br.furb.robotica.behavior.BehaviorMontaMenorCaminho;
import br.furb.robotica.behavior.BehaviorMontarTrajeto;
import br.furb.robotica.behavior.BehaviorSeguirCaminho;
import br.furb.robotica.behavior.BehaviorSeguirMenorCaminho;

/**
 * Classe de inicialização do robô.
 */
public class Robo {

    private static final int VELOCIDADE = 500;
    private static final int _90GRAUS_RODAS = 400;

    private static final int COR_INTERSECCAO = Color.WHITE;
    private static final int COR_LINHA = Color.BLACK;
    private static final int DISTANCIA_INSPECAO = 100;

    public static void main(String[] args) {
	System.out.println("ENTER    -> Executa");
	System.out.println("[outro]  -> Depura");
	Debug.debug = Button.waitForAnyPress() != Button.ENTER.getId();

	Robo robo = new Robo();

	Behavior seguirTrajeto = new BehaviorSeguirCaminho(robo);
	Behavior montarTrajeto = new BehaviorMontarTrajeto(robo);
	Behavior analizarPosicao = new BehaviorAnalizarPosicao(robo);

	Behavior[] comportamentos = { seguirTrajeto, montarTrajeto, analizarPosicao };
	Arbitrator arb = new Arbitrator(comportamentos, true);
	arb.start();

	System.out.println("O mapeamento está completo");

	if (mapa.getCoordenadaDestino() == null) {
	    System.out.println("Destino não encontrado.");
	} else {
	    System.out.println("Pressione ENTER para seguir o menor caminho");
	    Button.ENTER.waitForPressAndRelease();

	    robo.moveRoboParaPontoInicial();

	    Behavior mapeamentoMontaMenorCaminho = new BehaviorMontaMenorCaminho(robo);
	    Behavior seguirMenorCaminho = new BehaviorSeguirMenorCaminho(robo);

	    comportamentos = new Behavior[] { seguirMenorCaminho, mapeamentoMontaMenorCaminho };
	    arb = new Arbitrator(comportamentos);
	    arb.start();

	    System.out.println("Objetivo alcançado");
	}
	Button.ENTER.waitForPressAndRelease();
	System.out.println("FIM");
    }

    private Lado ladoAtual;
    private final GerenciadorNos gerenciadorNos;
    private Caminho caminho;
    private boolean mapaCompleto;
    private Stack<No> nosNaoVizitados;
    private ColorSensor colorSensor;

    private No noAtual;

    public Robo() {
	Motor.A.setSpeed(VELOCIDADE);
	Motor.B.setSpeed(VELOCIDADE);

	this.nosNaoVizitados = new Stack<>();
	this.colorSensor = new ColorSensor(SensorPort.S3);
	this.colorSensor.setFloodlight(true);

	this.gerenciadorNos = new GerenciadorNos();
	this.noAtual = new No(0, 0);
	this.gerenciadorNos.salvarNo(this.noAtual);
    }

    /**
     * Pega informações da posição atual do robo
     */
    public void analisarPosicao() {
	if (noAtual.isVisitado()) {
	    return;
	}
	List<Lado> ladosAExplorar = new ArrayList<>(4);
	for (Lado lado : Lado.values()) {
	    No vizinho = noAtual.getVizinho(lado);
	    if (vizinho == null) {
		No vizinhoNaoConexo = gerenciadorNos.getVizinho(noAtual, lado, false);
		if (vizinhoNaoConexo == null || !vizinhoNaoConexo.isVisitado()) {
		    ladosAExplorar.add(lado);
		}
	    }
	}
	for (Lado lado : ladosAExplorar) {
	    virarPara(lado);
	    avancar(DISTANCIA_INSPECAO);
	    if (estaSobreLinha()) {
		No vizinho = gerenciadorNos.getVizinho(noAtual, lado, true);
		nosNaoVizitados.push(vizinho);
	    }
	    retroceder(DISTANCIA_INSPECAO);
	}
	noAtual.setVisitado(true);
    }

    public boolean analisouPosicao() {
	return getPosicaoAtual() != null;
    }

    /**
     * Verifica se o robo está sobre a coordenada objetivo
     * 
     * @return
     */
    public boolean estaSobreOjetivo() {
	return Coordenada.equals(this.coordenadaAtual, this.mapa.getCoordenadaDestino());
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
     * Verifica se todo o mapa ja foi mapeado
     * 
     * @return
     */
    public boolean mapeamentoEstaCompleto() {
	return mapaCompleto;
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
		nosNaoVizitados.push(coordenadasVisinhas[i]);
	    }
	} else {
	    int[] coord = null;
	    while ((coord = nosNaoVizitados.pop()) != null) {
		Debug.print(Coordenada.toString(coord));
		if (mapa.getInfoPosicao(coord) == null) {
		    Debug.print("Escolhida!");
		    caminho = mapa.montarCaminhoDijkstra(coordenadaAtual, coord);
		    Debug.print(caminho.toString());
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
     * Monta o menor caminho da posição atual até o destino
     * 
     * @return
     */
    public Caminho montarMenorCaminho() {
	Caminho caminho = mapa.montarCaminhoDijkstra(this.coordenadaAtual, this.mapa.getCoordenadaDestino());
	caminho.nextElement();
	return caminho;
    }

    /**
     * Atualiza as coordenadas do robo para a posição inicial
     */
    public void moveRoboParaPontoInicial() {
	this.coordenadaAtual = this.coordenadaInicial;
	this.ladoAtual = this.ladoInicial;
    }

    /**
     * Move para a proxima posição do caminho
     */
    public void moverProximaPosicao() {
	int[] proximaPosicao = null;
	printPosicaoAtual();

	proximaPosicao = caminho.nextElement();
	Lado novoSentido = mapa.getLado(this.coordenadaAtual, proximaPosicao);
	virarPara(novoSentido);
	moverEmFrente();
	this.ladoAtual = novoSentido;
	this.coordenadaAtual = proximaPosicao;
	printPosicaoAtual();
    }

    /**
     * Exibe a posição e o lado atual do robo
     */
    public void printPosicaoAtual() {
	Debug.println("Posicao Atual: Coluna:" + this.coordenadaAtual[Matriz.COLUNA] + " Linha:"
		+ this.coordenadaAtual[Matriz.LINHA]);
	Debug.println("Lado Atual: " + this.ladoAtual.name());
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
     * Gira o robo no para o lado desejado
     * 
     * @param ladoDestino
     *            lado para o qual virar
     */
    private void virarPara(Lado ladoDestino) {
	int quantidadeGirar = ladoDestino.ordinal() - this.ladoAtual.ordinal();
	if (quantidadeGirar == 0) {
	    return;
	}

	// Se for 3, muda para 1 e muda a direção da rotação
	// se for 4, fica parado
	if (Math.abs(quantidadeGirar) > 2) {
	    quantidadeGirar = -(quantidadeGirar % 2);
	}

	// Obs: Se quantidadeGirar for positiva, irá girar para direita
	// se for negativa, irá ser para a esquerda
	Motor.A.rotate(-(quantidadeGirar * _90GRAUS_RODAS));
	Motor.B.rotate((quantidadeGirar * _90GRAUS_RODAS), true);

	if (this.ladoAtual != ladoDestino) {
	    Debug.print("Novo sentido " + ladoDestino.name());
	    if (quantidadeGirar > 0) {
		Debug.println(" - GIROU " + (quantidadeGirar * 90) + "º à direita");
	    } else {
		Debug.println(" - GIROU " + (quantidadeGirar * 90 * -1) + "º à esquerda");
	    }
	    this.ladoAtual = ladoDestino;
	}
    }

    /**
     * Move o robo um passo para frente
     * 
     * @param passo
     *            distancia a mover
     */
    public void avancar(int passo) {
	Debug.println("Moveu: " + passo);
	Motor.A.rotate(passo, true);
	Motor.B.rotate(passo);
    }

    /**
     * O mesmo que {@code avancar(-passo)}.
     * 
     * @param passo
     *            distancia a mover
     * @see #avancar(int)
     */
    public void retroceder(int passo) {
	avancar(-passo);
    }

    public boolean estaSobreInterseccao() {
	return colorSensor.getColor().getColor() == COR_INTERSECCAO;
    }

    public boolean estaSobreLinha() {
	if (colorSensor.getColorID() == COR_LINHA) {
	    return true;
	}
	Motor.A.rotate(DISTANCIA_INSPECAO);
	boolean estaSobreLinha = colorSensor.getColorID() == COR_LINHA;
	Motor.A.rotate(-DISTANCIA_INSPECAO);
	if (!estaSobreLinha) {
	    Motor.B.rotate(DISTANCIA_INSPECAO);
	    estaSobreLinha = colorSensor.getColorID() == COR_LINHA;
	    Motor.B.rotate(-DISTANCIA_INSPECAO);
	}
	return estaSobreLinha;
    }

}
