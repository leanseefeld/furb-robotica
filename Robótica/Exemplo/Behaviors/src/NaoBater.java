import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class NaoBater implements Behavior {

	private final UltrasonicSensor ultrasonicSensor;

	public NaoBater(UltrasonicSensor touch) {
		this.ultrasonicSensor = touch;
	}

	@Override
	public boolean takeControl() {
		int distance = ultrasonicSensor.getDistance();
		System.out.println(distance);
		return distance < 15 && distance != 255;
	}

	@Override
	public void action() {
		//		Motor.B.flt(true);
		Motor.A.rotate(-360, true);
		Motor.B.rotate(180);
	}

	@Override
	public void suppress() {
		// nada
	}

}
