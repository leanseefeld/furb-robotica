package br.furb.robotica.behavior;

import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.Robo;

/**
 * Verifica se o mapa ja foi totalmente explorado. <br>
 * Ou seja, se o robo ja visitou todas as partes assessíveis do mapa
 * 
 * @author Gustavo
 */
public class BehaviorMontaMenorCaminho implements Behavior {

    private Robo robo;

    public BehaviorMontaMenorCaminho(Robo robo) {
	this.robo = robo;
    }

    @Override
    public void action() {
	this.robo.montarCaminhoAteProximaPosicao();
    }

    @Override
    public void suppress() {

    }

    @Override
    public boolean takeControl() {
	return robo.getCaminho() == null || robo.getCaminho().estaVazia();
    }

}
