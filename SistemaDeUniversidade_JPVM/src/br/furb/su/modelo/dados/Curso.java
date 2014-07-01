package br.furb.su.modelo.dados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Curso {

	private int cod;
	private String nome;
	private Map<Integer, Disciplina> disciplinas;

	public Curso(int cod, String nome, List<Integer> codsDisciplinas) {
		this.cod = cod;
		this.nome = nome;
		this.disciplinas = new LinkedHashMap<>(codsDisciplinas.size());
		for (Integer codDisciplina : codsDisciplinas) {
			disciplinas.put(codDisciplina, null);
		}
	}

	public int getCod() {
		return cod;
	}

	public String getNome() {
		return nome;
	}

	public List<Disciplina> getDisciplinas() {
		return new ArrayList<>(disciplinas.values());
	}

	public List<Historico> filtrarHistoricos(List<Historico> historicos) {
		Map<Disciplina, Historico> historicosCurso = new HashMap<>();

		for (Historico historico : historicos) {
			final Disciplina disciplina = historico.getDisciplina();
			if (disciplinas.containsKey(disciplina.getCod())) {
				final Historico historicoAnterior = historicosCurso.get(disciplina);
				if (historicoAnterior == null || historicoAnterior.getInicio().before(historico.getInicio())) {
					historicosCurso.put(disciplina, historico);
				}
			}
		}

		return new ArrayList<>(historicosCurso.values());
	}

	public void resolver(Map<Integer, Disciplina> disciplinas) {
		for (Integer codDisciplina : this.disciplinas.keySet()) {
			this.disciplinas.put(codDisciplina, disciplinas.get(codDisciplina));
		}
	}

}
