package br.furb.su.escravo;

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
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.writer.CursoWriter;
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

	private jpvmTaskId tid;
	private jpvmEnvironment pvm;
	private final CursoReader cursoReader;
	private final DisciplinasReader disciplinasReader;
	private final HistoricosReader historicosReader;
	private final CursoWriter cursoWriter;

	public CursoCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		this.pvm = pvm;
		this.tid = tid;
		cursoReader = new CursoReader();
		disciplinasReader = new DisciplinasReader();
		historicosReader = new HistoricosReader();
		cursoWriter = new CursoWriter();
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

	private static void checkErrorResponse(jpvmMessage msg) throws jpvmException {
		if (msg.messageTag == ResponseEscravo.LOCKED.tag()) {
			throw new LockException(msg.buffer.upkstr());
		}
		if (msg.messageTag == ResponseEscravo.FAILURE.tag()) {
			throw new SlaveException(msg.buffer.upkstr());
		}
	}

}
