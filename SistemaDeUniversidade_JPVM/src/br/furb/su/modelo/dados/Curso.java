package br.furb.su.modelo.dados;

import java.util.List;

public class Curso {

	private int cod;
	private String nome;
	private List<Integer> disciplinas;

	public Curso(int cod, String nome, List<Integer> codsDisciplinas) {
		this.cod = cod;
		this.nome = nome;
		disciplinas = codsDisciplinas;
	}

	public int getCod() {
		return cod;
	}

	public String getNome() {
		return nome;
	}

	public List<Integer> getCodDisciplinas() {
		return this.disciplinas;
	}

}
