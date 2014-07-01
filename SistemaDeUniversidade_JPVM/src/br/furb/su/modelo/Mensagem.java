package br.furb.su.modelo;

import br.furb.su.modelo.dados.Aluno;

public class Mensagem {

	private Aluno aluno;
	private String etapa;
	private String mensagem;

	public Mensagem(Aluno aluno, String etapa, String mensagem) {
		this.aluno = aluno;
		this.etapa = etapa;
		this.mensagem = mensagem;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public String getEtapa() {
		return etapa;
	}

	public String getMensagem() {
		return mensagem;
	}

}
