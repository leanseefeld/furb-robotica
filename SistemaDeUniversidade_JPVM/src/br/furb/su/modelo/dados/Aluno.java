package br.furb.su.modelo.dados;

public class Aluno {

	private long cod;
	private String nome;
	private boolean ativo;

	public Aluno(long cod, String nome, boolean ativo) {
		this.cod = cod;
		this.nome = nome;
		this.ativo = ativo;
	}

	public String getNome() {
		return nome;
	}

	public long getCod() {
		return cod;
	}

	public boolean isAtivo() {
		return ativo;
	}

}
