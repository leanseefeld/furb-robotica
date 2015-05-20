package br.furb.robotica;

import java.util.List;

public class Main {

    public static void main(String[] args) {
	System.out.println("Trabalho3Testes iniciou");
	MapaLabirinto mapa = new MapaLabirinto();
	mapa.setCoordenadaDestino(0, 0);
	mapa.setCoordenadaOrigem(3, 3);
	
	InfoPosicao[][] posicoes = mapa.getPosicoes();
	posicoes[0][0] = new InfoPosicao(Lado.ATRAS); //Destino
	posicoes[1][0] = new InfoPosicao(Lado.ATRAS, Lado.FRENTE);
	posicoes[2][0] = new InfoPosicao(Lado.ATRAS, Lado.FRENTE);
	posicoes[3][0] = new InfoPosicao(Lado.FRENTE, Lado.DIREITA);
	posicoes[3][1] = new InfoPosicao(Lado.ESQUERDA, Lado.FRENTE, Lado.ESQUERDA);
	posicoes[2][1] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
	posicoes[1][1] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS, Lado.DIREITA);
	posicoes[1][2] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS, Lado.ESQUERDA);
	posicoes[0][2] = new InfoPosicao(Lado.ATRAS, Lado.DIREITA);
	posicoes[0][3] = new InfoPosicao(Lado.ESQUERDA, Lado.ATRAS);
	posicoes[1][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
	posicoes[2][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
	posicoes[3][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS); //Origem
	
	int[] coordenadaAtualDoRobo = new int[]{0, 0}; //Destino
	mapa.getCoordenadasNaoVisitadas();
	Caminho caminho = montarCaminhoAteProximaPosicao(mapa, coordenadaAtualDoRobo);
	System.out.println(caminho.toString());
	
    }

    public static Caminho montarCaminhoAteProximaPosicao(MapaLabirinto mapa, int[] coordenadaAtual) {
	Caminho caminho = null;
	int[] coordenadaVisinho = mapa.getVisinhoNaoVisitado(coordenadaAtual);

	if (coordenadaVisinho != null) {
	    caminho = new Caminho();
	    caminho.addPasso(coordenadaAtual);
	    caminho.addPasso(coordenadaVisinho);
	} else {
	    List<int[]> coordenadas = mapa.getCoordenadasNaoVisitadas();
	    for (int[] coord : coordenadas) {
		caminho = mapa.montarCaminho(coordenadaAtual, coord);
		if (caminho != null)
		    break;
	    }
	    if (caminho == null)
		System.out.println("Mapa completo");
	}
	caminho.toFirst();
	return caminho;
    }
}
