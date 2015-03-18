package br.furb.robotica.t1;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

/**
 * Variação do Algoritmo2: Esse funciona para o lado interno e externo. O lado
 * será escolhido um no inicio
 */
public class Algoritmo2 {

	private static final int VEL_RETA = 700;
	private static final int VEL_CURVA = 500;
	private int RAPIDO = VEL_CURVA;
	private int DEVAGAR = RAPIDO / 7;
	private int PRETO = 430;
	private ColorSensor colorSensor;
	private int estado = 1;

	private static long INTERVALO = 300;
	private long proxMedicao;
	private long variacao;
	private long GATILHO_RETA = 4;
	private boolean DIRECAO;

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
		proxMedicao = System.currentTimeMillis() + INTERVALO;
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
					variacao++;
				}
				break;
			case 3:
				girar(ESQUERDA);
				if (isPreto()) {
					estado = 2;
					variacao++;
				}
				break;
			case 4:
				girar(DIREITA);
				if (isPreto()) {
					estado = 5;
					variacao++;
				}
				break;
			case 5:
				girar(ESQUERDA);
				if (isBranco()) {
					estado = 4;
					variacao++;
				}
			}
			long mili = System.currentTimeMillis();
			if (mili >= proxMedicao) {
				if (variacao > GATILHO_RETA) {
					RAPIDO = VEL_RETA;
					LCD.clearDisplay();
					LCD.drawString("RETA", 0, 0);
				} else {
					RAPIDO = VEL_CURVA;
					LCD.clearDisplay();
					LCD.drawString("CURVA", 0, 0);
				}
//				System.out.println(variacao);
				DEVAGAR = RAPIDO / 7;
				girar(DIRECAO);
				variacao = 0;
				proxMedicao = mili + INTERVALO;
			}
		}
	}

	private void girar(boolean ehEsquerda) {
		DIRECAO = ehEsquerda;
		if (ehEsquerda) {
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
		return lightValue >= PRETO;
	}

	public boolean isPreto() {
		int lightValue = colorSensor.getNormalizedLightValue();
		return lightValue < PRETO;
	}
}
