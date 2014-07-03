package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpvm.jpvmBuffer;
import jpvm.jpvmException;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.reader.HistoricosWriter;
import br.furb.su.dataset.writer.CursoWriter;
import br.furb.su.dataset.writer.DisciplinasWriter;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.SituacaoDisciplina;
import br.furb.su.operacoes.Operacao;

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
	public static final String GET_CURSO = "curso";
	public static final String GET_HISTORICO = "historico";
	public static final String GET_DISCIPLINA = "disciplina";
	public static final String GET_ALUNO_CURSOU = "aluno_cursou";
	public static final String GET_HISTORICOS_ATIVOS = "historicos_ativos";
	public static final String GET_HISTORICOS_MAIS_RECENTE = "historico_mais_recente";
	public static final String GET_TOTAL_DISCIPLINAS_NO_CURSO = "total_disciplinas_no_nurso";
	public static final String PARAM_COD_ALUNO = "codAluno";
	public static final String PARAM_COD_CURSO = "codCurso";
	public static final String PARAM_COD_DISCIPLINA = "codDisciplina";

	private final Map<Integer, Curso> cursos;
	private final Map<Integer, Disciplina> disciplinas;
	/**
	 * {@code Map<CodAluno, Map<CodCurso, List<Historico>>>}
	 */
	private final Map<Long, Map<Integer, List<Historico>>> historicos;
	private final CursoReader cursoReader;
	private final CursoWriter cursoWriter;
	private final DisciplinasReader disciplinasReader;
	private final DisciplinasWriter disciplinasWriter;
	private final HistoricosReader historicosReader;
	private final HistoricosWriter historicosWriter;

	public CursoCenter() throws jpvmException {
		super();
		cursoReader = new CursoReader();
		cursoWriter = new CursoWriter();
		disciplinasReader = new DisciplinasReader();
		disciplinasWriter = new DisciplinasWriter();
		historicosReader = new HistoricosReader();
		historicosWriter = new HistoricosWriter();
		cursos = new HashMap<>();
		disciplinas = new HashMap<>();
		historicos = new HashMap<>();
	}

	public static void main(String[] args) throws jpvmException {
		new CursoCenter().run();
	}

	public void insereCurso(Curso curso) {
		cursos.put(curso.getCod(), curso);
	}

	public void insereDisciplina(Disciplina disciplina) {
		disciplinas.put(disciplina.getCod(), disciplina);
	}

	public void insereHistorico(Historico historico) {
		Map<Integer, List<Historico>> alunoHis = historicos.get(historico.getCodAluno());
		if (alunoHis == null) {
			alunoHis = new HashMap<>();
			historicos.put(historico.getCodAluno(), alunoHis);
		}
		List<Historico> cursoHis = alunoHis.get(historico.getCodCurso());
		if (cursoHis == null) {
			cursoHis = new ArrayList<>();
			alunoHis.put(historico.getCodCurso(), cursoHis);
		}
		cursoHis.add(historico);
	}

	@Override
	protected void doUpload(String buffer) {
		int lineBreak = buffer.indexOf("\n");
		String tipo = buffer.substring(0, lineBreak).toLowerCase();
		String registros = buffer.substring(lineBreak + 1);
		last = buffer;
		switch (tipo) {
		case TIPO_CURSO:
			List<Curso> cursosRecebidos = cursoReader.ler(registros);
			for (Curso curso : cursosRecebidos) {
				insereCurso(curso);
			}
			break;
		case TIPO_DISCIPLINA:
			final List<Disciplina> discRecebidas = disciplinasReader.ler(registros);
			for (Disciplina disciplina : discRecebidas) {
				insereDisciplina(disciplina);
			}
			break;
		case TIPO_HISTORICO:
			List<Historico> histRecebido = historicosReader.ler(registros);
			for (Historico historico : histRecebido) {
				insereHistorico(historico);
			}
			break;
		default:
			tryResponder(ResponseEscravo.FAILURE, String.format(MSG_TIPO_NAO_RECONHECIDO, tipo));
		}
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doGet(String buffer) {
		Operacao getOp = converterGetParaOperacao(buffer);

		escolha: switch (getOp.getNome()) {
		case GET_CURSO:
			int codCurso = Integer.parseInt((String) getOp.getParam(PARAM_COD_CURSO));
			Curso curso = cursos.get(codCurso);
			tryResponder(ResponseEscravo.OK, writeToString(cursoWriter, Arrays.asList(curso)));
			break;
		case GET_HISTORICO:
			Map<Integer, List<Historico>> alunoHis = historicos.get(getOp.getParam(PARAM_COD_ALUNO));
			if (alunoHis != null) {
				codCurso = Integer.parseInt((String) getOp.getParam(PARAM_COD_CURSO));
				List<Historico> curHis = alunoHis.get(codCurso);
				if (curHis != null && !curHis.isEmpty()) {
					tryResponder(ResponseEscravo.OK, writeToString(historicosWriter, curHis));
					break;
				}
			}
			tryResponder(ResponseEscravo.OK, "");
			break;
		case GET_DISCIPLINA:
			Disciplina d = disciplinas.get(getOp.getParam(PARAM_COD_DISCIPLINA));
			tryResponder(ResponseEscravo.OK, writeToString(disciplinasWriter, Arrays.asList(d)));
			break;
		case GET_ALUNO_CURSOU:
			alunoHis = historicos.get(getOp.getParam(PARAM_COD_ALUNO));
			if (alunoHis != null) {
				Integer codDisciplina = ((Integer) getOp.getParam(PARAM_COD_DISCIPLINA)).intValue();
				for (List<Historico> historicos : alunoHis.values()) {
					for (Historico historico : historicos) {
						if (codDisciplina == historico.getCodDisciplina()) {
							tryResponder(ResponseEscravo.OK, Boolean.TRUE.toString());
							break escolha;
						}
					}
				}
			}
			tryResponder(ResponseEscravo.OK, Boolean.FALSE.toString());
			break;
		case GET_HISTORICOS_ATIVOS:
			alunoHis = historicos.get(getOp.getParam(PARAM_COD_ALUNO));
			List<Historico> ativos = new ArrayList<>();
			if (alunoHis != null) {
				for (List<Historico> historicos : alunoHis.values()) {
					for (Historico historico : historicos) {
						SituacaoDisciplina situacao = historico.getSituacao();
						if (situacao == SituacaoDisciplina.MATRICULADO || situacao == SituacaoDisciplina.CURSANDO) {
							ativos.add(historico);
						}
					}
				}
			}
			tryResponder(ResponseEscravo.OK, writeToString(historicosWriter, ativos));
			break;
		case GET_HISTORICOS_MAIS_RECENTE:
			List<Historico> historicosRecentes = new ArrayList<>();

			alunoHis = historicos.get(getOp.getParam(PARAM_COD_ALUNO));
			if (alunoHis != null) {
				codCurso = Integer.parseInt((String) getOp.getParam(PARAM_COD_CURSO));
				List<Historico> historicoCurso = alunoHis.get(codCurso);
				if (historicoCurso != null) {
					Map<Integer, Historico> historicoDisciplinas = new HashMap<>();
					for (Historico historico : historicoCurso) {
						int codDisc = historico.getCodDisciplina();
						Historico historicoAnterior = historicoDisciplinas.get(codDisc);
						if (historicoAnterior == null || historicoAnterior.getInicio().before(historico.getInicio())) {
							historicoDisciplinas.put(codDisc, historico);
						}
					}
					historicosRecentes.addAll(historicoDisciplinas.values());
				}
			}
			tryResponder(ResponseEscravo.OK, writeToString(historicosWriter, historicosRecentes));
			break;
		case GET_TOTAL_DISCIPLINAS_NO_CURSO:
			codCurso = Integer.parseInt((String) getOp.getParam(PARAM_COD_CURSO));
			curso = cursos.get(codCurso);
			if (curso == null) {
				tryResponder(ResponseEscravo.FAILURE, "curso não cadastrado: " + codCurso);
				break;
			}
			jpvmBuffer outBuffer = new jpvmBuffer();
			outBuffer.pack(curso.getCodDisciplinas().size());
			tryResponder(ResponseEscravo.OK, outBuffer);
			break;
		default:
			super.doGet(getOp.getNome());
			return;
		}

	}

	@Override
	protected void doDownload(String buffer) {
		if (buffer.equals("historico")) {
			List<Historico> todosHistoricos = new ArrayList<Historico>();
			Collection<Map<Integer, List<Historico>>> alunosHis = historicos.values();
			for (Map<Integer, List<Historico>> histCurso : alunosHis) {
				for (List<Historico> histDisciplinas : histCurso.values()) {
					todosHistoricos.addAll(histDisciplinas);
				}
			}

			tryResponder(ResponseEscravo.OK, writeToString(historicosWriter, todosHistoricos));
		} else {
			super.doDownload(buffer);
		}
	}

}
