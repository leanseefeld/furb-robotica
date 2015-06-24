package br.furb.robotica.behavior;

import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.Robo;

/**
 * Se todo o mapa ja foi explorado, então esse comportamento será executado para executar o menor
 * caminho do inicio ao ponto de objetivo do mapa
 * 
 * @author Gustavo
 */
public class BehaviorSeguirMenorCaminho implements Behavior {

    private Robo robo;

    public BehaviorSeguirMenorCaminho(Robo robo) {
	this.robo = robo;
    }

    @Override
    public void action() {
	robo.moverProximaPosicao();
    }

    @Override
    public void suppress() {
    }

    @Override
    public boolean takeControl() {
	return !robo.getCaminho().hasMoreElements();
    }

}
