package br.furb.su.modelo.dados;

import java.util.Calendar;

import br.furb.su.Sistema;

public class Mensalidade {

	private long codAluno;
	private Aluno aluno;
	private boolean isPaga;
	private Calendar vencimento, competencia;
	private double valor;

	public Mensalidade(long codAluno, double valor, Calendar competencia, Calendar vencimento, boolean isPaga) {
		this.codAluno = codAluno;
		this.valor = valor;
		this.competencia = competencia;
		this.vencimento = vencimento;
		this.isPaga = isPaga;
	}

	public Mensalidade(Aluno aluno, double valor, Calendar competencia, Calendar vencimento, boolean isPaga) {
		this(aluno.getCod(), valor, competencia, vencimento, isPaga);
		this.aluno = aluno;
	}

	public long getCodAluno() {
		return codAluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public boolean isPaga() {
		return isPaga;
	}

	public Calendar getVencimento() {
		return vencimento;
	}

	public double calculaMulta() {
		return valor * Sistema.PERCENTUAL_MULTA;
	}

	public Calendar getCompetencia() {
		return competencia;
	}

	public double getValor() {
		return valor;
	}

}
