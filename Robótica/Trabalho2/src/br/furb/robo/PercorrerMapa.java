package br.furb.robo;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import br.furb.robotica.Caminho;
import br.furb.robotica.Main;
import br.furb.robotica.Trapezio;

public class PercorrerMapa {

	private int SPEED = 300;
	private Sentido sentido = Sentido.Norte;
	private static final int _90GRAUS = 100;
	private static final int PASSO = 100;
	private int[] posicaoAtual;

	public static void main(String[] args) throws Exception {
		PercorrerMapa percorrerMapa = new PercorrerMapa();

		System.out.println("PRESSIONE ENTER");
		// Button.ENTER.waitForPressAndRelease();

		int[][] mapa = Main.getCenarioE();
		Trapezio trap = new Trapezio(mapa);
		Caminho caminho = trap.montaCaminho();
		System.out.println("Caminho a seguir:");
		System.out.println(caminho.toString());
		caminho.Otimizar();
		System.out.println("Caminho a seguir otimizado:");
		System.out.println(caminho.toString());
		System.out.println();

		percorrerMapa.run(caminho);
	}

	private void MoverEmFrente() {
		System.out.println("Moveu para frente");
		//Motor.A.rotate(PASSO, true);
		//Motor.B.rotate(PASSO);
	}

	private Sentido getSentido(int[] origem, int[] destino) {
		Sentido novoSentido = null;
		if (origem[0] - destino[0] < 0) {
			novoSentido = Sentido.Leste;
		} else if (origem[0] - destino[0] > 0) {
			novoSentido = Sentido.Oeste;
		}

		if (origem[1] - destino[1] < 0) {
			novoSentido = Sentido.Sul;
		} else if (origem[1] - destino[1] > 0) {
			novoSentido = Sentido.Norte;
		}

		return novoSentido;
	}

	private void Girar(Sentido sentidoDestino) {
		int quantidadeGirar = sentidoDestino.value - this.sentido.value;

		// Se for 3, muda para 1 e muda a direção da rotação
		// se for 4, fica parado
		if (quantidadeGirar > 2) {
			quantidadeGirar = -(quantidadeGirar % 2);
		}

		// Obs: Se quantidadeGirar for positiva, irá girar para direita
		// se for negativa, irá ser para a esquerda
		// Motor.A.rotate((quantidadeGirar * _90GRAUS), true);
		// Motor.B.rotate(-(quantidadeGirar * _90GRAUS));

		if (this.sentido != sentidoDestino) {
			System.out.print("Novo sentido " + sentidoDestino.name());
			if (quantidadeGirar > 0) {
				System.out.println(" - GIROU " + (quantidadeGirar * 90) + "º à direita");
			} else {
				System.out.println(" - GIROU " + (quantidadeGirar * 90 * -1) + "º à esquerda");
			}
		}
	}

	public void run(Caminho caminho) throws Exception {
		// Motor.A.setSpeed(SPEED);
		// Motor.B.setSpeed(SPEED);
		int[] proximaPosicao = null;
		caminho.toFirst();
		this.posicaoAtual = caminho.nextElement();
		this.sentido = Sentido.Norte;
		printPosicaoAtual();
		while ((proximaPosicao = caminho.nextElement()) != null) {
			Sentido novoSentido = getSentido(this.posicaoAtual, proximaPosicao);
			Girar(novoSentido);
			MoverEmFrente();
			this.sentido = novoSentido;
			this.posicaoAtual = proximaPosicao;
			printPosicaoAtual();
		}
		System.out.println("CHEGOU AO DESTINO!!");
	}

	public void printPosicaoAtual() {
		System.out.println("Posicao Atual: Coluna:" + this.posicaoAtual[0] + " Linha:" + this.posicaoAtual[1]);
		System.out.println("Sentido Atual: " + this.sentido.name());
	}
}
