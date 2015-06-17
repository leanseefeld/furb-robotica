package br.furb.robotica.behavior;

import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.Debug;
import br.furb.robotica.Robo;

/**
 * Segue um caminho definido
 */
public class BehaviorSeguirCaminho implements Behavior {

    private Robo robo;

    public BehaviorSeguirCaminho(Robo robo) {
	this.robo = robo;
    }

    @Override
    public void action() {
	Debug.step("S.action");
	this.robo.moverProximaPosicao();
    }

    @Override
    public void suppress() {
    }

    @Override
    public boolean takeControl() {
	Debug.println("S.takeControl");
	Debug.step("Com. " + robo.mapeamentoEstaCompleto());
	return !robo.mapeamentoEstaCompleto() && robo.getCaminho() != null && !robo.getCaminho().isAfterLast();
    }

}