import static java.lang.Thread.sleep;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class TrackFollower {

	private static enum Direcao {
		FRENTE, DIREITA, ESQUERDA
	}

	private static int FORWARD_TIME = 50;
	private static int ROTATION_TIME = 20;

	private static int SPEED = 200;
	private static int ROTATION_SPEED = 600;
	private static int DEFAULT_ROTATION = 30;
	private static int BLACK = 400;

	private static ColorSensor colorSensor;
	private static int estado = 1;
	private static Direcao direcao = Direcao.FRENTE;
	private static int count = 0;

	static boolean teste = false;

	public static void main(String[] args) throws InterruptedException {

		if (teste) {
			teste();
			return;
		}

		colorSensor = new ColorSensor(SensorPort.S1);
		colorSensor.setFloodlight(true);
		System.out.println("Ready to go! Press the main button");
		Button.ENTER.waitForPressAndRelease();

		/*
		 * System.out.println("Posicione na cor escura... ");
		 * Button.ENTER.waitForPressAndRelease(); int lightValue =
		 * colorSensor.getLightValue(); colorSensor.setLow(lightValue);
		 * System.out.print("\nPosicione na cor clara... ");
		 * Button.ENTER.waitForPressAndRelease(); lightValue =
		 * colorSensor.getLightValue(); colorSensor.setHigh(lightValue);
		 * System.out.println();
		 */
		setSpeed(ROTATION_SPEED);

		while (true) {
			System.out.println("Estado: " + estado);
			switch (estado) {
			case 1:
				direita();
				if (isPreto()) {
					estado = 2;
				}
				break;
			case 2:
				direita();
				if (isBranco()) {
					estado = 3;
				}
				break;
			case 3:
				esquerda();
				if (isPreto()) {
					estado = 4;
				}
				break;
			case 4:
				esquerda();
				if (isBranco()) {
					estado = 1;
				}
				break;
			}
		}
	}

	private static void setSpeed(int velocidade) {
		Motor.A.setSpeed(velocidade);
		Motor.B.setSpeed(velocidade);
	}

	/*public static void frente() throws InterruptedException {
		 Motor.A.forward();
		 Motor.B.forward();
		 sleep(FORWARD_TIME);
	}*/

	public static void direita() throws InterruptedException {
		rotate(false);
	}

	public static void esquerda() throws InterruptedException {
		rotate(true);
	}

	private static void rotate(boolean esquerda) throws InterruptedException {
		if (esquerda) {
			Motor.B.forward();
			Motor.A.stop();
			// Motor.B.rotate(DEFAULT_ROTATION);
		} else {
			Motor.A.forward();
			Motor.B.stop();
		}
		sleep(ROTATION_TIME);
	}

	public static boolean isBranco() {
		return !isPreto();
	}

	public static boolean isPreto() {
		int lightValue = colorSensor.getNormalizedLightValue();
		System.out.println(count++ + "    LV: " + lightValue);
		return lightValue < BLACK;
	}

	private static void teste() throws InterruptedException {
		setSpeed(100);
		// frente();
		sleep(2000);
		setSpeed(1000);
		direita();
		sleep(5000);
	}

}
