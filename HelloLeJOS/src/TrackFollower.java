import static java.lang.Thread.sleep;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class TrackFollower {

	private static enum Direcao {
		ESQUERDA, DIREITA;
	}

	private ColorSensor colorSensor;
	private int rotationTime = 20;
	private int rotationSpeed = 600;
	private int black = 400;
	private int estado = 1;
	private int count = 0;

	public static void main(String[] args) throws InterruptedException {
		TrackFollower follower = new TrackFollower();

		System.out.println("Ready to go! Press the main button");
		Button.ENTER.waitForPressAndRelease();

		follower.run();
	}

	public TrackFollower() {
		colorSensor = new ColorSensor(SensorPort.S1);
	}

	public void run() throws InterruptedException {
		colorSensor.setFloodlight(true);
		setVelocidade(rotationSpeed);
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

	public void direita() throws InterruptedException {
		girar(Direcao.DIREITA);
	}

	public void esquerda() throws InterruptedException {
		girar(Direcao.ESQUERDA);
	}

	private void girar(Direcao direcao) throws InterruptedException {
		if (direcao == Direcao.ESQUERDA) {
			Motor.B.forward();
			Motor.A.stop();
		} else {
			Motor.A.forward();
			Motor.B.stop();
		}
	}

	public boolean isBranco() {
		return !isPreto();
	}

	public boolean isPreto() {
		int lightValue = colorSensor.getNormalizedLightValue();
		System.out.println(count++ + "    LV: " + lightValue);
		return lightValue < black;
	}

	private static void setVelocidade(int velocidade) {
		Motor.A.setSpeed(velocidade);
		Motor.B.setSpeed(velocidade);
	}

}
