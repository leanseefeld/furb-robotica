package br.furb.su.modelo.dados;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

	private long cod;
	private String nome;
	private final List<Mensalidade> mensalidades;
	private final List<Historico> historicos;
	private boolean possuiMensalidadeAtrasada;
	private boolean ativo;

	public Aluno(long cod, String nome, boolean ativo) {
		this.cod = cod;
		this.nome = nome;
		this.ativo = ativo;
		mensalidades = new ArrayList<>();
		historicos = new ArrayList<>();
	}

	public void addMensalidade(Mensalidade mensalidade) {
		mensalidades.add(mensalidade);
	}

	public void addHistorico(Historico historico) {
		historicos.add(historico);
	}

	public List<Mensalidade> getMensalidades() {
		return mensalidades;
	}

	public boolean possuiMensalidadeAtrasada() {
		return possuiMensalidadeAtrasada;
	}

	public void setMensalidadeAtrasada(boolean atrasada) {
		this.possuiMensalidadeAtrasada = atrasada;
	}

	public String getNome() {
		return nome;
	}

	public long getCod() {
		return cod;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public boolean cursou(Disciplina disciplina) {
		for (Historico historico : historicos) {
			if (historico.getDisciplina().equals(disciplina)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public List<Historico> getHistoricosAtivos() {
		List<Historico> historicos = new ArrayList<>();
		for (Historico historico : this.historicos) {
			final SituacaoDisciplina situacao = historico.getSituacao();
			if (situacao == SituacaoDisciplina.MATRICULADO || situacao == SituacaoDisciplina.CURSANDO) {
				historicos.add(historico);
			}
		}
		return historicos;
	}

}
