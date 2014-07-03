package br.furb.su.modelo.dados;

public class SolicitacaoDiploma {

	private long codAluno;
	private int codCurso;

	public SolicitacaoDiploma(long codAluno, int codCurso) {
		this.codAluno = codAluno;
		this.codCurso = codCurso;
	}

	public long getCodAluno() {
		return codAluno;
	}

	public int getCodCurso() {
		return codCurso;
	}

}
