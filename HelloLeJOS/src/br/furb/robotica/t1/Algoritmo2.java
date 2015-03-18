package br.furb.robotica.t1;

import static java.lang.System.currentTimeMillis;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

/**
 * Variação do Algoritmo2: Esse funciona para o lado interno e externo. O lado
 * será escolhido no início.
 * <p>
 * <h5>Melhorias implementadas (indicar como sugestões)</h5>
 * <ul>
 * <li>
 * Incremento gradual de velocidade: a cada ciclo em que um estado se mantém, a
 * velocidade aumenta, até que se chegue a um limite</li>
 * </ul>
 * </p>
 */
public class Algoritmo2 {

	private static long DURACAO_CICLO = 200;
	private static final int VEL_NORMAL = 500;
	private static final int VEL_MAXIMA = 700;
	private static final int PROPORCAO_PIVO = 7;
	// conclui o aumento em 200 * 5 ms
	private static final double VEL_INCREMENTO = (VEL_MAXIMA - VEL_NORMAL) / 5;
	private static final int PRETO = 430;
	private int velRotacao = VEL_NORMAL;
	private int velRotacaoPivo = velRotacao / PROPORCAO_PIVO;
	private final ColorSensor colorSensor;

	private boolean DIRECAO;

	private static final boolean ESQUERDA = true;
	private static final boolean DIREITA = false;

	public static void main(String[] args) throws InterruptedException {
		Algoritmo2 follower = new Algoritmo2();
		follower.executa();
	}

	public Algoritmo2() {
		colorSensor = new ColorSensor(SensorPort.S1);
	}

	public void executa() throws InterruptedException {
		Motor.A.setSpeed(velRotacao);
		Motor.B.setSpeed(velRotacao);

		System.out.println("Pressione ENTER para ligar a luz");
		Button.ENTER.waitForPressAndRelease();
		colorSensor.setFloodlight(true);

		System.out.println("Pressione ENTER para começar!");
		Button.ENTER.waitForPressAndRelease();

		Motor.A.forward();
		Motor.B.forward();

		long fimCiclo = currentTimeMillis() + DURACAO_CICLO;
		int estado = 1;
		int estadoAnterior = estado;

		while (true) {
			if (currentTimeMillis() >= fimCiclo) {
				if (estadoAnterior == estado) {
					velRotacao += VEL_INCREMENTO;
					velRotacao = velRotacao > VEL_MAXIMA ? VEL_MAXIMA
							: velRotacao; // Math.min
					velRotacaoPivo = velRotacao / PROPORCAO_PIVO;

					girar(DIRECAO); // atualiza velocidades
					fimCiclo = currentTimeMillis() + DURACAO_CICLO;
				}
			} else if (estadoAnterior != estado) {
				velRotacao = VEL_NORMAL;
				velRotacaoPivo = velRotacao / PROPORCAO_PIVO;

				girar(DIRECAO);
				fimCiclo = currentTimeMillis() + DURACAO_CICLO;
			}
			estadoAnterior = estado;

			if (Button.ESCAPE.isDown()) {
				break;
			}

			switch (estado) {
			case 1:
				if (isBranco()) {
					estado = 4;
				} else {
					estado = 2;
				}
				break;
			case 2:
				girar(DIREITA);
				if (isBranco()) {
					estado = 3;
				}
				break;
			case 3:
				girar(ESQUERDA);
				if (isPreto()) {
					estado = 2;
				}
				break;
			case 4:
				girar(DIREITA);
				if (isPreto()) {
					estado = 5;
				}
				break;
			case 5:
				girar(ESQUERDA);
				if (isBranco()) {
					estado = 4;
				}
			}
		}
	}

	private void girar(boolean ehEsquerda) {
		DIRECAO = ehEsquerda;
		if (ehEsquerda) {
			Motor.B.setSpeed(velRotacao);
			Motor.A.setSpeed(velRotacaoPivo);
		} else {
			Motor.A.setSpeed(velRotacao);
			Motor.B.setSpeed(velRotacaoPivo);
		}
	}

	public boolean isBranco() {
		int lightValue = colorSensor.getNormalizedLightValue();
		return lightValue >= PRETO;
	}

	public boolean isPreto() {
		int lightValue = colorSensor.getNormalizedLightValue();
		return lightValue < PRETO;
	}

}
