package br.furb.teste.robotica;

import br.furb.robotica.Lado;
import br.furb.robotica.MapaLabirinto;
import br.furb.robotica.common.Coordenada;

public class Main {

    public static void main(String[] args) {
	System.out.println("INICIO - Trabalho3Testes");
	MapaLabirinto mapa = new MapaLabirinto();
	mapa.setCoordenadaDestino(0, 0);

	RoboMapeador robo = new RoboMapeador(mapa, Lado.FRENTE, Coordenada.criar(3, 3));

//	InfoPosicao[][] posicoes = mapa.getPosicoes();
//	posicoes[3][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS); //Origem
//	posicoes[2][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
//	posicoes[1][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
//	posicoes[0][3] = new InfoPosicao(Lado.ESQUERDA, Lado.ATRAS);
//	posicoes[0][2] = new InfoPosicao(Lado.ATRAS, Lado.DIREITA);
//	posicoes[1][2] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS, Lado.ESQUERDA);
//	robo.addCoordenadaNaoExplorada(new int[] { 2, 2 });
//	posicoes[1][1] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS, Lado.DIREITA);
//	robo.addCoordenadaNaoExplorada(new int[] { 0, 1 });
//	posicoes[2][1] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
//	posicoes[3][1] = new InfoPosicao(Lado.ESQUERDA, Lado.FRENTE, Lado.DIREITA);
//	robo.addCoordenadaNaoExplorada(new int[] { 3, 2 });
//	posicoes[3][0] = new InfoPosicao(Lado.FRENTE, Lado.DIREITA);
//	posicoes[2][0] = new InfoPosicao(Lado.ATRAS, Lado.FRENTE);
//	posicoes[1][0] = new InfoPosicao(Lado.ATRAS, Lado.FRENTE);
//	posicoes[0][0] = new InfoPosicao(Lado.ATRAS);

	while (true) {
	    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	    System.out.println("-------ANALIZANDO POSIÇÃO: " + Coordenada.toString(robo.getCoordenadaAtual()));
	    robo.analisarPosicao();
	    
	    System.out.println("-------PROCURANDO CAMINHO");
	    robo.setCaminho(robo.montarCaminhoAteProximaPosicao());

	    if (robo.mapeamentoEstaCompleto())
		break;

	    System.out.println(robo.getCaminho().toString());

	    System.out.println("-------CAMINHANDO");
	    while (!robo.getCaminho().isAfterLast()) {
		robo.moverProximaPosicao();
	    }
	    
	}
	System.out.println("Mapa completo");
	System.out.println("FIM");
    }

    /*private static Caminho montarCaminhoAteProximaPosicao(MapaLabirinto mapa, int[] coordenadaAtual) {
	Caminho caminho = null;
	List<int[]> coordenadaVisinho = mapa.getVisinhosExplorados(coordenadaAtual);

	if (!coordenadaVisinho.isEmpty()) {
	    caminho = new Caminho();
	    caminho.addPasso(coordenadaAtual);
	    caminho.addPasso(coordenadaVisinho.get(0));
	    for (int i = 1; i < coordenadaVisinho.size(); i++) {
		caminhosNaoVisitados.empilhar(coordenadaVisinho.get(i));
	    }
	} else {
	    int[] coord = null;
	    while ((coord = caminhosNaoVisitados.pegar()) != null) {
		if (mapa.getInfoPosicao(coord) == null) {
		    caminho = mapa.montarCaminhoDijkstra(coordenadaAtual, coord);
		    //caminho = mapa.montarCaminhoWaveFront(coordenadaAtual, coord, new MinhaLinkedList<int[]>());
		    break;
		}
	    }

	    if (caminho == null) {
		mapaCompleto = true;
	    }
	}
	caminho.toFirst();
	return caminho;
    }*/
}
