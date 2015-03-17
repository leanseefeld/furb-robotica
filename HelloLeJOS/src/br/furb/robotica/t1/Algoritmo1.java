package br.furb.robotica.t1;
import br.furb.robotica.t1.commons.Direcao;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Algoritmo1 {

	/*
	 * Objetivo do algoritmo: Fazer com que o robô ande na extremide interna da
	 * fita preta
	 * 
	 * Para fazer isso o robo irá verificar se onde ele está é preto se for
	 * preto ele vai para a direta. Para isso ele irá travar uma das rodas e
	 * manter a outra rodando se for branco ele vai para a esquerda. Para isso,
	 * ele irá reduzir gradativamente a velocidade de uma das rodas até parar
	 * 
	 * Toda vez q mudar de estado, as velocidades das rodas recebem o valor maximo padrão
	 */

	private int MAX_ESPEED = 600;
	private int MAX_SPEED_WHITE = 200;
	private int REDUCAO_POR_CICLO = 5;
	private int BLACK = 400;
	private ColorSensor colorSensor;
	private int estado = 1;
	private int count = 0;

	public static void main(String[] args) throws InterruptedException {
		Algoritmo1 follower = new Algoritmo1();

		System.out.println("Ready to go! Press the main button");
		Button.ENTER.waitForPressAndRelease();

		follower.run();
	}
	
	public Algoritmo1()
	{
		colorSensor = new ColorSensor(SensorPort.S1);
	}
	
	public void run() throws InterruptedException {
		colorSensor.setFloodlight(true);
		aplicarVelocidadePadrao();
		while (true) {
			System.out.println("Estado: " + estado);
			switch (estado) {
			case 1:
				if (isBranco()) {
					estado = 4;
				} else {
					estado = 2;
				}
				break;
			case 2:
				direita();
				if (isBranco()) {
					estado = 3;
					aplicarVelocidadePadrao();
				}
				break;
			case 3:
				esquerda();
				if (isPreto()) {
					estado = 2;
					aplicarVelocidadePadrao();
				}
				break;
			case 4:
				direita();
				if (isPreto()) {
					estado = 2;
					aplicarVelocidadePadrao();
				}
				break;
			}
		}
	}

	public void direita() throws InterruptedException {
		girar(Direcao.DIREITA);
	}

	public void esquerda() throws InterruptedException {
		girar(Direcao.ESQUERDA);
	}

	private void girar(Direcao direcao) throws InterruptedException {
		if (direcao == Direcao.ESQUERDA) {
			Motor.B.forward();
			if (Motor.B.getSpeed() > MAX_SPEED_WHITE) {
				Motor.B.setSpeed(MAX_SPEED_WHITE);
			} else {
				Motor.B.setSpeed(Motor.B.getSpeed() - REDUCAO_POR_CICLO);
			}
		} else {
			Motor.A.forward();
			Motor.B.stop();
		}
	}

	private void aplicarVelocidadePadrao() {
		Motor.A.setSpeed(MAX_ESPEED);
		Motor.B.setSpeed(MAX_ESPEED);
	}

	public boolean isBranco() {
		return !isPreto();
	}

	public boolean isPreto() {
		int lightValue = colorSensor.getNormalizedLightValue();
		System.out.println(count++ + "    LV: " + lightValue);
		return lightValue < BLACK;
	}
}
