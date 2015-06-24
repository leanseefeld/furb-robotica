package br.furb.robotica;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import util.Pilha;
import util.PilhaImpl;

public class BuscaLargura {

    public static Pilha<Passo> getMenorCaminho(No origem, No destino) {
	Collection<No> visitados = new LinkedList<>();
	Queue<Passo> naoVisitados = new Queue<>();

	naoVisitados.push(new Passo(origem, null, null));

	Passo passoAtual = null;
	while ((passoAtual = (Passo) naoVisitados.pop()) != null) {
	    No noAtual = passoAtual.getNo();
	    if (noAtual == destino) {
		break;
	    }

	    for (Sentido sentido : Sentido.values()) {
		No vizinho = noAtual.getVizinho(sentido);
		if (vizinho != null && vizinho.isVisitado() && !visitados.contains(vizinho)) {
		    naoVisitados.push(new Passo(vizinho, passoAtual, sentido));
		}
	    }

	    visitados.add(noAtual);
	}

	if (passoAtual == null) {
	    return null;
	}

	Pilha<Passo> caminho = new PilhaImpl<>();
	caminho.empilhar(passoAtual);
	while ((passoAtual = passoAtual.getPai()) != null) {
	    caminho.empilhar(passoAtual);
	}
	return caminho;
    }
}
