package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

/**
 * Verifica se o mapa ja foi totalmente explorado. <br>
 * Ou seja, se o robo ja visitou todas as partes assessíveis do mapa
 * 
 * @author Gustavo
 */
public class BehaviorMontaMenorCaminho implements Behavior {

	private RoboMapeador robo;

	public BehaviorMontaMenorCaminho(RoboMapeador robo) {
		this.robo = robo;
	}

	@Override
	public boolean takeControl() {
		return robo.getCaminho() == null || robo.estaSobreOjetivo();
	}

	@Override
	public void action() {
		Caminho menorCaminho = this.robo.montarMenorCaminho();
		this.robo.setCaminho(menorCaminho);
	}

	@Override
	public void suppress() {

	}

}
