package testes;

import static java.lang.Thread.sleep;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Ultrassom {
	
	public static void main(String[] args) throws InterruptedException {
		UltrasonicSensor ultrassom = new UltrasonicSensor(SensorPort.S1);
		while(true) {
			sleep(500);
			System.out.println(ultrassom.getDistance());
		}
	}

}
