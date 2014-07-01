package br.furb.su.dataset;

import java.util.ArrayList;
import java.util.List;

import br.furb.su.Mensagem;
import br.furb.su.model.Diploma;
import br.furb.su.model.Historico;
import br.furb.su.model.Mensalidade;

public class OutDataset {

	private final List<Mensagem> avisos;
	private final List<Mensagem> problemas;
	private final List<Historico> historicos;
	private final List<Mensalidade> mensalidades;
	private final List<Diploma> diplomas;

	public OutDataset() {
		this.avisos = new ArrayList<>();
		this.problemas = new ArrayList<>();
		this.historicos = new ArrayList<>();
		this.mensalidades = new ArrayList<>();
		this.diplomas = new ArrayList<>();
	}

	public void addProblema(Mensagem problema) {
		problemas.add(problema);
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void addAviso(Mensagem aviso) {
		avisos.add(aviso);
	}

	public void addMensalidade(Mensalidade mensalidade) {
		mensalidades.add(mensalidade);
	}

	public List<Mensalidade> getMensalidades() {
		return mensalidades;
	}

	public List<Mensagem> getAvisos() {
		return avisos;
	}

	public List<Mensagem> getProblemas() {
		return problemas;
	}

	public void addDiploma(Diploma diploma) {
		diplomas.add(diploma);
	}

	public List<Diploma> getDiplomas() {
		return diplomas;
	}

}
