package br.furb.robotica;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;
import util.Pilha;

/**
 * Classe de inicialização do robô.
 */
public class Robo {

    private static final int PASSOS_ANALISE_LINHA = 4;
    private static final int ROTACAO_ANALISE_LINHA = 30;
    private static final int VELOCIDADE = 200;
    private static final int _90GRAUS_RODAS = 250;

    private static final int DISTANCIA_INSPECAO = 100;
    private static final int DISTANCIA_AJUSTE = 10;
    private static final int DISTANCIA_PASSO = 40;
    // TODO: Não pode ser branca pq senão vai confundir com o fundo (fora da linha)
    private static final int COR_OBJETIVO = Color.RED;
    private static final int COR_LINHA = Color.BLACK;

    private Sentido sentidoAtual;
    private final GerenciadorNos gerenciadorNos;
    private Pilha<Passo> caminho;
    private final Stack<No> nosNaoVisitados;
    private final ColorSensor colorSensorDireito = new ColorSensor(SensorPort.S3);
    private final ColorSensor colorSensorEsquerdo = new ColorSensor(SensorPort.S4);
    private final NXTRegulatedMotor motorDireito = Motor.B;
    private final NXTRegulatedMotor motorEsquerdo = Motor.A;
    private No noDestino;
    private No noAtual;
    private final No noInicial;
    private final Sentido sentidoInicial;

    private boolean moveu = true;
    private int ultimaCorDireita;
    private int ultimaCorEsquerda;

    public static void main(String[] args) {
	try {
	    System.out.println("ENTER    -> Executa");
	    System.out.println("[outro]  -> Depura");
	    Debug.debug = Button.waitForAnyPress() != Button.ENTER.getId();

	    Robo robo = new Robo();
	    robo.analisarPosicao();
	    Debug.debug = false;

	    boolean mapeamentoEstaCompleto = false;
	    while (!mapeamentoEstaCompleto) {
		Debug.step("a");
		if (robo.estaSobreInterseccao()) {
		    Debug.step("b");
		    robo.analisarPosicao();
		}
		
		Debug.step("c");
		mapeamentoEstaCompleto = robo.mapeamentoEstaCompleto();
		Debug.step("d");
		boolean estaSeguindoCaminho = robo.estaSeguindoCaminho();
		Debug.step("e");

		if (!mapeamentoEstaCompleto && !estaSeguindoCaminho) {
		    Debug.step("f");

		    robo.montarCaminhoAteProximaPosicao();
		    Debug.step("g");
		    robo.moverProximaPosicao();
		    Debug.step("h");

		} else if (estaSeguindoCaminho) {
		    Debug.step("i");
		    robo.moverProximaPosicao();
		}
		Debug.step("j");
	    }
	    Debug.step("k");

	    System.out.println("O mapeamento está completo");

	    if (robo.getDestino() == null) {
		System.out.println("Destino não encontrado.");
	    } else {
		System.out.println("Pressione ENTER para ir a posicao original");
		Button.ENTER.waitForPressAndRelease();

		robo.moveParaInicio();

		System.out.println("Pressione ENTER para seguir o menor caminho");
		Button.ENTER.waitForPressAndRelease();

		robo.moverParaDestino();

		System.out.println("Objetivo alcançado");
	    }
	    Button.ENTER.waitForPressAndRelease();
	    System.out.println("FIM");
	} catch (Throwable t) {
	    Debug.throwUp();
	    throw t;
	}
    }

    public No getDestino() {
	return this.noDestino;
    }

    public Robo() {
	Motor.A.setSpeed(VELOCIDADE);
	Motor.B.setSpeed(VELOCIDADE);

	this.nosNaoVisitados = new Stack<>();
	this.colorSensorDireito.setFloodlight(true);
	this.colorSensorEsquerdo.setFloodlight(true);

	this.gerenciadorNos = new GerenciadorNos();
	this.noAtual = new No(0, 0);
	this.gerenciadorNos.salvarNo(this.noAtual);

	this.noInicial = noAtual;
	this.nosNaoVisitados.push(this.noInicial);
	this.sentidoAtual = Sentido.NORTE;
	this.sentidoInicial = this.sentidoAtual;
    }

