import Commons.Direcao;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class SeguidorTrilha2 {

	/*
	 * Ao atingir o branco, uma das rodas para com o objetivo de chegar ao preto
	 * novamente Ao atingir o preto, a roda parada passa a girar novamente mas
	 * numa velocidade inferior a velocidade normal para que o veiculo continue
	 * girando para a mesma direção
	 */

	private ColorSensor sensorCor;
	private int velocidadeRotacao = 600;
	private int velocidadeRotacaoMiniminaNoPreto = 200;
	private int velocidadeRotacaoMiniminaNoBranco = 0;
	private int corPreta = 400;
	private int estado = 1;
	private int contagem = 0;

	public static void main(String[] args) throws InterruptedException {
		SeguidorTrilha2 seguidor = new SeguidorTrilha2();

		System.out.println("Pronto! Aperte ENTER");
		Button.ENTER.waitForPressAndRelease();

		seguidor.executar();
	}

	public SeguidorTrilha2() {
		sensorCor = new ColorSensor(SensorPort.S1);
	}

	public void executar() throws InterruptedException {
		sensorCor.setFloodlight(true);
		setVelocidade(velocidadeRotacao);
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
			if (isPreto()) {
				Motor.A.setSpeed(velocidadeRotacaoMiniminaNoPreto);
			} else {
				Motor.A.setSpeed(velocidadeRotacaoMiniminaNoBranco);
			}
		} else {
			Motor.A.forward();
			if (isPreto()) {
				Motor.B.setSpeed(velocidadeRotacaoMiniminaNoPreto);
			} else {
				Motor.A.setSpeed(velocidadeRotacaoMiniminaNoBranco);
			}
		}
	}

	public boolean isBranco() {
		return !isPreto();
	}

	public boolean isPreto() {
		int lightValue = sensorCor.getNormalizedLightValue();
		System.out.println(contagem++ + "    LV: " + lightValue);
		return lightValue < corPreta;
	}

	private static void setVelocidade(int velocidade) {
		Motor.A.setSpeed(velocidade);
		Motor.B.setSpeed(velocidade);
	}

}
