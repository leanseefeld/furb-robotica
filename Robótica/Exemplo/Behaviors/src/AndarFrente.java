import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

public class AndarFrente implements Behavior {

	@Override
	public boolean takeControl() {
		// nível zero - sempre é ativado se
		// nenhum outro comportamento foi ativado
		return true;
	}

	@Override
	public void action() {
		Motor.A.forward();
		Motor.B.forward();
	}

	@Override
	public void suppress() {
		Motor.A.flt();
		Motor.B.flt();
	}

}