    /**
     * Pega informações da posição atual do robô.
     */
    public void analisarPosicao() {
	Debug.debug = false;
	Debug.step("a1");
	if (noAtual.isVisitado()) {
	    Debug.step("b1");
	    return;
	}
	Debug.step("c1");
	noAtual.setVisitado(true);

	Debug.step("d1");
	if (estaSobreObjetivo()) {
	    Debug.step("Robo:125");
	    this.noDestino = this.noAtual;
	    return;
	}
	Debug.step("e1");

	List<Sentido> ladosAExplorar = new ArrayList<>(4);
	Debug.step("f1");
	for (Sentido sentido : Sentido.values()) {
	    Debug.step("g1:" + sentido);
	    No vizinho = noAtual.getVizinho(sentido);
	    Debug.step("h1");
	    if (vizinho == null) {
		Debug.step("i1");
		No vizinhoNaoConexo = gerenciadorNos.getVizinho(noAtual, sentido, false);
		Debug.step("j1:" + vizinhoNaoConexo);
		if (vizinhoNaoConexo == null || !vizinhoNaoConexo.isVisitado()) {
		    Debug.step("k1");
		    ladosAExplorar.add(sentido);
		}
	    }
	}
	Debug.debug = true;
	Debug.step("l1");
	for (Sentido sentido : ladosAExplorar) {
	    Debug.step("m1");
	    virarPara(sentido);
	    Debug.step("n1");
	    this.sentidoAtual = sentido;
	    avancar(DISTANCIA_INSPECAO);
	    Debug.step("o1");
	    if (estaSobreLinha()) {
		Debug.step("p1");
		No vizinho = gerenciadorNos.getVizinho(noAtual, sentido, true);
		nosNaoVisitados.push(vizinho);
	    }
	    Debug.step("q1");
	    retroceder(DISTANCIA_INSPECAO);
	}
	Debug.step("r1");
	Debug.toggle();
    }

    /**
     * Retorna o lado que se encontra o sentido desejado em relação ao sentido atual
     * 
     * @param sentidoAtual
     * @param sentidoDesejado
     * @return
     */
    private Lado getLado(Sentido sentidoAtual, Sentido sentidoDesejado) {
	Debug.step("f1,atual=" + sentidoAtual + ",desejado=" + sentidoDesejado);
	int quantidadeGirar = sentidoAtual.ordinal() - sentidoDesejado.ordinal();
	Lado lado = null;

	if (Math.abs(quantidadeGirar) > 2) {
	    quantidadeGirar = -(quantidadeGirar % 2);
	}

	switch (quantidadeGirar) {
	    case 0:
		lado = Lado.FRENTE;
		break;
	    case -1:
		lado = Lado.DIREITA;
		break;
	    case 1:
		lado = Lado.ESQUERDA;
		break;
	    case 2:
	    case -2:
		lado = Lado.ATRAS;
		break;
	    default:
		System.out.println("Erro ao girar de " + sentidoAtual.name() + " para " + sentidoDesejado.name());
	}
	return lado;
    }

    /**
     * Gira para a esquerda ou direita um quantidade
     * 
     * @param lado
     * @param qtd
     */
    private void girar(Lado lado, int qtd) {
	moveu = true;
	if (lado == Lado.DIREITA) {
	    motorDireito.rotate(-qtd, true);
	    motorEsquerdo.rotate(+qtd);
	} else if (lado == Lado.ESQUERDA) {
	    motorDireito.rotate(+qtd, true);
	    motorEsquerdo.rotate(-qtd);
	} else {
	    System.out.println("Não é possível girar para o lado " + lado.name());
	}
    }

