package br.furb.robotica.t1;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

/**
 * Variação do Algoritmo2: Esse funciona para o lado interno e externo. O lado
 * será escolhido um no inicio
 */
public class Algoritmo2 {

	private int RAPIDO = 500;
	private int DEVAGAR = RAPIDO / 5;
	private int BLACK = 375;
	private ColorSensor colorSensor;
	private int estado = 1;

	private static final boolean ESQUERDA = true;
	private static final boolean DIREITA = false;

	public static void main(String[] args) throws InterruptedException {
		Algoritmo2 follower = new Algoritmo2();
		follower.executa();
	}

	public Algoritmo2() {
		colorSensor = new ColorSensor(SensorPort.S1);
	}

	public void executa() throws InterruptedException {
		aplicarVelocidadePadrao();
		System.out.println("Pressione ENTER para ligar a luz");
		Button.ENTER.waitForPressAndRelease();
		colorSensor.setFloodlight(true);
		System.out.println("Pressione ENTER para começar!");
		Button.ENTER.waitForPressAndRelease();
		while (true) {
			switch (estado) {
			case 1:
				if (isBranco()) {
					estado = 4;
				} else {
					estado = 2;
				}
				break;
			case 2:
				girar(DIREITA);
				if (isBranco()) {
					estado = 3;
				}
				break;
			case 3:
				girar(ESQUERDA);
				if (isPreto()) {
					estado = 2;
				}
				break;
			case 4:
				girar(DIREITA);
				if (isPreto()) {
					estado = 5;
				}
				break;
			case 5:
				girar(ESQUERDA);
				if (isBranco()) {
					estado = 4;
				}
			}
		}
	}

	private void girar(boolean esquerda) {
		if (esquerda) {
			Motor.B.setSpeed(RAPIDO);
			Motor.B.forward();
			Motor.A.setSpeed(DEVAGAR);
		} else {
			Motor.A.setSpeed(RAPIDO);
			Motor.A.forward();
			Motor.B.setSpeed(DEVAGAR);
		}
	}

	private void aplicarVelocidadePadrao() {
		Motor.A.setSpeed(RAPIDO);
		Motor.B.setSpeed(RAPIDO);
	}

	public boolean isBranco() {
		int lightValue = colorSensor.getNormalizedLightValue();
		System.out.println(lightValue);
		return lightValue >= BLACK;
	}

	public boolean isPreto() {
		int lightValue = colorSensor.getNormalizedLightValue();
		System.out.println(lightValue);
		return lightValue < BLACK;
	}
}
