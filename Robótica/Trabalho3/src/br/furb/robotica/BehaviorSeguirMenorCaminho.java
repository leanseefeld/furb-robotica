package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

/**
 * Se todo o mapa ja foi explorado, então esse comportamento será executado para
 * executar o menor caminho do inicio ao ponto de objetivo do mapa
 * 
 * @author Gustavo
 */
public class BehaviorSeguirMenorCaminho implements Behavior {

	private RoboMapeador robo;

	public BehaviorSeguirMenorCaminho(RoboMapeador robo) {
		this.robo = robo;
	}

	@Override
	public boolean takeControl() {
		return !robo.getCaminho().isAfterLast();
	}

	@Override
	public void action() {
		robo.moverProximaPosicao();
	}

	@Override
	public void suppress() {
	}

}
