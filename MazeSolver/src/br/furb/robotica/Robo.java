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
import util.Pilha;
import br.furb.robotica.behavior.BehaviorAnalizarPosicao;
import br.furb.robotica.behavior.BehaviorMontaMenorCaminho;
import br.furb.robotica.behavior.BehaviorMontarTrajeto;
import br.furb.robotica.behavior.BehaviorSeguirCaminho;

/**
 * Classe de inicialização do robô.
 */
public class Robo {

    private static final int VELOCIDADE = 500;
    private static final int _90GRAUS_RODAS = 400;

    private static final int COR_INTERSECCAO = Color.WHITE;
    private static final int COR_LINHA = Color.BLACK;
    private static final int DISTANCIA_INSPECAO = 100;
    private static final int COR_OBJETIVO = Color.WHITE;

    private Sentido sentidoAtual;
    private final GerenciadorNos gerenciadorNos;
    private Pilha<Passo> caminho;
    private Stack<No> nosNaoVisitados;
    private ColorSensor colorSensor;
    private No noDestino;
    private No noAtual;
    private final No noInicial;
    private final Sentido ladoInicial;

    public static void main(String[] args) {
	System.out.println("ENTER    -> Executa");
	System.out.println("[outro]  -> Depura");
	Debug.debug = Button.waitForAnyPress() != Button.ENTER.getId();

	Robo robo = new Robo();

	Behavior seguirCaminho = new BehaviorSeguirCaminho(robo);
	Behavior montarCaminho = new BehaviorMontarTrajeto(robo);
	Behavior analizarPosicao = new BehaviorAnalizarPosicao(robo);

	Behavior[] comportamentos = { seguirCaminho, montarCaminho, analizarPosicao };
	Arbitrator arb = new Arbitrator(comportamentos, true);
	arb.start();

	System.out.println("O mapeamento está completo");

	if (robo.getDestino() == null) {
	    System.out.println("Destino não encontrado.");
	} else {
	    System.out.println("Pressione ENTER para seguir o menor caminho");
	    Button.ENTER.waitForPressAndRelease();

	    robo.moveRoboParaPontoInicial();

	    Behavior mapeamentoMontaMenorCaminho = new BehaviorMontaMenorCaminho(robo);
	    montarCaminho = new BehaviorSeguirCaminho(robo);
	    //	    Behavior seguirMenorCaminho = new BehaviorSeguirMenorCaminho(robo);

	    comportamentos = new Behavior[] { montarCaminho, mapeamentoMontaMenorCaminho };
	    arb = new Arbitrator(comportamentos);
	    arb.start();

	    System.out.println("Objetivo alcançado");
	}
	Button.ENTER.waitForPressAndRelease();
	System.out.println("FIM");
    }

    public No getDestino() {
	return this.noDestino;
    }

    public Robo() {
	Motor.A.setSpeed(VELOCIDADE);
	Motor.B.setSpeed(VELOCIDADE);

	this.nosNaoVisitados = new Stack<>();
	this.colorSensor = new ColorSensor(SensorPort.S3);
	this.colorSensor.setFloodlight(true);

	this.gerenciadorNos = new GerenciadorNos();
	this.noAtual = new No(0, 0);
	this.gerenciadorNos.salvarNo(this.noAtual);

	this.noInicial = noAtual;
	this.nosNaoVisitados.push(this.noInicial);
	this.ladoInicial = this.sentidoAtual;
    }

    /**
     * Pega informações da posição atual do robo
     */
    public void analisarPosicao() {
	if (noAtual.isVisitado()) {
	    return;
	}
	Sentido sentidoOriginal = this.sentidoAtual;

	List<Sentido> ladosAExplorar = new ArrayList<>(4);
	for (Sentido lado : Sentido.values()) {
	    No vizinho = noAtual.getVizinho(lado);
	    if (vizinho == null) {
		// No vizinhoNaoConexo = gerenciadorNos.getVizinho(noAtual, lado, false);
		// if (vizinhoNaoConexo == null || !vizinhoNaoConexo.isVisitado()) {
		ladosAExplorar.add(lado);
		// }
	    }
	}
	boolean estahSobreObjetivo = true;
	for (Sentido sentido : ladosAExplorar) {
	    virarPara(sentido);
	    avancar(DISTANCIA_INSPECAO);
	    if (estaSobreLinha()) {
		No vizinho = gerenciadorNos.getVizinho(noAtual, sentido, true);
		nosNaoVisitados.push(vizinho);
	    } else if (sentidoAtual != sentidoOriginal) {
		estahSobreObjetivo &= estaSobreObjetivo();
	    }
	    retroceder(DISTANCIA_INSPECAO);
	}
	noAtual.setVisitado(true);
	if (estahSobreObjetivo) {
	    noDestino = noAtual;
	}
    }

    public boolean analisouPosicao() {
	return getPosicaoAtual() != null;
    }

    /**
     * @return Retorna o caminho que o robo deve seguir
     */
    public Pilha<Passo> getCaminho() {
	return this.caminho;
    }

    public boolean estaSeguindoCaminho() {
	return caminho != null && !caminho.estaVazia();
    }

    /**
     * @return Retorna a posição atual do robo no mapa
     */
    public No getPosicaoAtual() {
	return this.noAtual;
	//	return this.mapa.getInfoPosicao(this.coordenadaAtual);
    }

    /**
     * Verifica se todo o mapa ja foi mapeado
     * 
     * @return
     */
    public boolean mapeamentoEstaCompleto() {
	return this.nosNaoVisitados.isEmpty();
    }

    /**
     * Monta um caminho para uma posição desejada para que o robo siga
     */
    public Pilha<Passo> montarCaminhoAteProximaPosicao() {
	No noNaoVisitado = nosNaoVisitados.pop();
	this.caminho = BuscaLargura.getMenorCaminho(noAtual, noNaoVisitado);
	return this.caminho;
    }

    /**
     * Atualiza as coordenadas do robo para a posição inicial
     */
    public void moveRoboParaPontoInicial() {
	this.noAtual = this.noInicial;
	this.sentidoAtual = this.ladoInicial;
    }

    /**
     * Move para a proxima posição do caminho
     */
    public void moverProximaPosicao() {
	Passo proximaPosicao = null;
	printPosicaoAtual();

	proximaPosicao = caminho.pegar();
	Sentido novoSentido = proximaPosicao.getSentidoOrigem();
	virarPara(novoSentido);
	seguirLinha();
    }

    private void seguirLinha() {
	while (!estaSobreInterseccao()) {
	    // segue a linha preta
	}
    }

    /**
     * Exibe a posição e o lado atual do robo
     */
    public void printPosicaoAtual() {
	Debug.println("Posicao Atual: " + this.noAtual.toString());
	Debug.println("Lado Atual: " + this.sentidoAtual.name());
    }

    /**
     * Gira o robo no para o lado desejado
     * 
     * @param ladoDestino
     *            lado para o qual virar
     */
    private void virarPara(Sentido ladoDestino) {
	int quantidadeGirar = ladoDestino.ordinal() - this.sentidoAtual.ordinal();
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

	if (this.sentidoAtual != ladoDestino) {
	    Debug.print("Novo sentido " + ladoDestino.name());
	    if (quantidadeGirar > 0) {
		Debug.println(" - GIROU " + (quantidadeGirar * 90) + "º à direita");
	    } else {
		Debug.println(" - GIROU " + (quantidadeGirar * 90 * -1) + "º à esquerda");
	    }
	    this.sentidoAtual = ladoDestino;
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

    private boolean estaSobreObjetivo() {
	return colorSensor.getColor().getColor() == COR_OBJETIVO;
    }

}
