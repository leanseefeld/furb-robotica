package br.furb.su.escravo;

import java.util.List;

import jpvm.jpvmTaskId;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;

/**
 * Controla o acesso ao escravo {@code CursoCenter}.
 * 
 * @author wseefeld
 * 
 */
public class CursoCenterControle {

	private jpvmTaskId[] tids;

	public CursoCenterControle(jpvmTaskId[] tids) {
		this.tids = tids;
	}

	public Curso getCurso(int cod) {
		// TODO
		return null;
	}

	public void insereCurso(Curso curso) {
		// TODO: lançar exceção em caso de problema
	}

	public List<Historico> getHistorico(long codAluno) {
		// TODO
		return null;
	}

	public void insereHistorico(Historico historico) {
		// TODO: lançar exceção em caso de problema
	}

	public void insereDisciplina(Disciplina disciplina) {
		// TODO: lançar exceção em caso de problema
	}

}
