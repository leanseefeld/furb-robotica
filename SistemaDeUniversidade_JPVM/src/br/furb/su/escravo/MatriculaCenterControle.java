package br.furb.su.escravo;

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
import br.furb.su.modelo.dados.SolicitacaoMatricula;

public class MatriculaCenterControle extends BaseCenterControle {

	private jpvmTaskId tid;
	private jpvmEnvironment pvm;
	private SolicitacoesMatriculaWriter writer;

	public MatriculaCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		this.pvm = pvm;
		this.tid = tid;
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

}
