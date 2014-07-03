package br.furb.su.modelo.dados;

import java.util.Calendar;

public class Diploma {

	private long aluno;
	private int curso;
	private Calendar data;

	public Diploma(long aluno, int curso, Calendar data) {
		this.aluno = aluno;
		this.curso = curso;
		this.data = data;
	}

	public final long getCodAluno() {
		return aluno;
	}

	public final int getCodCurso() {
		return curso;
	}

	public final Calendar getData() {
		return data;
	}

}
