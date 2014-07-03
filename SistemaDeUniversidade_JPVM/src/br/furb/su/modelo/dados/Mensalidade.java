package br.furb.su.modelo.dados;

import java.util.Calendar;

import br.furb.su.Sistema;

public class Mensalidade {

	private long codAluno;
	private boolean isPaga;
	private Calendar vencimento, competencia;
	private double valor;
	private boolean atrasada;

	public Mensalidade(long codAluno, double valor, Calendar competencia, Calendar vencimento, boolean isPaga) {
		this.codAluno = codAluno;
		this.valor = valor;
		this.competencia = competencia;
		this.vencimento = vencimento;
		this.isPaga = isPaga;
	}

	public long getCodAluno() {
		return codAluno;
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

	public void setAtrasada(boolean atrasada) {
		this.atrasada = atrasada;
	}

	public boolean isAtrasada() {
		return atrasada;
	}

}
