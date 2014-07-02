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
import br.furb.su.dataset.reader.SolicitacoesMatriculaWriter;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.SolicitacaoMatricula;

public class MatriculaCenterControle extends BaseCenterControle {

	private SolicitacoesMatriculaWriter writer;

	public MatriculaCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
	}

	public void insereSolicitacaoMatricula(SolicitacaoMatricula sol) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(CursoCenter.TIPO_CURSO).append('\n');
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			writer.gravarDados(Arrays.asList(sol), pw);
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

}
