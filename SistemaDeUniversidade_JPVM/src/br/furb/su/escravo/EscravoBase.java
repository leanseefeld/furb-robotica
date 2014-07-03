package br.furb.su.escravo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import br.furb.su.Sistema;
import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;

public abstract class EscravoBase {
	
	public static String last;

	static {
		Sistema.DEBUG = false;
	}

	protected static final String MSG_COD_NAO_RECONHECIDO = "Código de requisição desconhecido: %d";
	protected static final String MSG_COD_NAO_SUPORTADO = "Código de requisição conhecido, mas não suportado: %s (%d)";
	protected static final String MSG_REGISTRO_LIVRE = "Registro %d está livre.";
	protected static final String MSG_REGISTRO_TRAVADO = "Registro %d está travado para %s";
	protected static final String MSG_SEM_RESPOSTA = "O escravo esqueceu de responder.";
	protected static final String MSG_TIPO_NAO_RECONHECIDO = "Tipo não reconhecido: %s";
	private static final Integer ANY_REGISTER = new Integer(Integer.MIN_VALUE);

	protected final jpvmEnvironment pvm;

	private boolean isAtivo, respondido;
	private jpvmTaskId destinatario;

	private final Map<Integer, jpvmTaskId> locks;

	public EscravoBase() throws jpvmException {
		pvm = new jpvmEnvironment();
		locks = new HashMap<>();
	}

	public void run() throws jpvmException {
		try {
			isAtivo = true;
			destinatario = pvm.pvm_parent();
			responder(ResponseEscravo.OK, null);
			do {
				respondido = false;
				jpvmMessage msg = pvm.pvm_recv();
				destinatario = msg.sourceTid;
				processaMensagem(msg);
			} while (isAtivo);
		} catch (Throwable t) {
			try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
				t.printStackTrace(pw);
				JOptionPane.showMessageDialog(null, sw.getBuffer().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void processaMensagem(jpvmMessage msg) throws jpvmException {
		final int messageTag = msg.messageTag;
		final jpvmBuffer buffer = msg.buffer;
		if (messageTag < 0 || messageTag > RequestEscravo.TOTAL_REQUESTS) {
			responder(ResponseEscravo.FAILURE, String.format(MSG_COD_NAO_RECONHECIDO, messageTag));
		} else {
			try {
				String bufferStr = buffer.upkstr();
				last = bufferStr; // TODO: remover
//				JOptionPane.showMessageDialog(null, bufferStr);
				RequestEscravo req = RequestEscravo.values()[messageTag];
				switch (req) {
				case KILL:
					doKill();
					break;
				case UPLOAD:
					doUpload(bufferStr);
					break;
				case DOWNLOAD:
					if (checkLocked(destinatario, ANY_REGISTER)) {
						doDownload(bufferStr);
					}
					break;
				case GET:
					doGet(bufferStr);
					break;
				/*case REMOVE:
					doRemove(bufferStr);
					break;
				case UPDATE:
					doUpdate(bufferStr);
					break;
				case LOCK:
					doLock(destinatario, bufferStr);
					break;
				case UNLOCK:
					doUnlock(destinatario, bufferStr);
					break;*/
				case OPERATION:
					doOperation(bufferStr);
					break;
				case SET_SLAVE:
					doSetSlave(bufferStr);
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
				isAtivo = false;
			}
			garantirResposta();
		}
	}

	protected void doKill() throws jpvmException {
		isAtivo = false;
		responder(ResponseEscravo.OK, null);
	}

	protected abstract void doUpload(String buffer);

	protected abstract void doDownload(String buffer);

	protected abstract void doGet(String buffer);

	/*protected abstract void doRemove(String buffer);

	protected abstract void doUpdate(String buffer);

	protected abstract void doLock(jpvmTaskId taskId, String buffer);

	protected abstract void doUnlock(jpvmTaskId taskId, String buffer);*/

	protected abstract void doOperation(String buffer);

	protected abstract void doSetSlave(String buffer);

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
		pvm.pvm_send(buffer, destinatario, responseTag.tag());
		respondido = true;
	}
	
	protected final void tryResponder(ResponseEscravo responseTag, String mensagem) {
		try {
			responder(responseTag, mensagem);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	private void garantirResposta() throws jpvmException {
		if (!respondido) {
			responder(ResponseEscravo.FAILURE, MSG_SEM_RESPOSTA);
		}
	}

}
