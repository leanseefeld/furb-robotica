package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

/**
 * Pega informações da posição atual do robo
 * 
 * @author Gustavo
 * 
 */
public class BehaviorAnalizarPosicao implements Behavior {

	private RoboMapeador robo;

	public BehaviorAnalizarPosicao(RoboMapeador robo) {
		this.robo = robo;
	}

	@Override
	public boolean takeControl() {
		Debug.step("A.takeControl");
		return this.robo.getPosicaoAtual() == null;
	}

	@Override
	public void action() {
		Debug.step("A.action");
		robo.analisarPosicao();
	}

	@Override
	public void suppress() {
	}

}
