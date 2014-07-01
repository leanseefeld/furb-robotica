package br.furb.su.model;

public class SolicitacaoDiploma {

	private long codAluno;
	private int codCurso;
	private Aluno aluno;
	private Curso curso;

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

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public final Curso getCurso() {
		return curso;
	}

	public final void setCurso(Curso curso) {
		this.curso = curso;
	}

}
