package br.furb.robotica.t1;
import br.furb.robotica.t1.commons.Direcao;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Algoritmo2 {

	/*
	 * Variação do Algoritmo2:
	 * Esse funciona para o lado interno e externo. O lado será escolhido um no inicio
	 */

	private int MAX_ESPEED = 600;
	private int MAX_SPEED_WHITE = 200;
	private int REDUCAO_POR_CICLO = 1;
	private int BLACK = 400;
	private ColorSensor colorSensor;
	private int estado = 1;
	private int count = 0;

	public static void main(String[] args) throws InterruptedException {
		Algoritmo2 follower = new Algoritmo2();

		System.out.println("Ready to go! Press the main button");
		Button.ENTER.waitForPressAndRelease();

		follower.run();
	}
	
	public Algoritmo2()
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
					estado = 5;
					aplicarVelocidadePadrao();
				}
				break;
			case 5:
				esquerda();
				if (isBranco()) {
					estado = 4;
					aplicarVelocidadePadrao();
				}
			}
		}
	}

	public void direita() throws InterruptedException {
		girar(Direcao.DIREITA);
	}

	public void esquerda() throws InterruptedException {
		girar(Direcao.ESQUERDA);
	}

	private void CurvaSuave(NXTRegulatedMotor motor) {
		if (motor.getSpeed() > MAX_SPEED_WHITE) {
			motor.setSpeed(MAX_SPEED_WHITE);
		} else {
			motor.setSpeed(motor.getSpeed() - REDUCAO_POR_CICLO);
		}
	}

	private void girar(Direcao direcao) throws InterruptedException {
		if (direcao == Direcao.ESQUERDA) {
			Motor.B.forward();
			if (isPreto()) {
				Motor.A.stop();
			} else {
				CurvaSuave(Motor.A);
			}

		} else {
			Motor.A.forward();
			if (isPreto()) {
				Motor.B.stop();
			} else {
				CurvaSuave(Motor.B);
			}
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
		System.out.println(count++ + " - LV: " + lightValue);
		return lightValue < BLACK;
	}
}
