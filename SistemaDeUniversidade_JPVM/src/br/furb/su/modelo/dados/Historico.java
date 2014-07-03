package br.furb.su.modelo.dados;

import java.util.Calendar;

public class Historico {

	private long codAluno;
	private int codDisciplina;
	private int codCurso;
	private Calendar inicio;
	private SituacaoDisciplina situacao;

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

	public int getCodDisciplina() {
		return codDisciplina;
	}

	public SituacaoDisciplina getSituacao() {
		return situacao;
	}

	public int getCodCurso() {
		return codCurso;
	}

	public Calendar getInicio() {
		return inicio;
	}

}
