package br.furb.robotica;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import br.furb.robotica.common.Coordenada;
import br.furb.robotica.estruturas.MinhaPilha;
import br.furb.robotica.estruturas.MinhaQueue;

public class RoboMapeador {

	private static final int VELOCIDADE = 500;
	private static final int PASSO = 800;
	private static final int DISTANCIA_OBSTACULO = 20;
	private static final int _90GRAUS = 90;
	private static final int _90GRAUS_RODAS = 400;

	public static void main(String[] args) {
		System.out.println("ENTER    -> Executa");
		System.out.println("[outro]  -> Depura");
		Debug.debug = Button.waitForAnyPress() != Button.ENTER.getId();

		MapaLabirinto mapa = new MapaLabirinto();
		RoboMapeador robo = new RoboMapeador(mapa, Lado.FRENTE,
				Coordenada.criar(3, 3));

		Behavior seguirTrajeto = new BehaviorSeguirCaminho(robo);
		Behavior montarTrajeto = new BehaviorMontarCaminhoExploracao(robo);
		Behavior analizarPosicao = new BehaviorAnalizarPosicao(robo);

		Behavior[] comportamentos = { seguirTrajeto, montarTrajeto,
				analizarPosicao };
		Arbitrator arb = new Arbitrator(comportamentos, true);
		arb.start();

		System.out.println("O mapeamento está completo");

		if (mapa.getCoordenadaDestino() == null) {
			System.out.println("Destino não encontrado.");
		} else {
			System.out.println("Pressione enter para seguir o menor caminho");
			Button.ENTER.waitForPressAndRelease();

			robo.moveRoboParaPontoInicial();

			Behavior mapeamentoMontaMenorCaminho = new BehaviorMontaMenorCaminho(
					robo);
			Behavior seguirMenorCaminho = new BehaviorSeguirMenorCaminho(robo);

			comportamentos = new Behavior[] { seguirMenorCaminho,
					mapeamentoMontaMenorCaminho };
			arb = new Arbitrator(comportamentos);
			arb.start();

			System.out.println("Objetivo alcançado");
		}
		Button.ENTER.waitForPressAndRelease();
		System.out.println("FIM");
	}

	private int[] coordenadaAtual;
	private Lado ladoAtual;
	private MapaLabirinto mapa;
	private UltrasonicSensor sensor;
	private Caminho caminho;
	private boolean mapaCompleto;
	private MinhaPilha<int[]> coordenadasNaoVisitados;
	private ColorSensor colorSensor;

	private final int[] coordenadaInicial;
	private final Lado ladoInicial;

	public RoboMapeador(MapaLabirinto mapa, Lado ladoAtual,
			int[] coordenadaAtual) {
		Motor.A.setSpeed(VELOCIDADE);
		Motor.B.setSpeed(VELOCIDADE);

		this.coordenadaAtual = coordenadaAtual;
		this.coordenadaInicial = coordenadaAtual;
		this.ladoAtual = ladoAtual;
		this.ladoInicial = ladoAtual;
		this.mapa = mapa;
		this.coordenadasNaoVisitados = new MinhaQueue<int[]>();
		this.sensor = new UltrasonicSensor(SensorPort.S4);
		this.colorSensor = new ColorSensor(SensorPort.S3);
		this.colorSensor.setFloodlight(true);
	}

	/**
	 * @return Retorna o caminho que o robo deve seguir
	 */
	public Caminho getCaminho() {
		return this.caminho;
	}

	/**
	 * Insere um caminho para o robo seguir
	 * 
	 * @param caminho
	 */
	public void setCaminho(Caminho caminho) {
		this.caminho = caminho;
	}

	/**
	 * @return Retorna a posição atual do robo no mapa
	 */
	public InfoPosicao getPosicaoAtual() {
		return this.mapa.getInfoPosicao(this.coordenadaAtual);
	}

	/**
	 * Pega informações da posição atual do robo
	 */
	public void analisarPosicao() {
		if (mapa.getInfoPosicao(coordenadaAtual) != null) {
			//Apenas para testes... depois remover isso
			throw new UnsupportedOperationException(
					"Analisando posição repetida");
		}

		InfoPosicao infoPosicao = mapa.criarPosicao(coordenadaAtual);

		if (colorSensor.isFloodlightOn()) {
			int colorId = colorSensor.getColorID();
			if (colorId == Color.GREEN) {
				this.mapa.setCoordenadaDestino(coordenadaAtual);
				colorSensor.setFloodlight(false);
				Debug.println("Green: " + colorId);
			}
		}

		Motor.C.rotate(-_90GRAUS); //ESQUERDA DO ROBO
		int ordinalLado = this.ladoAtual.ordinal() - 1;
		ordinalLado = ordinalLado == -1 ? 3 : ordinalLado;
		Lado ladoSensor = Lado.valueOf(ordinalLado);
		infoPosicao.setLadoLivre(ladoSensor,
				sensor.getDistance() > DISTANCIA_OBSTACULO);

		Debug.println(ladoSensor.name() + " - " + sensor.getDistance());

		Motor.C.rotate(+_90GRAUS); //FRENTE DO ROBO
		ladoSensor = Lado.valueOf(this.ladoAtual.ordinal());
		infoPosicao.setLadoLivre(ladoSensor,
				sensor.getDistance() > DISTANCIA_OBSTACULO);

		Debug.println(ladoSensor.name() + " - " + sensor.getDistance());

		Motor.C.rotate(+_90GRAUS); //DIREITA DO ROBO
		ladoSensor = Lado.valueOf((this.ladoAtual.ordinal() + 1) % 4);
		infoPosicao.setLadoLivre(ladoSensor,
				sensor.getDistance() > DISTANCIA_OBSTACULO);
		Debug.println(ladoSensor.name() + " - " + sensor.getDistance());

		//Aponta o sensor para frente novamente
		Motor.C.rotate(-_90GRAUS, true);
		if (!Coordenada.equals(coordenadaInicial, coordenadaAtual)) {
			infoPosicao.setLadoLivre(ladoAtual.getOposto());
		}
	}

