package br.furb.robotica;

import static br.furb.robotica.Constantes.O;
import static br.furb.robotica.Constantes.R;
import static br.furb.robotica.Constantes.V;
import static br.furb.robotica.Constantes.X;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import br.furb.robotica.algoritmos.Caminho;
import br.furb.robotica.algoritmos.Wavefront;

public class EscolhedorCaminho {

	private int SPEED = 400;
	private Sentido sentido = Sentido.NORTE;
	private static final int _90GRAUS = 200;
	private static final int PASSO = 600;
	private int[] posicaoAtual;

	public static void main(String[] args) throws Exception {
		EscolhedorCaminho seguidor = new EscolhedorCaminho();

		System.out.println("PRESSIONE ENTER");
		Button.ENTER.waitForPressAndRelease();

		int[][] mapa = Cenarios.getCenarioB();
		Caminho caminho = getCaminho(mapa);
		System.out.println("Caminho a seguir:");
		System.out.println(caminho.toString());
		caminho.otimizar();
		System.out.println("Caminho a seguir otimizado:");
		System.out.println(caminho.toString());
		System.out.println();

		seguidor.percorrer(caminho);
	}

	private static int[][] escolherMapa() {
		List<Mapa> mapas = gerarMapas();
		System.out.println("Escolha um mapa \nusando as setas\n");
//		lejos.nxt.LCDOutputStream 
		return null;
		
	}

	private static List<Mapa> gerarMapas() {
		int[][] mapa = new int[][]
		/*  */{ { V, X, V, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ V, V, V, V, X, O }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ X, V, X, V, V, V }, //
				{ R, V, V, X, V, X } };
		mapa = Cenarios.inverteMatriz(mapa);
		Mapa mA = new Mapa("Cenário A", mapa);

		mapa = new int[][]
		/*  */{ { V, V, V, V, V, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, X, V, V }, //
				{ R, X, V, V, X, V }, //
				{ V, V, X, V, V, V }, //
				{ V, V, V, V, X, V }, //
				{ V, V, V, X, V, O } };
		mapa = Cenarios.inverteMatriz(mapa);
		Mapa mB = new Mapa("Cenário B", mapa);
		List<Mapa> mapas = new ArrayList<Seguidor_.Mapa>();
		mapas.add(mA);
		mapas.add(mB);
		return mapas;
	}

	private static Caminho getCaminho(int[][] mapa) {
		//		Trapezio trapezio = new Trapezio(mapa);
		//		return trapezio.montaCaminho();

		Wavefront wavefront = new Wavefront(mapa);
		return wavefront.buscarCaminho();
	}

	private void MoverEmFrente() {
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

		/*Girar(Sentido.Leste);
		System.out.println("PRESSIONE ENTER");
		Button.ENTER.waitForPressAndRelease();
		Girar(Sentido.Norte);
		System.out.println("PRESSIONE ENTER");
		Button.ENTER.waitForPressAndRelease();
		MoverEmFrente();
		System.out.println("PRESSIONE ENTER");
		Button.ENTER.waitForPressAndRelease();*/

		while ((proximaPosicao = caminho.nextElement()) != null) {
			Sentido novoSentido = getSentido(this.posicaoAtual, proximaPosicao);
			girar(novoSentido);
			MoverEmFrente();
			this.sentido = novoSentido;
			this.posicaoAtual = proximaPosicao;
			printPosicaoAtual();
		}
		System.out.println("CHEGOU AO DESTINO!!");
	}

	public void printPosicaoAtual() {
		System.out.println("Posicao Atual: Coluna:" + this.posicaoAtual[0]
				+ " Linha:" + this.posicaoAtual[1]);
		System.out.println("Sentido Atual: " + this.sentido.name());
	}

	private static class Mapa {

		private int[][] celulas;
		private String nome;

		public Mapa(String nome, int[][] celulas) {
			this.nome = nome;
			this.celulas = celulas;
		}

		public int[][] getCelulas() {
			return celulas;
		}

		public String getNome() {
			return nome;
		}

	}

}
