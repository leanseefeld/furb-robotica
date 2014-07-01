package br.furb.su.modelo.dados;

public class Disciplina {

	private int cod;
	private String nome;
	private double valorMatricula;
	private double valorMensal;

	public Disciplina(int cod, String nome, double valorMatricula, double valorMensal) {
		this.cod = cod;
		this.nome = nome;
		this.valorMatricula = valorMatricula;
		this.valorMensal = valorMensal;
	}

	public int getCod() {
		return cod;
	}

	public String getNome() {
		return nome;
	}

	public double getValorMatricula() {
		return valorMatricula;
	}

	public double getValorMensal() {
		return valorMensal;
	}

}
