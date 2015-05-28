package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

/**
 * Monta um caminho para explorar algum local ainda não explorado
 * 
 * @author Gustavo
 */
public class BehaviorMontarCaminhoExploracao implements Behavior {

	private RoboMapeador robo;

	public BehaviorMontarCaminhoExploracao(RoboMapeador robo) {
		this.robo = robo;
	}

	@Override
	public boolean takeControl() {
		Debug.step("M.takeControl");
		return !robo.mapeamentoEstaCompleto()
				&& (robo.getCaminho() == null || robo.getCaminho()
						.isAfterLast());
	}

	@Override
	public void action() {
		Caminho caminho = robo.montarCaminhoAteProximaPosicao();
		Debug.step("M.action: caminhoNull? " + (caminho == null));
		robo.setCaminho(caminho);
	}

	@Override
	public void suppress() {

	}

}
