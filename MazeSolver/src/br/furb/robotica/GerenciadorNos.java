package br.furb.robotica;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class GerenciadorNos {

    private final Map<Integer, Map<Integer, No>> nos;

    public GerenciadorNos() {
	nos = new HashMap<>();
    }

    private Map<Integer, No> getLinhas(int x) {
	Map<Integer, No> linha = nos.get(x);
	if (linha == null) {
	    linha = new HashMap<>();
	    nos.put(x, linha);
	}
	return linha;
    }

    public void salvarNo(No no) {
	Map<Integer, No> linhas = getLinhas(no.getX());
	linhas.put(no.getY(), no);
    }

    public No getVizinho(No noOrigem, Sentido lado, boolean criar) {
	int x = noOrigem.getX();
	int y = noOrigem.getY();

	switch (lado) {
	    case SUL:
		y--;
		break;
	    case NORTE:
		y++;
		break;
	    case LESTE:
		x++;
		break;
	    case OESTE:
		x--;
		break;
	}

	Map<Integer, No> linha = getLinhas(x);
	No vizinho = linha.get(y);
	if (vizinho == null && criar) {
	    vizinho = new No(x, y);
	    vizinho.setVizinho(lado.getOposto(), noOrigem);
	    noOrigem.setVizinho(lado, vizinho);
	    linha.put(y, vizinho);
	}

	return vizinho;
    }

}
