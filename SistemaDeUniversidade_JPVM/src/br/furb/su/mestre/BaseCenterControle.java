package br.furb.su.mestre;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.escravo.LockException;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.escravo.ResponseEscravo;
import br.furb.su.escravo.SlaveException;
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
		if (msg.messageTag == ResponseEscravo.FAILURE.tag()) {
			throw new SlaveException(msg.buffer.upkstr());
		}
	}

	public jpvmTaskId getTaskId() {
		return tid;
	}

	public void async_enviaOperacao(Operacao op) throws jpvmException {
		byte[] bytes;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(op);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao serializar operação", e);
		}
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(bytes.length);
		buffer.pack(bytes, bytes.length, 1);
		pvm.pvm_send(buffer, tid, RequestEscravo.OPERATION.tag());
	}

	public void waitResposta() throws jpvmException {
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
	}
}
