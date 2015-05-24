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
	return robo.getEstado() == Estado.EXPLORANDO_MAPA
		&& (robo.getCaminho() == null || robo.getCaminho().isAfterLast());
    }

    @Override
    public void action() {
	robo.setCaminho(robo.montarCaminhoAteProximaPosicao());
    }

    @Override
    public void suppress() {

    }

}
