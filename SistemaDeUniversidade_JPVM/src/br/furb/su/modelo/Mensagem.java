package br.furb.su.modelo;

public class Mensagem {

	private long aluno;
	private String etapa;
	private String mensagem;

	public Mensagem(long aluno, String etapa, String mensagem) {
		this.aluno = aluno;
		this.etapa = etapa;
		this.mensagem = mensagem;
	}

	public Mensagem(long aluno, String mensagem) {
		this(aluno, null, mensagem);
	}

	public long getAluno() {
		return aluno;
	}

	public String getEtapa() {
		return etapa;
	}

	public String getMensagem() {
		return mensagem;
	}

}
