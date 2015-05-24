package br.furb.robotica;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

/**
 * Verifica se o mapa ja foi totalmente explorado. <br>
 * Ou seja, se o robo ja visitou todas as partes assessíveis do mapa
 * 
 * @author Gustavo
 */
public class BehaviorMapeamentoCompleto implements Behavior {

    private RoboMapeador robo;

    public BehaviorMapeamentoCompleto(RoboMapeador robo) {
	super();
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return robo.getEstado() == Estado.EXPLORANDO_MAPA && robo.mapeamentoEstaCompleto();
    }

    @Override
    public void action() {
	System.out.println("O mapeamento está completo");
	System.out.println("Pressione enter para seguir o menor caminho");
	Button.ENTER.waitForPressAndRelease();

	this.robo.setEstado(Estado.SEGUINDO_MENOR_CAMINHO);
	this.robo.moveRoboParaPontoInicial();
	Caminho menorCaminho = this.robo.montarMenorCaminho();
	this.robo.setCaminho(menorCaminho);
    }

    @Override
    public void suppress() {

    }

}
