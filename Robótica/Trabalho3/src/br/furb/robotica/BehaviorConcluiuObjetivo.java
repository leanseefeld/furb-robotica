package br.furb.robotica;

import br.furb.robotica.common.Coordenada;
import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

public class BehaviorConcluiuObjetivo implements Behavior {

    private RoboMapeador robo;

    public BehaviorConcluiuObjetivo(RoboMapeador robo) {
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return this.robo.getEstado() == Estado.SEGUINDO_MENOR_CAMINHO && this.robo.estaSobreOjetivo();
    }

    @Override
    public void action() {
	System.out.println("Objetivo alcançado");
	Button.ENTER.waitForPressAndRelease();
    }

    @Override
    public void suppress() {
	// TODO Auto-generated method stub

    }

}
