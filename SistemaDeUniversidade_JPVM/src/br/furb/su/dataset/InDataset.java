package br.furb.su.dataset;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.furb.su.model.Aluno;
import br.furb.su.model.Curso;
import br.furb.su.model.Disciplina;
import br.furb.su.model.Historico;
import br.furb.su.model.Mensalidade;
import br.furb.su.model.SolicitacaoDiploma;
import br.furb.su.model.SolicitacaoMatricula;

public class InDataset {

	private final List<Mensalidade> mensalidades;
	private final List<SolicitacaoDiploma> solicitacoesDiploma;
	private final List<SolicitacaoMatricula> solicitacoesMatricula;
	private final List<Historico> historicos;
	private final Map<Integer, Curso> cursos;
	private final Map<Integer, Disciplina> disciplinas;
	private final Map<Long, Aluno> alunos;

	public InDataset() {
		mensalidades = new ArrayList<>();
		solicitacoesDiploma = new ArrayList<>();
		solicitacoesMatricula = new ArrayList<>();
		historicos = new ArrayList<>();
		cursos = new LinkedHashMap<>();
		disciplinas = new LinkedHashMap<>();
		alunos = new LinkedHashMap<>();
	}

	public List<Mensalidade> getMensalidades() {
		return mensalidades;
	}

	public Aluno findAluno(long codAluno) {
		return alunos.get(Long.valueOf(codAluno));
	}

	public List<SolicitacaoDiploma> getSolicitacoesDiploma() {
		return solicitacoesDiploma;
	}

	public List<SolicitacaoMatricula> getSolicitacoesMatricula() {
		return solicitacoesMatricula;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public List<Aluno> getAlunos() {
		return new ArrayList<>(alunos.values());
	}

	public Map<Long, Aluno> getAlunosMap() {
		return alunos;
	}

	public Map<Integer, Curso> getCursosMap() {
		return cursos;
	}

	public Map<Integer, Disciplina> getDisciplinasMap() {
		return disciplinas;
	}

	public Curso getCurso(int codCurso) {
		return cursos.get(codCurso);
	}

	public Disciplina getDisciplina(int codDisciplina) {
		return disciplinas.get(codDisciplina);
	}

}
