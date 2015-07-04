package br.furb.robotica;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class GerenciadorNos {

    private final Map<Integer, Map<Integer, No>> nos;

    public GerenciadorNos() {
	nos = new HashMap<>();
    }

    private static int normalizeKey(int index) {
	return index < 0 ? Integer.MAX_VALUE - index : index;
    }

    private Map<Integer, No> getLinhas(int x) {
	Map<Integer, No> linha = nos.get(normalizeKey(x));
	Debug.step("c2");
	if (linha == null) {
	    linha = new HashMap<>();
	    nos.put(normalizeKey(x), linha);
	    Debug.step("d2");
	}
	return linha;
    }

    public void salvarNo(No no) {
	Map<Integer, No> linhas = getLinhas(no.getX());
	linhas.put(normalizeKey(no.getY()), no);
    }

    public No getVizinho(No noOrigem, Sentido lado, boolean criar) {
	Debug.step("a2");
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
	Debug.step("b2");

	Map<Integer, No> linha = getLinhas(x);
	No vizinho = linha.get(normalizeKey(y));
	Debug.step("e2:" + vizinho);
	if (vizinho == null && criar) {
	    Debug.step("f2");
	    vizinho = new No(x, y);
	    Debug.step("g2");
	    vizinho.setVizinho(lado.getOposto(), noOrigem);
	    Debug.step("h2");
	    noOrigem.setVizinho(lado, vizinho);
	    Debug.step("i2");
	    linha.put(normalizeKey(y), vizinho);
	    Debug.step("j2");
	}

	return vizinho;
    }

}
