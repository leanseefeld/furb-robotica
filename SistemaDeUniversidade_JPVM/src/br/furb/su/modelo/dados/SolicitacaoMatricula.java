package br.furb.su.modelo.dados;

public class SolicitacaoMatricula {

	private long codAluno;
	private int codDisciplina;
	private int codCurso;
	private Aluno aluno;
	private Disciplina disciplina;
	private Curso curso;

	public SolicitacaoMatricula(long codAluno, int codDisciplina, int codCurso) {
		this.codAluno = codAluno;
		this.codDisciplina = codDisciplina;
		this.codCurso = codCurso;
	}

	public final int getCodDisciplina() {
		return codDisciplina;
	}

	public final int getCodCurso() {
		return codCurso;
	}

	public final void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public final void setCurso(Curso curso) {
		this.curso = curso;
	}

	public long getCodAluno() {
		return codAluno;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public Curso getCurso() {
		return curso;
	}

}
