package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

public class BehaviorAnalizarPosicao implements Behavior {

    private RoboMapeador robo;

    public BehaviorAnalizarPosicao(RoboMapeador robo) {
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return false;
    }

    @Override
    public void action() {
	robo.analisarPosicao();
    }

    @Override
    public void suppress() {
    }

}
