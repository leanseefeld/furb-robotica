package br.furb.su.escravo;

import java.util.List;

import jpvm.jpvmException;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;

/**
 * Armazena {@link Curso Cursos}, {@link Disciplina Disciplinas} e
 * {@link Historico Historicos}.
 * 
 * @author wseefeld
 * 
 */
public class CursoCenter extends EscravoBase {

	private List<Curso> cursos;
	private List<Disciplina> disciplinas;
	private List<Historico> historicos;

	public CursoCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) throws jpvmException {
		new CursoCenter();
	}

	public void insereCurso(Curso curso) {
		cursos.add(curso);
	}

	public void insereDisciplina(Disciplina disciplina) {
		disciplinas.add(disciplina);
	}

	public void insereHistorico(Historico historico) {
		historicos.add(historico);
	}

}
