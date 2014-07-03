package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.List;

import jpvm.jpvmException;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
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

	public static final String TIPO_CURSO = "curso";
	public static final String TIPO_DISCIPLINA = "disciplina";
	public static final String TIPO_HISTORICO = "historico";

	private final List<Curso> cursos;
	private final List<Disciplina> disciplinas;
	private final List<Historico> historicos;
	private final CursoReader cursoReader;
	private final DisciplinasReader disciplinasReader;
	private final HistoricosReader historicosReader;

	public CursoCenter() throws jpvmException {
		super();
		cursoReader = new CursoReader();
		disciplinasReader = new DisciplinasReader();
		historicosReader = new HistoricosReader();
		cursos = new ArrayList<>();
		disciplinas = new ArrayList<>();
		historicos = new ArrayList<>();
	}

	public static void main(String[] args) throws jpvmException {
		new CursoCenter().run();
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

	@Override
	protected void doUpload(String buffer) {
		int lineBreak = buffer.indexOf("\n");
		String tipo = buffer.substring(0, lineBreak).toLowerCase();
		String registros = buffer.substring(lineBreak + 1);
		last = buffer;
		switch (tipo) {
		case TIPO_CURSO:
			cursos.addAll(cursoReader.ler(registros));
			break;
		case TIPO_DISCIPLINA:
			disciplinas.addAll(disciplinasReader.ler(registros));
			break;
		case TIPO_HISTORICO:
			historicos.addAll(historicosReader.ler(registros));
			break;
		default:
			tryResponder(ResponseEscravo.FAILURE, String.format(MSG_TIPO_NAO_RECONHECIDO, tipo));
		}
		tryResponder(ResponseEscravo.OK, null);
	}

	@Override
	protected void doDownload(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doGet(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doOperation(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetSlave(String buffer) {
		// TODO Auto-generated method stub

	}

}
