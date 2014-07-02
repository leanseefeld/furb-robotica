package br.furb.su.escravo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;

public abstract class EscravoBase {

	protected static final String MSG_REGISTRO_TRAVADO = "Registro %d está travado para %s";
	protected static final String MSG_REGISTRO_LIVRE = "Registro %d está livre.";
	protected static final String MSG_COD_NAO_RECONHECIDO = "Código de requisição desconhecido: %d";
	protected static final String MSG_COD_NAO_SUPORTADO = "Código de requisição conhecido, mas não suportado: %s (%d)";
	protected static final String MSG_SEM_RESPOSTA = "O escravo esqueceu de responder.";
	private static final Integer ANY_REGISTER = new Integer(Integer.MIN_VALUE);

	protected final jpvmEnvironment pvm;

	private boolean isAtivo, respondido;
	private jpvmTaskId destinatario;

	private final Map<Integer, jpvmTaskId> locks;

	public EscravoBase() throws jpvmException {
		pvm = new jpvmEnvironment(true);
		locks = new HashMap<>();
		isAtivo = true;
		do {
			respondido = false;
			jpvmMessage msg = pvm.pvm_recv();
			destinatario = msg.sourceTid;
			processaRequest(msg);
		} while (isAtivo);
	}

	protected void processaRequest(jpvmMessage msg) throws jpvmException {
		final int messageTag = msg.messageTag;
		final jpvmBuffer buffer = msg.buffer;
		if (messageTag < 0 || messageTag > RequestEscravo.TOTAL_REQUESTS) {
			responder(ResponseEscravo.FAILURE, String.format(MSG_COD_NAO_RECONHECIDO, messageTag));
		} else {
			try {
				RequestEscravo req = RequestEscravo.values()[messageTag];
				switch (req) {
				case KILL:
					doKill();
					break;
				case UPLOAD:
					doUpload(buffer);
					break;
				case DOWNLOAD:
					if (checkLocked(destinatario, ANY_REGISTER)) {
						doDownload(buffer);
					}
					break;
				case GET:
					doGet(buffer);
					break;
				case REMOVE:
					doRemove(buffer);
					break;
				case UPDATE:
					doUpdate(buffer);
					break;
				case LOCK:
					doLock(destinatario, buffer);
					break;
				case UNLOCK:
					doUnlock(destinatario, buffer);
					break;
				case OPERATION:
					doOperation(msg);
					break;
				default:
					responder(ResponseEscravo.FAILURE, String.format(MSG_COD_NAO_SUPORTADO, req.name(), req.tag()));
					break;
				}
			} catch (Throwable t) {
				try (StringWriter sw = new StringWriter(); //
						PrintWriter pw = new PrintWriter(sw)) {
					t.printStackTrace(pw);
					responder(ResponseEscravo.FAILURE, sw.toString()); // tenta stack completa
				} catch (IOException e) {
					e.printStackTrace();
					responder(ResponseEscravo.FAILURE, t.toString()); // garante apenas qual é a exceção
				}
			}
			garantirResposta();
		}
	}

	protected void doKill() throws jpvmException {
		isAtivo = false;
		responder(ResponseEscravo.OK, null);
	}

	protected abstract void doUpload(jpvmBuffer buffer);

	protected abstract void doDownload(jpvmBuffer buffer);

	protected abstract void doGet(jpvmBuffer buffer);

	protected abstract void doRemove(jpvmBuffer buffer);

	protected abstract void doUpdate(jpvmBuffer buffer);

	protected abstract void doLock(jpvmTaskId taskId, jpvmBuffer buffer);

	protected abstract void doUnlock(jpvmTaskId taskId, jpvmBuffer buffer);

	protected abstract void doOperation(jpvmMessage msg);

	protected void lock(jpvmTaskId taskId, Integer key) throws jpvmException {
		if (checkLocked(taskId, key)) {
			locks.put(key, taskId);
		}
	}

	protected boolean checkLocked(jpvmTaskId taskId, Integer key) throws jpvmException {
		if (isLocked(taskId, key)) {
			responder(ResponseEscravo.LOCKED, getLockMessage(key));
			return false; // não ok
		}
		return true; // ok
	}

	protected String getLockMessage(Integer key) {
		jpvmTaskId owner = locks.get(key);
		if (owner == null) {
			return String.format(MSG_REGISTRO_LIVRE, key.intValue());
		}
		return String.format(MSG_REGISTRO_TRAVADO, key.intValue(), owner.toString());
	}

	protected boolean isLocked(jpvmTaskId taskId, Integer key) {
		if (ANY_REGISTER.equals(key)) {
			return locks.containsValue(taskId);
		}
		jpvmTaskId owner = locks.get(key);
		if (owner == null) {
			return false;
		}
		return owner.equals(taskId);
	}

	protected final void responder(ResponseEscravo responseTag, String mensagem) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		if (mensagem != null) {
			buffer.pack(mensagem);
		}
		pvm.pvm_send(buffer, destinatario, responseTag.getTag());
		respondido = true;
	}

	private void garantirResposta() throws jpvmException {
		if (!respondido) {
			responder(ResponseEscravo.FAILURE, MSG_SEM_RESPOSTA);
		}
	}

}
