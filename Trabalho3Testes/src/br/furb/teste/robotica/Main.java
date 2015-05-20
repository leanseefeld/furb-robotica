package br.furb.teste.robotica;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import br.furb.robotica.Caminho;
import br.furb.robotica.InfoPosicao;
import br.furb.robotica.Lado;

public class Main {

    private static Queue<int[]> caminhosNaoVisitados;

    public static void main(String[] args) {
	caminhosNaoVisitados = new LinkedList<int[]>();
	System.out.println("Trabalho3Testes iniciou");
	MapaLabirinto mapa = new MapaLabirinto();
	mapa.setCoordenadaDestino(0, 0);
	mapa.setCoordenadaOrigem(3, 3);

	InfoPosicao[][] posicoes = mapa.getPosicoes();
	posicoes[0][0] = new InfoPosicao(Lado.ATRAS); //Destino
	posicoes[1][0] = new InfoPosicao(Lado.ATRAS, Lado.FRENTE);
	posicoes[2][0] = new InfoPosicao(Lado.ATRAS, Lado.FRENTE);
	posicoes[3][0] = new InfoPosicao(Lado.FRENTE, Lado.DIREITA);
	caminhosNaoVisitados.add(new int[] { 3, 2 });
	posicoes[3][1] = new InfoPosicao(Lado.ESQUERDA, Lado.FRENTE, Lado.ESQUERDA);
	posicoes[2][1] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
	caminhosNaoVisitados.add(new int[] { 0, 1 });
	posicoes[1][1] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS, Lado.DIREITA);
	caminhosNaoVisitados.add(new int[] { 2, 2 });
	posicoes[1][2] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS, Lado.ESQUERDA);
	posicoes[0][2] = new InfoPosicao(Lado.ATRAS, Lado.DIREITA);
	posicoes[0][3] = new InfoPosicao(Lado.ESQUERDA, Lado.ATRAS);
	posicoes[1][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
	posicoes[2][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS);
	posicoes[3][3] = new InfoPosicao(Lado.FRENTE, Lado.ATRAS); //Origem

	int[] coordenadaAtualDoRobo = new int[] { 0, 0 }; //Destino
	mapa.getCoordenadasNaoVisitadas();
	Caminho caminho = montarCaminhoAteProximaPosicao(mapa, coordenadaAtualDoRobo);
	System.out.println(caminho.toString());

	System.out.println("FIM");
    }

    public static Caminho montarCaminhoAteProximaPosicao(MapaLabirinto mapa, int[] coordenadaAtual) {
	Caminho caminho = null;
	List<int[]> coordenadaVisinho = mapa.getVisinhosNaoVisitado(coordenadaAtual);

	if (!coordenadaVisinho.isEmpty()) {
	    caminho = new Caminho();
	    caminho.addPasso(coordenadaAtual);
	    caminho.addPasso(coordenadaVisinho.get(0));
	    for (int i = 1; i < coordenadaVisinho.size(); i++) {
		caminhosNaoVisitados.add(coordenadaVisinho.get(i));
	    }
	} else {
	    if (!caminhosNaoVisitados.isEmpty()) {
		int[] coord = caminhosNaoVisitados.peek();
		caminho = mapa.montarCaminho(coordenadaAtual, coord);
	    } else {
		System.out.println("Mapa ja foi totalmente explorado");
		return null;
	    }
	}
	caminho.toFirst();
	return caminho;
    }
}
