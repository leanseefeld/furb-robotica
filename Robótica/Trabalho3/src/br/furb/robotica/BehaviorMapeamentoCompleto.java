package br.furb.robotica;

import lejos.robotics.subsumption.Behavior;

public class BehaviorMapeamentoCompleto implements Behavior {

    private RoboMapeador robo;

    public BehaviorMapeamentoCompleto(RoboMapeador robo) {
	super();
	this.robo = robo;
    }

    @Override
    public boolean takeControl() {
	return robo.mapeamentoEstaCompleto();
    }

    @Override
    public void action() {
	//TODO: mandar mensagem para o outro robo com o menor caminho
	//TODO: dar um jeito de finalizar essa bagaça
	System.out.println("FINALIZADO");
	try {
	    Thread.sleep(1000);//Pra n ficar num super loop
	} catch (Exception ex) {
	    System.out.println("Erro no thread.sleep");
	}
    }

    @Override
    public void suppress() {
	
    }

}
