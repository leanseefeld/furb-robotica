package br.furb.su.operacoes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Operacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3566612867383090653L;
	private String nome;
	private Map<String, Serializable> params;

	public Operacao(String nome) {
		this.nome = nome;
		this.params = new HashMap<>(5);
	}

	public String getNome() {
		return nome;
	}

	public Map<String, Serializable> getParams() {
		return params;
	}

	public void setParam(String nome, Serializable valor) {
		params.put(nome.toUpperCase(), valor);
	}

	public Serializable getParam(String nome) {
		return params.get(nome.toUpperCase());
	}

	public void removeParam(String nome) {
		params.remove(nome.toUpperCase());
	}

	@Override
	public String toString() {
		return "nome=" + getNome() + ",params=" + params.toString();
	}

}
