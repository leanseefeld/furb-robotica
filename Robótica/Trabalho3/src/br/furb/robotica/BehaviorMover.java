package br.furb.robotica;

import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

public class BehaviorMover implements Behavior {

    private RoboMapeador robo;

    public BehaviorMover(RoboMapeador robo) {
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return robo.getPosicaoAtual() != null;
    }

    @Override
    public void action() {
	Motor.A.forward();
	Motor.B.forward();
	this.robo.moverParaPosicaoNaoVisitadao();
    }

    @Override
    public void suppress() {
	Motor.A.flt();
	Motor.B.flt();
    }

}
