package br.furb.su.mestre;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.reader.HistoricosWriter;
import br.furb.su.dataset.writer.CursoWriter;
import br.furb.su.dataset.writer.DisciplinasWriter;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.SituacaoDisciplina;

/**
 * Controla o acesso ao escravo {@code CursoCenter}.
 * 
 * @author wseefeld
 * 
 */
public class CursoCenterControle extends BaseCenterControle {

	private final CursoReader cursoReader;
	private final CursoWriter cursoWriter;
	private final DisciplinasWriter disciplinasWriter;
	private final DisciplinasReader disciplinasReader;
	private final HistoricosReader historicosReader;
	private final HistoricosWriter historicosWriter;

	public CursoCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
		cursoReader = new CursoReader();
		cursoWriter = new CursoWriter();
		disciplinasWriter = new DisciplinasWriter();
		disciplinasReader = new DisciplinasReader();
		historicosReader = new HistoricosReader();
		historicosWriter = new HistoricosWriter();
	}

	public Curso getCurso(int cod) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d", //
				CursoCenter.GET_CURSO, //
				CursoCenter.PARAM_COD_CURSO, cod));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		final String bufferStr = msg.buffer.upkstr();
		checkErrorResponse(msg);
		return cursoReader.ler(bufferStr).get(0);
	}

	public void insereCurso(Curso curso) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(CursoCenter.TIPO_CURSO).append('\n');
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			cursoWriter.gravarDados(Arrays.asList(curso), pw);
			comando.append(sw.getBuffer().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.UPLOAD.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
	}

	public List<Historico> getHistorico(long codAluno, int codCurso) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d;%s=%d", //
				CursoCenter.GET_HISTORICO, //
				CursoCenter.PARAM_COD_ALUNO, codAluno, //
				CursoCenter.PARAM_COD_CURSO, codCurso));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		final String bufferStr = msg.buffer.upkstr();
		return historicosReader.ler(bufferStr);
	}

	public void insereHistorico(Historico historico) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(CursoCenter.TIPO_HISTORICO).append('\n');
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			historicosWriter.gravarDados(Arrays.asList(historico), pw);
			comando.append(sw.getBuffer().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.UPLOAD.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
	}

	public void insereDisciplina(Disciplina disciplina) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(CursoCenter.TIPO_DISCIPLINA).append('\n');
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			disciplinasWriter.gravarDados(Arrays.asList(disciplina), pw);
			comando.append(sw.getBuffer().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.UPLOAD.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
	}

	public boolean alunoCursou(long aluno, int disciplina) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d;%s=%d", // 
				CursoCenter.GET_ALUNO_CURSOU, // 
				CursoCenter.PARAM_COD_ALUNO, aluno, //
				CursoCenter.PARAM_COD_DISCIPLINA, disciplina));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return Boolean.valueOf(msg.buffer.upkstr());
	}

	/**
	 * Retorna as disciplinas do histórico do aluno na situação
	 * {@link SituacaoDisciplina#MATRICULADO} ou
	 * {@link SituacaoDisciplina#CURSANDO}.
	 */
	public List<Historico> getHistoricosAtivos(long codAluno) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d", //
				CursoCenter.GET_HISTORICOS_ATIVOS, //
				CursoCenter.PARAM_COD_ALUNO, codAluno));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return historicosReader.ler(msg.buffer.upkstr());
	}

	/**
	 * Retorna apenas as disciplinas mais recentes do histórico, ou seja,
	 * desconsidera duplicatas.
	 */
	public List<Historico> getHistoricoMaisRecente(long aluno, int curso) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d;%s=%d", //
				CursoCenter.GET_HISTORICOS_MAIS_RECENTE, //
				CursoCenter.PARAM_COD_ALUNO, aluno, // 
				CursoCenter.PARAM_COD_CURSO, curso));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return historicosReader.ler(msg.buffer.upkstr());
	}

	public int getTotalDisciplinasCurso(int codCurso) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d", //
				CursoCenter.GET_TOTAL_DISCIPLINAS_NO_CURSO, // 
				CursoCenter.PARAM_COD_CURSO, codCurso));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return msg.buffer.upkint();
	}

	public Disciplina getDisciplina(int codDisciplina) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d", //
				CursoCenter.GET_DISCIPLINA, //
				CursoCenter.PARAM_COD_DISCIPLINA, codDisciplina));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return disciplinasReader.ler(msg.buffer.upkstr()).get(0);
	}

	public Collection<Historico> downloadHistorico() throws jpvmException {
		super.requestDownload("historico");
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return historicosReader.ler(msg.buffer.upkstr());
	}

}
