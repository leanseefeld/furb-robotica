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
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.dataset.writer.MensalidadesWriter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.Mensalidade;

public class MensalidadeCenterControle extends BaseCenterControle {

	private MensalidadesReader reader;
	private MensalidadesWriter writer;

	public MensalidadeCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
	}

	public List<Mensalidade> getMensalidade(int codAluno) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("codAluno=%d", codAluno));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv();
		final String bufferStr = msg.buffer.upkstr();
		checkErrorResponse(msg);
		return reader.ler(bufferStr);
	}

	public void insereMensalidade(Mensalidade m) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			writer.gravarDados(Arrays.asList(m), pw);
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
