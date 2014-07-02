package br.furb.su.mestre;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.writer.SolicitacaoDiplomaWriter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.SolicitacaoDiploma;

public class DiplomaCenterControle extends BaseCenterControle {

	private SolicitacoesDiplomaReader solReader;
	private SolicitacaoDiplomaWriter solWriter;

	public DiplomaCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
	}

	public SolicitacaoDiploma getSolicitacaoDiploma(long codAluno, int codCurso) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("aluno=%d;curso=%d", codAluno, codCurso));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv();
		final String bufferStr = msg.buffer.upkstr();
		checkErrorResponse(msg);
		return solReader.ler(bufferStr).get(0);
	}

	public void insereSolicitacaoDiploma(SolicitacaoDiploma sol) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			solWriter.gravarDados(Arrays.asList(sol), pw);
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

	public void setCursoCenter(jpvmTaskId taskId) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(String.format("cursoCenter.host=%s;cursoCenter.port=%d", taskId.getHost(), taskId.getPort()));
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.SET_SLAVE.tag());
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
	}

	public void setMensalidadeCenter(jpvmTaskId taskId) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(String.format("mensalidadeCenter.host=%s;mensalidadeCenter.port=%d", taskId.getHost(), taskId.getPort()));
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.SET_SLAVE.tag());
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
	}
	
	public void processaDiplomas() throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append("processaDiplomas");
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.OPERATION.tag());
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
	}

}
