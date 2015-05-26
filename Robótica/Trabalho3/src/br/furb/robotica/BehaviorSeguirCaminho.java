package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

/**
 * Segue um caminho definido
 * 
 * @author Gustavo
 */
public class BehaviorSeguirCaminho implements Behavior {

    private RoboMapeador robo;

    public BehaviorSeguirCaminho(RoboMapeador robo) {
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return !robo.mapeamentoEstaCompleto() && robo.getCaminho() != null
		&& !robo.getCaminho().isAfterLast();
    }

    @Override
    public void action() {
	this.robo.moverProximaPosicao();
    }

    @Override
    public void suppress() {
    }

}
