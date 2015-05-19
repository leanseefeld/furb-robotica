package br.furb.robotica;

import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

public class BehaviorSeguirTrajeto implements Behavior {

    private RoboMapeador robo;

    public BehaviorSeguirTrajeto(RoboMapeador robo) {
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return robo.getCaminho() != null && !robo.getCaminho().isAfterLast();
    }

    @Override
    public void action() {
	this.robo.moverProximaPosicao();
    }

    @Override
    public void suppress() {
	Motor.A.flt();
	Motor.B.flt();
    }

}
