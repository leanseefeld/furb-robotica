package br.furb.su.mestre;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.escravo.EscravoBase;
import br.furb.su.escravo.LockException;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.escravo.ResponseEscravo;
import br.furb.su.escravo.SlaveException;
import br.furb.su.modelo.Mensagem;
import br.furb.su.operacoes.Operacao;

public abstract class BaseCenterControle {

	protected final jpvmTaskId tid;
	protected final jpvmEnvironment pvm;

	public BaseCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		this.tid = tid;
		this.pvm = pvm;
	}

	protected static void checkErrorResponse(jpvmMessage msg) throws jpvmException {
		if (msg.messageTag == ResponseEscravo.LOCKED.tag()) {
			throw new LockException(msg.buffer.upkstr());
		}
		if (msg.messageTag == ResponseEscravo.FAILURE.tag() || msg.messageTag == ResponseEscravo.ERROR.tag()) {
			throw new SlaveException(msg.buffer.upkstr());
		}
	}

	public jpvmTaskId getTaskId() {
		return tid;
	}

	public void async_enviaOperacao(Operacao op) throws jpvmException {
		byte[] bytes = EscravoBase.serializar(op);
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(bytes.length);
		buffer.pack(bytes, bytes.length, 1);
		pvm.pvm_send(buffer, tid, RequestEscravo.OPERATION.tag());
	}

	public void waitResposta() throws jpvmException {
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
	}

	public void setEscravo(jpvmTaskId taskId, String nomeEscravo) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		comando.append(String.format("%s.host=%s;%s.port=%d", nomeEscravo, taskId.getHost(), nomeEscravo, taskId.getPort()));
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.SET_SLAVE.tag());
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
	}

	public void requestDownload(String artefato) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(artefato);
		pvm.pvm_send(buffer, tid, RequestEscravo.DOWNLOAD.tag());

	}

	@SuppressWarnings("unchecked")
	public Collection<? extends Mensagem> downloadMensagens() throws jpvmException {
		requestDownload("mensagens");
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);

		jpvmBuffer buffer = msg.buffer;
		int bytesCount = buffer.upkint();
		byte[] bytes = new byte[bytesCount];
		buffer.unpack(bytes, bytesCount, 1);

		List<Mensagem> mensagens;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
			mensagens = (List<Mensagem>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Não foi possível recuperar as mensagens");
		}
		return mensagens;
	}

}
