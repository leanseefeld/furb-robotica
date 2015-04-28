package br.furb.robotica;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import br.furb.robotica.algoritmos.Caminho;
import br.furb.robotica.algoritmos.GeradorCaminho;
import br.furb.robotica.algoritmos.Trapezio;

/**
 * Classe principal para testar os algoritmos com os diferentes cenários. <br>
 * Para alterar o algoritmo e o cenário utilizados, altere a implementação no
 * método {@link #getCaminho()}.
 */
public class Seguidor {

	private static final int _90GRAUS = 200;
	private static final int PASSO = 600;

	private int SPEED = 300;
	private Sentido sentido = Sentido.NORTE;
	private int[] posicaoAtual;

	public static void main(String[] args) throws Exception {
		System.out.println("PRESSIONE ENTER");
		Button.ENTER.waitForPressAndRelease();

		Caminho caminho = getCaminho();
		System.out.println("Caminho a seguir:");
		System.out.println(caminho.toString());
		caminho.otimizar();
		System.out.println("Caminho a seguir otimizado:");
		System.out.println(caminho.toString());
		System.out.println();

		Seguidor seguidor = new Seguidor();
		seguidor.percorrer(caminho);
	}

	/**
	 * Obtém um caminho a ser seguido pelo robô.<br>
	 * Altere a implementação deste método para mudar o algoritmo e o cenário
	 * usados na geração do caminho.
	 * 
	 * @return caminho a ser seguido pelo robô.
	 */
	private static Caminho getCaminho() {
		int[][] mapa = Cenarios.getCenarioA();

		GeradorCaminho geradorCaminho;
		// geradorCaminho = new Wavefront(mapa);
		geradorCaminho = new Trapezio(mapa);

		Caminho caminho = geradorCaminho.gerarCaminho();
		caminho.otimizar();
		return caminho;
	}

	private void moverEmFrente() {
		System.out.println("Moveu para frente");
		Motor.A.rotate(PASSO, true);
		Motor.B.rotate(PASSO);
	}

	private Sentido getSentido(int[] origem, int[] destino) {
		Sentido novoSentido = null;
		if (origem[0] - destino[0] < 0) {
			novoSentido = Sentido.LESTE;
		} else if (origem[0] - destino[0] > 0) {
			novoSentido = Sentido.OESTE;
		}

		if (origem[1] - destino[1] < 0) {
			novoSentido = Sentido.SUL;
		} else if (origem[1] - destino[1] > 0) {
			novoSentido = Sentido.NORTE;
		}

		return novoSentido;
	}

	private void girar(Sentido sentidoDestino) {
		int quantidadeGirar = sentidoDestino.ordinal() - this.sentido.ordinal();

		// Se for 3, muda para 1 e muda a direção da rotação
		// se for 4, fica parado
		if (quantidadeGirar > 2) {
			quantidadeGirar = -(quantidadeGirar % 2);
		}

		// Obs: Se quantidadeGirar for positiva, irá girar para direita
		// se for negativa, irá ser para a esquerda
		Motor.A.rotate((quantidadeGirar * _90GRAUS), true);
		Motor.B.rotate(-(quantidadeGirar * _90GRAUS));

		if (this.sentido != sentidoDestino) {
			System.out.print("Novo sentido " + sentidoDestino.name());
			if (quantidadeGirar > 0) {
				System.out.println(" - GIROU " + (quantidadeGirar * 90)
						+ "º à direita");
			} else {
				System.out.println(" - GIROU " + (quantidadeGirar * 90 * -1)
						+ "º à esquerda");
			}
			this.sentido = sentidoDestino;
		}
	}

	public void percorrer(Caminho caminho) throws Exception {
		Motor.A.setSpeed(SPEED);
		Motor.B.setSpeed(SPEED);
		int[] proximaPosicao = null;
		caminho.toFirst();
		this.posicaoAtual = caminho.nextElement();
		this.sentido = Sentido.NORTE;
		printPosicaoAtual();

		while ((proximaPosicao = caminho.nextElement()) != null) {
			Sentido novoSentido = getSentido(this.posicaoAtual, proximaPosicao);
			girar(novoSentido);
			moverEmFrente();
			this.sentido = novoSentido;
			this.posicaoAtual = proximaPosicao;
			printPosicaoAtual();
		}
		System.out.println("CHEGOU AO DESTINO!");
	}

	public void printPosicaoAtual() {
		System.out.println("Posicao Atual: Coluna:" + this.posicaoAtual[0]
				+ " Linha:" + this.posicaoAtual[1]);
		System.out.println("Sentido Atual: " + this.sentido.name());
	}

}
