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
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.writer.SolicitacaoDiplomaWriter;
import br.furb.su.modelo.dados.SolicitacaoDiploma;

public class DiplomaCenterControle extends BaseCenterControle {

	private jpvmTaskId tid;
	private jpvmEnvironment pvm;
	private SolicitacoesDiplomaReader solReader;
	private SolicitacaoDiplomaWriter solWriter;

	public DiplomaCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		this.tid = tid;
		this.pvm = pvm;
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

}