	/**
	 * Monta um caminho para uma posição desejada para que o robo siga
	 */
	public Caminho montarCaminhoAteProximaPosicao() {
		Caminho caminho = null;
		int[][] coordenadasVisinhas = mapa
				.getVisinhosConexosNaoExplorados(coordenadaAtual);

		if (coordenadasVisinhas.length != 0) {
			caminho = new Caminho();
			caminho.addPasso(coordenadasVisinhas[0]);
			for (int i = 1; i < coordenadasVisinhas.length; i++) {
				coordenadasNaoVisitados.empilhar(coordenadasVisinhas[i]);
			}
		} else {
			int[] coord = null;
			while ((coord = coordenadasNaoVisitados.pegar()) != null) {
				Debug.print(Coordenada.toString(coord));
				if (mapa.getInfoPosicao(coord) == null) {
					Debug.print("Escolhida!");
					caminho = mapa
							.montarCaminhoDijkstra(coordenadaAtual, coord);
					Debug.print(caminho.toString());
					break;
				}
			}

			if (caminho == null) {
				this.mapaCompleto = true;
				return null;
			} else {
				caminho.toFirst();
				caminho.nextElement();
			}
		}
		return caminho;
	}

	/**
	 * Monta o menor caminho da posição atual até o destino
	 * 
	 * @return
	 */
	public Caminho montarMenorCaminho() {
		Caminho caminho = mapa.montarCaminhoDijkstra(this.coordenadaAtual,
				this.mapa.getCoordenadaDestino());
		caminho.nextElement();
		return caminho;
	}

	/**
	 * Move para a proxima posição do caminho
	 */
	public void moverProximaPosicao() {
		int[] proximaPosicao = null;
		printPosicaoAtual();

		proximaPosicao = caminho.nextElement();
		Lado novoSentido = mapa.getLado(this.coordenadaAtual, proximaPosicao);
		girar(novoSentido);
		moverEmFrente();
		this.ladoAtual = novoSentido;
		this.coordenadaAtual = proximaPosicao;
		printPosicaoAtual();
	}

	/**
	 * Verifica se todo o mapa ja foi mapeado
	 * 
	 * @return
	 */
	public boolean mapeamentoEstaCompleto() {
		return mapaCompleto;
	}

	/**
	 * Move o robo uma posição para frente
	 */
	private void moverEmFrente() {
		Debug.println("Moveu para frente");
		Motor.A.rotate(PASSO, true);
		Motor.B.rotate(PASSO);
	}

	/**
	 * Gira o robo no para o lado desejado
	 * 
	 * @param ladoDestino
	 */
	private void girar(Lado ladoDestino) {
		int quantidadeGirar = ladoDestino.ordinal() - this.ladoAtual.ordinal();

		// Se for 3, muda para 1 e muda a direção da rotação
		// se for 4, fica parado
		if (quantidadeGirar > 2) {
			quantidadeGirar = -(quantidadeGirar % 2);
		}

		// Obs: Se quantidadeGirar for positiva, irá girar para direita
		// se for negativa, irá ser para a esquerda
		Motor.A.rotate(-(quantidadeGirar * _90GRAUS_RODAS));
		Motor.B.rotate((quantidadeGirar * _90GRAUS_RODAS), true);

		if (this.ladoAtual != ladoDestino) {
			Debug.print("Novo sentido " + ladoDestino.name());
			if (quantidadeGirar > 0) {
				Debug.println(" - GIROU " + (quantidadeGirar * 90)
						+ "º à direita");
			} else {
				Debug.println(" - GIROU " + (quantidadeGirar * 90 * -1)
						+ "º à esquerda");
			}
			this.ladoAtual = ladoDestino;
		}
	}

	/**
	 * Exibe a posição e o lado atual do robo
	 */
	public void printPosicaoAtual() {
		Debug.println("Posicao Atual: Coluna:"
				+ this.coordenadaAtual[Matriz.COLUNA] + " Linha:"
				+ this.coordenadaAtual[Matriz.LINHA]);
		Debug.println("Lado Atual: " + this.ladoAtual.name());
	}

	/**
	 * Atualiza as coordenadas do robo para a posição inicial
	 */
	public void moveRoboParaPontoInicial() {
		this.coordenadaAtual = this.coordenadaInicial;
		this.ladoAtual = this.ladoInicial;
	}

	/**
	 * Verifica se o robo está sobre a coordenada objetivo
	 * 
	 * @return
	 */
	public boolean estaSobreOjetivo() {
		return Coordenada.equals(this.coordenadaAtual,
				this.mapa.getCoordenadaDestino());
	}

	public boolean analisouPosicao() {
		return getPosicaoAtual() != null;
	}
}