    private boolean existeSensorSobreLinha() {
	analisarCores();
	return ultimaCorDireita == COR_LINHA || ultimaCorEsquerda == COR_LINHA;
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
     * Move o robô para a posição e sentido originais, seguindo o menor caminho.
     */
    private void moveParaInicio() {
	moverPeloMenorCaminho(noInicial);
    }

    /**
     * Move o robô para a posição destino, seguindo o menor caminho.
     */
    private void moverParaDestino() {
	moverPeloMenorCaminho(noDestino);
    }

    private void moverPeloMenorCaminho(No objetivo) {
	this.caminho = BuscaLargura.getMenorCaminho(noAtual, noInicial);
	while (estaSeguindoCaminho()) {
	    moverProximaPosicao();
	}
	virarPara(sentidoInicial);
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
	this.noAtual = proximaPosicao.getNo();
    }

    private void seguirLinha() {
	while (!estaSobreInterseccao() && !estaSobreObjetivo()) {
	    if (ultimaCorEsquerda == COR_LINHA) {
		motorDireito.rotate(DISTANCIA_AJUSTE);
	    } else if (ultimaCorDireita == COR_LINHA) {
		motorEsquerdo.rotate(DISTANCIA_AJUSTE);
	    }
	    avancar(DISTANCIA_PASSO);
	}
    }

    /**
     * Exibe a posição e o lado atual do robo
     */
    public void printPosicaoAtual() {
	Debug.println("pa: " + this.noAtual.toString());
	Debug.println("la: " + this.sentidoAtual.name());
    }

    /**
     * Gira o robo no para o lado desejado
     * 
     * @param sentidoDestino
     *            lado para o qual virar
     */
    private void virarPara(Sentido sentidoDestino) {
	Debug.step("a5");
	Lado lado = getLado(sentidoAtual, sentidoDestino);
	Debug.step("b5");

	//Se girar pra frente, não faz nada
	if (lado == Lado.FRENTE) {
	    Debug.step("c5");
	    return;
	}

	//Se girar para traz, gira duas vezes para a direita e assim faz 180º
	//Se não, gira para o lado desejado
	if (lado == Lado.ATRAS) {
	    girar(Lado.DIREITA, _90GRAUS_RODAS * 2);
	} else {
	    girar(lado, _90GRAUS_RODAS);
	}
	this.sentidoAtual = sentidoDestino;
	Debug.step("d5");

	//	int quantidadeGirar = ladoDestino.ordinal() - this.sentidoAtual.ordinal();
	//	if (quantidadeGirar == 0) {
	//	    return;
	//	}
	//
	//	// Se for 3, muda para 1 e muda a direção da rotação
	//	// se for 4, fica parado
	//	if (Math.abs(quantidadeGirar) > 2) {
	//	    quantidadeGirar = -(quantidadeGirar % 2);
	//	}
	//
	//	// Obs: Se quantidadeGirar for positiva, irá girar para direita
	//	// se for negativa, irá ser para a esquerda
	//	Motor.A.rotate(-(quantidadeGirar * _90GRAUS_RODAS));
	//	Motor.B.rotate((quantidadeGirar * _90GRAUS_RODAS), true);
	//
	//	if (this.sentidoAtual != ladoDestino) {
	//	    Debug.print("Novo sentido " + ladoDestino.name());
	//	    if (quantidadeGirar > 0) {
	//		Debug.println(" - GIROU " + (quantidadeGirar * 90) + "º à direita");
	//	    } else {
	//		Debug.println(" - GIROU " + (quantidadeGirar * 90 * -1) + "º à esquerda");
	//	    }
	//	    this.sentidoAtual = ladoDestino;
	//	}
    }

    /**
     * Move o robo um passo para frente
     * 
     * @param passo
     *            distancia a mover
     */
    public void avancar(int passo) {
	moveu = true;
	Debug.println("Moveu: " + passo);
	motorDireito.rotate(passo, true);
	motorEsquerdo.rotate(passo);
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
	Debug.step("cores");
	analisarCores();
	return ultimaCorDireita == COR_LINHA && ultimaCorEsquerda == COR_LINHA;
    }

    public boolean estaSobreLinha() {
	boolean encontrouLinha = false;
	int quantidadeGirar;
	for (quantidadeGirar = 0; quantidadeGirar < PASSOS_ANALISE_LINHA; quantidadeGirar++) {
	    girar(Lado.DIREITA, ROTACAO_ANALISE_LINHA);
	    if (existeSensorSobreLinha()) {
		encontrouLinha = true;
		break;
	    }
	}
	for (; quantidadeGirar >= 0; quantidadeGirar--) {
	    girar(Lado.ESQUERDA, ROTACAO_ANALISE_LINHA);
	}
	return encontrouLinha;

	//	if (colorSensor.getColorID() == COR_LINHA) {
	//	    return true;
	//	}
	//	Motor.A.rotate(DISTANCIA_INSPECAO);
	//	boolean estaSobreLinha = colorSensor.getColorID() == COR_LINHA;
	//	Motor.A.rotate(-DISTANCIA_INSPECAO);
	//	if (!estaSobreLinha) {
	//	    Motor.B.rotate(DISTANCIA_INSPECAO);
	//	    estaSobreLinha = colorSensor.getColorID() == COR_LINHA;
	//	    Motor.B.rotate(-DISTANCIA_INSPECAO);
	//	}
	//	return estaSobreLinha;
    }

    private boolean estaSobreObjetivo() {
	analisarCores();
	return ultimaCorDireita == COR_OBJETIVO && ultimaCorEsquerda == COR_OBJETIVO;
    }

    private void analisarCores() {
	if (moveu) {
	    moveu = false;
	    ultimaCorDireita = colorSensorDireito.getColorID();
	    ultimaCorEsquerda = colorSensorEsquerdo.getColorID();
	}
    }

}
