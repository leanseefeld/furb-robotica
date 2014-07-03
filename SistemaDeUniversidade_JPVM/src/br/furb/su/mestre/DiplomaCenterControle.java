package br.furb.su.mestre;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.DiplomaReader;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.writer.SolicitacaoDiplomaWriter;
import br.furb.su.escravo.DiplomaCenter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.Diploma;
import br.furb.su.modelo.dados.SolicitacaoDiploma;
import br.furb.su.operacoes.OperacaoFactory;

public class DiplomaCenterControle extends BaseCenterControle {

	private final SolicitacoesDiplomaReader solReader = new SolicitacoesDiplomaReader();
	private final SolicitacaoDiplomaWriter solWriter = new SolicitacaoDiplomaWriter();
	private final DiplomaReader diplomaReader = new DiplomaReader();

	public DiplomaCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
	}

	public SolicitacaoDiploma getSolicitacaoDiploma(long codAluno, int codCurso) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\ncodAluno=%d;codCurso=%d", DiplomaCenter.GET_DIPLOMA, codAluno, codCurso));
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
		super.setEscravo(taskId, "cursoCenter");
	}

	public void setMensalidadeCenter(jpvmTaskId taskId) throws jpvmException {
		super.setEscravo(taskId, "mensalidadeCenter");
	}

	public void processaDiplomas(Calendar dataAtual) throws jpvmException {
		async_enviaOperacao(OperacaoFactory.processarDiplomas(dataAtual));
	}

	public Collection<Diploma> downloadDiplomas() throws jpvmException {
		super.requestDownload("diplomas");
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
		return diplomaReader.ler(msg.buffer.upkstr());
	}

}
