package br.furb.robotica;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class RoboMapeador {

    private static final int VELOCIDADE = 600;
    private static final int DISTANCIA_OBSTACULO = 50;

    public static void main(String[] args) {
	System.out.println("Pressione ENTER");
	Button.ENTER.waitForPressAndRelease();
	Motor.A.setSpeed(VELOCIDADE);
	Motor.B.setSpeed(VELOCIDADE);

	MapaLabirinto mapa = new MapaLabirinto();
	mapa.setCoordenadaDestino(0, 0);
	mapa.setCoordenadaOrigem(3, 3);
	RoboMapeador robo = new RoboMapeador(mapa, Lado.FRENTE);

	Behavior analizarPosicao = new BehaviorAnalizarPosicao(robo);
	Behavior mover = new BehaviorMover(robo);
	Behavior mapeamentoCompleto = new BehaviorMapeamentoCompleto(robo);

	Behavior[] comportamentos = { analizarPosicao, mover, mapeamentoCompleto };
	Arbitrator arb = new Arbitrator(comportamentos);
	arb.start();
    }

    private int[] coordenadaAtual;
    private Lado ladoAtual;
    private MapaLabirinto mapa;
    private UltrasonicSensor sensor;

    public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual) {
	this.mapa = mapa;
	this.ladoAtual = ladoAtual;
	sensor = new UltrasonicSensor(SensorPort.S4);
    }

    public void analisarPosicao() {
	if (mapa.getInfoPosicao(coordenadaAtual) != null) {
	    //TODO: Apenas para testes... depois remover isso
	    throw new UnsupportedOperationException("analisarPosicao não deveria ter sido chamado");
	}

	InfoPosicao infoPosicao = mapa.criarPosicao(coordenadaAtual, ladoAtual);

	Motor.C.rotate(-90); //ESQUERDA DO ROBO
	Lado ladoSensor = Lado.valueOf((this.ladoAtual.ordinal() - 1) % 4);
	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);

	Motor.C.rotate(+90); //FRENTE DO ROBO
	ladoSensor = Lado.valueOf(this.ladoAtual.ordinal());
	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);

	Motor.C.rotate(+90); //ESQUERDA DO ROBO
	ladoSensor = Lado.valueOf((this.ladoAtual.ordinal() + 1) % 4);
	infoPosicao.setLadoLivre(ladoSensor, sensor.getDistance() < DISTANCIA_OBSTACULO);

	//Aponta o sensor para frente novamente
	Motor.C.rotate(90, true);
    }

    public InfoPosicao getPosicaoAtual() {
	return this.mapa.getInfoPosicao(this.coordenadaAtual);
    }

    public void moverParaPosicaoNaoVisitadao() {
	//TODO: Implementar
	//Analisar visinho não visitado
	//Se todos os visinhos foram visitados, verificar se existe alguma posição não visitada e ir até la
	//Se não tiver posição não visitada, fazer nada pois esse metodo não será executado
    }

    public boolean mapeamentoEstaCompleto() {
	//Da pra deixar assim, mas pode ocorre de existir uma posição inacessível
	return !mapa.existePosicaoNaoVisitada();
    }

}
