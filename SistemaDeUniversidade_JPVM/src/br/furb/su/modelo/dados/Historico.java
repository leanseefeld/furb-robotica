package br.furb.su.modelo.dados;

import java.util.Calendar;

public class Historico {

	private long codAluno;
	private Aluno aluno;
	private Disciplina disciplina;
	private Curso curso;
	private Calendar inicio;
	private SituacaoDisciplina situacao;
	private int codDisciplina;
	private int codCurso;

	public Historico(Aluno aluno, Disciplina disciplina, Curso curso, SituacaoDisciplina situacao, Calendar inicio) {
		this(aluno.getCod(), disciplina.getCod(), curso.getCod(), situacao, inicio);
		this.aluno = aluno;
		this.disciplina = disciplina;
		this.curso = curso;
	}

	public Historico(long codAluno, int codDisciplina, int codCurso, SituacaoDisciplina situacao, Calendar inicio) {
		this.codAluno = codAluno;
		this.codDisciplina = codDisciplina;
		this.codCurso = codCurso;
		this.situacao = situacao;
		this.inicio = inicio;
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

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public int getCodDisciplina() {
		return codDisciplina;
	}

	public SituacaoDisciplina getSituacao() {
		return situacao;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int getCodCurso() {
		return codCurso;
	}

	public Calendar getInicio() {
		return inicio;
	}

}
