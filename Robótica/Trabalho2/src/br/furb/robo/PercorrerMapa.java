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
		Button.ENTER.waitForPressAndRelease();

		int[][] mapa = Main.cenarioA();
		Trapezio trap = new Trapezio(mapa);
		Caminho caminho = trap.getCaminho();
		
		percorrerMapa.run(caminho);
	}

	private void MoverEmFrente() {
		Motor.A.rotate(PASSO, true);
		Motor.B.rotate(PASSO);
	}

	private Sentido getSentido(int[] origem, int[] destino) {
		if (origem[0] - destino[0] < 0) {
			return Sentido.Leste;
		} else if (origem[0] - destino[0] > 0) {
			return Sentido.Oeste;
		}

		if (origem[1] - destino[1] < 0) {
			return Sentido.Sul;
		} else if (origem[1] - destino[1] > 0) {
			return Sentido.Norte;
		}
		System.out.println("Algo errado em getSentido");
		return null;
	}

	private void Girar(Sentido sentidoDestino) {
		int quantidadeGirar = Math.abs(this.sentido.value - sentidoDestino.value);

		// Se for 3, muda para 1 e muda a direção da rotação
		// se for 4, fica parado
		if (quantidadeGirar > 2) {
			quantidadeGirar = -(quantidadeGirar % 2);
		}

		// Obs: Se quantidadeGirar for positiva, irá girar para direita
		// se for negativa, irá ser para a esquerda
		Motor.A.rotate((quantidadeGirar * _90GRAUS), true);
		Motor.B.rotate(-(quantidadeGirar * _90GRAUS));
	}

	public void run(Caminho caminho) throws Exception {
		Motor.A.setSpeed(SPEED);
		Motor.B.setSpeed(SPEED);
		
		caminho.toFirst();
		this.posicaoAtual = caminho.getPosicaoAtual();
		while(caminho.toNext())
		{
			Sentido novoSentido = getSentido(this.posicaoAtual, caminho.getPosicaoAtual());
			Girar(novoSentido);
			MoverEmFrente();
			this.posicaoAtual = caminho.getPosicaoAtual();
		}
		MoverEmFrente();
	}
}
