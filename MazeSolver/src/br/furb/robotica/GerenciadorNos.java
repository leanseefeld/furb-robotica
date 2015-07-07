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
	return index < 0 ? Integer.MAX_VALUE + index : index;
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

    /**
     * Encontra o vizinho do {@code noOrigem} no sentido informado.
     * 
     * @param noOrigem
     *            nó sendo analizado
     * @param sentido
     *            sentido a ser analizado
     * @param criarOuLigar
     *            {@code true} para criar o vizinho caso este seja nulo e/ou para ligar
     *            {@code noOrigem} com o vizinho no sentido informado.
     * @return
     */
    public No getVizinho(No noOrigem, Sentido sentido, boolean criarOuLigar) {
	int x = noOrigem.getX();
	int y = noOrigem.getY();

	switch (sentido) {
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
	No vizinho = linha.get(normalizeKey(y));

	if (criarOuLigar) {
	    if (vizinho == null) {
		vizinho = new No(x, y);
		linha.put(normalizeKey(y), vizinho);
	    }
	    vizinho.setVizinho(sentido.getOposto(), noOrigem);
	    noOrigem.setVizinho(sentido, vizinho);
	}

	return vizinho;
    }

}
