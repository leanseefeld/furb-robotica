package br.furb.robotica;

import java.util.List;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

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
	mapa.setCoordenadaOrigem(3, 3);
	RoboMapeador robo = new RoboMapeador(mapa, Lado.FRENTE);

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

    public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual) {
	Motor.A.setSpeed(VELOCIDADE);
	Motor.B.setSpeed(VELOCIDADE);

	this.mapa = mapa;
	this.ladoAtual = ladoAtual;
	sensor = new UltrasonicSensor(SensorPort.S4);
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
	    //TODO: Apenas para testes... depois remover isso
	    throw new UnsupportedOperationException("analisarPosicao não deveria ter sido chamado");
	}

	InfoPosicao infoPosicao = mapa.criarPosicao(coordenadaAtual, ladoAtual);

	Motor.C.rotate(-_90GRAUS); //ESQUERDA DO ROBO
	Lado ladoSensor = Lado.valueOf((this.ladoAtual.ordinal() - 1) % 4);
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
    public void montarCaminhoAteProximaPosicao() {
	// TODO Auto-generated method stub
	int[] coordenadaVisinho = this.mapa.getVisinhoNaoVisitado(this.coordenadaAtual);

	//TODO: Fazer isso usando busca em profundidade... deve ficar mais facil
	if (coordenadaVisinho != null) {
	    this.caminho = new Caminho();
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
    }

    /**
     * Move para a proxima posição do caminho
     */
    public void moverProximaPosicao() {
	//TODO Implementar
	//Da pra utilizar a mesma lógica no trabalho 2
    }

    /**
     * Verifica se todo o mapa ja foi mapeado
     * 
     * @return
     */
    public boolean mapeamentoEstaCompleto() {
	return mapaCompleto;
    }
}
