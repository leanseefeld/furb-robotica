import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class SumptionRobot {

	private static final int VELOCIDADE = 600;

	public static void main(String[] args) {
		System.out.println("Pressione ENTER");
		Button.ENTER.waitForPressAndRelease();
		Motor.A.setSpeed(VELOCIDADE);
		Motor.B.setSpeed(VELOCIDADE);
		
		UltrasonicSensor sensor = new UltrasonicSensor(SensorPort.S4);

		Behavior andar = new AndarFrente();
		Behavior desviar = new NaoBater(sensor);
		
		Behavior[] comportamentos = {andar, desviar};
		// quanto maior o índice, 
		// maior a prioridade do comportamento
		
		Arbitrator arb = new Arbitrator(comportamentos);
		arb.start();
	}

}
