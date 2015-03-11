import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class LightSensor {

	public static void main(String[] args) {
		TouchSensor touch = new TouchSensor(SensorPort.S1);
		ColorSensor color = new ColorSensor(SensorPort.S3);

		// // Calibrando
		// LCD.drawString("Ponto claro", 0, 0);
		// Button.ENTER.waitForPress();
		// color.setHigh(color.getLightValue());

		while (!touch.isPressed()) {
			System.out.println(color.getLightValue());
		}
	}

}
