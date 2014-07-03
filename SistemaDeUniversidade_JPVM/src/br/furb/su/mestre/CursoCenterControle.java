package br.furb.su.mestre;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.Sistema;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.reader.HistoricosWriter;
import br.furb.su.dataset.writer.CursoWriter;
import br.furb.su.dataset.writer.DisciplinasWriter;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;

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
	private final HistoricosReader historicosReader;
	private final HistoricosWriter historicosWriter;

	public CursoCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
		cursoReader = new CursoReader();
		cursoWriter = new CursoWriter();
		disciplinasWriter = new DisciplinasWriter();
		historicosReader = new HistoricosReader();
		historicosWriter = new HistoricosWriter();
	}

	public Curso getCurso(int cod) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack("curso=".concat(String.valueOf(cod)));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv();
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
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
	}

	public List<Historico> getHistorico(long codAluno, int codCurso) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("aluno=%d;curso=%d", codAluno, codCurso));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv();
		final String bufferStr = msg.buffer.upkstr();
		checkErrorResponse(msg);
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
		jpvmMessage msg = pvm.pvm_recv();
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
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
	}

}
