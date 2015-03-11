import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class TestButton {

	public static void main(String[] args) {
		TouchSensor touch = new TouchSensor(SensorPort.S1);

		int count = 0;
		while (!Button.ESCAPE.isDown()) {
			if (touch.isPressed()) {
				System.out.println("Pressionado! " + ++count);
			}
		}
	}

}
