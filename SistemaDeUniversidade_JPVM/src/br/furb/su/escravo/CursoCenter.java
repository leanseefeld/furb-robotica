package br.furb.su.escravo;

import java.util.List;

import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
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

	private List<Curso> cursos;
	private List<Disciplina> disciplinas;
	private List<Historico> historicos;
	private final CursoReader cursoReader;
	private final DisciplinasReader disciplinasReader;
	private final HistoricosReader historicosReader;

	public CursoCenter() throws jpvmException {
		super();
		cursoReader = new CursoReader();
		disciplinasReader = new DisciplinasReader();
		historicosReader = new HistoricosReader();
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
		String registros = buffer.substring(lineBreak);
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
			try {
				responder(ResponseEscravo.FAILURE, String.format(MSG_TIPO_NAO_RECONHECIDO, tipo));
			} catch (jpvmException e) {
				// suprime, não há pra quem responder...
				e.printStackTrace();
			}
		}

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
	protected void doRemove(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doUpdate(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doLock(jpvmTaskId taskId, String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doUnlock(jpvmTaskId taskId, String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doOperation(jpvmMessage msg) {
		// TODO Auto-generated method stub

	}

}
