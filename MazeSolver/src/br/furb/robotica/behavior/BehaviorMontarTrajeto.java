package br.furb.robotica.behavior;

import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.Debug;
import br.furb.robotica.Robo;

/**
 * Monta um caminho para explorar algum local ainda não explorado
 * 
 * @author Gustavo
 */
public class BehaviorMontarTrajeto implements Behavior {

    private Robo robo;

    public BehaviorMontarTrajeto(Robo robo) {
	this.robo = robo;
    }

    @Override
    public void action() {
	robo.montarCaminhoAteProximaPosicao();
	//	Debug.step("M.action: caminhoNull? " + (caminho == null));
    }

    @Override
    public void suppress() {

    }

    @Override
    public boolean takeControl() {
	Debug.step("M.takeControl");
	return !(robo.mapeamentoEstaCompleto() || robo.estaSeguindoCaminho());
    }

}
