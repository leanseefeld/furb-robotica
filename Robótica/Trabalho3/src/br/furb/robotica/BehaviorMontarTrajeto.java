package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

public class BehaviorMontarTrajeto implements Behavior {

    private RoboMapeador robo;

    public BehaviorMontarTrajeto(RoboMapeador robo) {
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	if (robo.getCaminho() == null || robo.getCaminho().isAfterLast()) {
	    return true;
	}
	return false;
    }

    @Override
    public void action() {
	robo.setCaminho(robo.montarCaminhoAteProximaPosicao());
    }

    @Override
    public void suppress() {

    }

}
