package br.furb.su.model;

import java.util.Calendar;

public class Diploma {

	private Aluno aluno;
	private Curso curso;
	private Calendar data;

	public Diploma(Aluno aluno, Curso curso, Calendar data) {
		this.aluno = aluno;
		this.curso = curso;
		this.data = data;
	}

	public final Aluno getAluno() {
		return aluno;
	}

	public final Curso getCurso() {
		return curso;
	}

	public final Calendar getData() {
		return data;
	}

}
