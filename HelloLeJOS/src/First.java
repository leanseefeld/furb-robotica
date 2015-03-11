import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;


public class First {

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(1000);
		ColorSensor cor = new ColorSensor(SensorPort.S3);
		
		float speed = 1440;
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed);
		while (true) {
			if (cor.getLightValue() > 100) {
				Motor.A.rotate(50);
			} else {
				Motor.B.rotate(50);
			}
		}
	}

}