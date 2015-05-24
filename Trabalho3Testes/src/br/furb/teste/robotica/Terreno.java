package br.furb.teste.robotica;

import br.furb.robotica.InfoPosicao;
import br.furb.robotica.Lado;
import br.furb.robotica.Matriz;
import br.furb.robotica.common.Coordenada;

public class Terreno {

    public static boolean isDestino(int[] coordenadaAtual) {
	return Coordenada.comparar(coordenadaAtual, Coordenada.criar(0, 0));
    }

    public static void verificarPosicao(int[] coordenadaAtual, InfoPosicao infoPosicao) {

	switch (coordenadaAtual[Matriz.COLUNA]) {
	    case 0:
		switch (coordenadaAtual[Matriz.LINHA]) {
		    case 0:
			infoPosicao.setLadoLivre(Lado.ATRAS);
			break;
		    case 1:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.ATRAS);
			break;
		    case 2:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.ATRAS);
			break;
		    case 3:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.DIREITA);
			break;
		}
		break;
	    case 1:
		switch (coordenadaAtual[Matriz.LINHA]) {
		    case 0:
			infoPosicao.setLadoLivre(Lado.ATRAS);
			break;
		    case 1:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.DIREITA, Lado.ATRAS);
			break;
		    case 2:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.ATRAS);
			break;
		    case 3:
			infoPosicao.setLadoLivre(Lado.ESQUERDA, Lado.FRENTE, Lado.DIREITA);
			break;
		}
		break;
	    case 2:
		switch (coordenadaAtual[Matriz.LINHA]) {
		    case 0:
			infoPosicao.setLadoLivre(Lado.ATRAS, Lado.DIREITA);
			break;
		    case 1:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.ESQUERDA, Lado.ATRAS);
			break;
		    case 2:
			infoPosicao.setLadoLivre(Lado.FRENTE);
			break;
		    case 3:
			infoPosicao.setLadoLivre(Lado.ESQUERDA);
			break;
		}
		break;
	    case 3:
		switch (coordenadaAtual[Matriz.LINHA]) {
		    case 0:
			infoPosicao.setLadoLivre(Lado.ESQUERDA, Lado.ATRAS);
			break;
		    case 1:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.ATRAS);
			break;
		    case 2:
			infoPosicao.setLadoLivre(Lado.FRENTE, Lado.ATRAS);
			break;
		    case 3:
			infoPosicao.setLadoLivre(Lado.FRENTE);
			break;
		}
		break;
	}
    }
}
