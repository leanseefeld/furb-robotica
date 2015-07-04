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
	while (!naoVisitados.isEmpty()) {
	    passoAtual = (Passo) naoVisitados.pop();
	    No noAtual = passoAtual.getNo();
	    if (noAtual == destino) {
		break;
	    }

	    for (Sentido sentido : Sentido.values()) {
		No vizinho = noAtual.getVizinho(sentido);
		if (vizinho != null && !visitados.contains(vizinho)) {
		    naoVisitados.push(new Passo(vizinho, passoAtual, sentido));
		}
	    }

	    visitados.add(noAtual);
	}

	if (passoAtual == null) {
	    return null;
	}

	Pilha<Passo> caminho = new PilhaImpl<>();
	while (passoAtual.getPai() != null) {
	    caminho.empilhar(passoAtual);
	    passoAtual = passoAtual.getPai();
	}
	return caminho;
    }
}
