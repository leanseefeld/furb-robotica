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
import br.furb.su.Sistema;
import br.furb.su.dataset.reader.SolicitacoesMatriculaWriter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.SolicitacaoMatricula;
import br.furb.su.operacoes.OperacaoFactory;

public class MatriculaCenterControle extends BaseCenterControle {

	private final SolicitacoesMatriculaWriter writer = new SolicitacoesMatriculaWriter();

	public MatriculaCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
	}

	public void insereSolicitacaoMatricula(SolicitacaoMatricula sol) throws jpvmException {
		StringBuilder comando = new StringBuilder();
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
		super.setEscravo(taskId, "cursoCenter");
	}

	public void setMensalidadeCenter(jpvmTaskId taskId) throws jpvmException {
		super.setEscravo(taskId, "mensalidadeCenter");
	}

	public void processarMatriculas() throws jpvmException {
		async_enviaOperacao(OperacaoFactory.processarMatriculas(Sistema.getDataAtual()));
	}

}
