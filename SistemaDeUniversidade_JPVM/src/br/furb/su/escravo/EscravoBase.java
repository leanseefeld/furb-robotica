package br.furb.su.escravo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.Sistema;
import br.furb.su.dataset.writer.DataWriter;
import br.furb.su.modelo.Mensagem;
import br.furb.su.operacoes.Operacao;

public abstract class EscravoBase {

	public static String last;

	protected static final String GET_MENSAGENS = "mensagens";
	protected static final String MSG_PARAM_NAO_ENCONTRADO = "Parâmetro '%s' não encontrado para o item %s.";
	protected static final String MSG_PARAM_NAO_RECONHECIDO = "Parâmetro '%s' não reconhecido para o item '%s'.";
	protected static final String MSG_ITEM_NAO_RECONHECIDO = "Item não reconhecido: %s";
	protected static final String MSG_OP_NAO_RECONHECIDA = "Operação desconhecida: %s";
	protected static final String MSG_COD_NAO_RECONHECIDO = "Código de requisição desconhecido: %d";
	protected static final String MSG_COD_NAO_SUPORTADO = "Código de requisição conhecido, mas não suportado: %s (%d)";
	protected static final String MSG_REGISTRO_LIVRE = "Registro %d está livre.";
	protected static final String MSG_REGISTRO_TRAVADO = "Registro %d está travado para %s";
	protected static final String MSG_SEM_RESPOSTA = "O escravo esqueceu de responder.";
	protected static final String MSG_TIPO_NAO_RECONHECIDO = "Tipo não reconhecido: %s";
	private static final Integer ANY_REGISTER = new Integer(Integer.MIN_VALUE);

	protected final jpvmEnvironment pvm;
	protected final ArrayList<Mensagem> mensagens = new ArrayList<>();

	private boolean isAtivo, respondido;
	private jpvmTaskId destinatario;

	private final Map<Integer, jpvmTaskId> locks;

	public EscravoBase() throws jpvmException {
		Sistema.DEBUG = false;
		pvm = new jpvmEnvironment();
		locks = new HashMap<>();
	}

	public void run() {
		try {
			isAtivo = true;
			destinatario = pvm.pvm_parent();
			do {
				respondido = false;
				jpvmMessage msg = pvm.pvm_recv();
				destinatario = msg.sourceTid;
				processaMensagem(msg);
			} while (isAtivo);
		} catch (Throwable t) {
			try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
				t.printStackTrace(pw);
				if (Sistema.JOPTIONPANE) {
					JOptionPane.showMessageDialog(null, sw.getBuffer().toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (Sistema.JOPTIONPANE) {
			JOptionPane.showMessageDialog(null, "Saindo normalmente: " + getClass());
		}
	}

	protected void processaMensagem(jpvmMessage msg) throws jpvmException {
		final int messageTag = msg.messageTag;
		final jpvmBuffer buffer = msg.buffer;
		if (messageTag < 0 || messageTag > RequestEscravo.TOTAL_REQUESTS) {
			responder(ResponseEscravo.FAILURE, String.format(MSG_COD_NAO_RECONHECIDO, messageTag));
		} else {
			try {
				RequestEscravo req = RequestEscravo.values()[messageTag];
				String bufferStr;
				switch (req) {
				case KILL:
					doKill();
					break;
				case UPLOAD:
					bufferStr = buffer.upkstr();
					last = bufferStr; // TODO: remover
					doUpload(bufferStr);
					break;
				case DOWNLOAD:
					bufferStr = buffer.upkstr();
					last = bufferStr; // TODO: remover
					if (checkLocked(destinatario, ANY_REGISTER)) {
						doDownload(bufferStr);
					}
					break;
				case GET:
					bufferStr = buffer.upkstr();
					last = bufferStr; // TODO: remover
					doInternalGet(bufferStr);
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
					int bytesCount = buffer.upkint();
					byte[] bytes = new byte[bytesCount];
					buffer.unpack(bytes, bytesCount, 1);

					Operacao op;
					try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
						op = (Operacao) ois.readObject();
					}
					doOperation(op);
					break;
				case SET_SLAVE:
					bufferStr = buffer.upkstr();
					last = bufferStr; // TODO: remover

					String[] setCmd = bufferStr.split(";");
					final String hostPart = setCmd[0];
					final String portPart = setCmd[1];
					String slaveName = hostPart.substring(0, hostPart.indexOf('.'));
					String host = hostPart.substring(hostPart.indexOf('=') + 1);
					int port = Integer.parseInt(portPart.substring(portPart.indexOf('=') + 1));
					jpvmTaskId taskId = new jpvmTaskId(host, port);

					doSetSlave(slaveName, taskId);
					break;
				default:
					responder(ResponseEscravo.FAILURE, String.format(MSG_COD_NAO_SUPORTADO, req.name(), req.tag()));
					break;
				}
			} catch (Throwable t) {
				try (StringWriter sw = new StringWriter(); //
						PrintWriter pw = new PrintWriter(sw)) {
					t.printStackTrace(pw);
					JOptionPane.showMessageDialog(null, "Erro em " + getClass() + ":\n" + sw.toString());
					responder(ResponseEscravo.ERROR, sw.toString()); // tenta stack completa
				} catch (IOException e) {
					e.printStackTrace();
					responder(ResponseEscravo.ERROR, t.toString()); // garante apenas qual é a exceção
				}
				isAtivo = false;
			}
			garantirResposta();
		}
	}

	protected void doKill() throws jpvmException {
		isAtivo = false;
		responder(ResponseEscravo.OK);
	}

	protected abstract void doUpload(String buffer);

	protected void doDownload(String buffer) {
		if (buffer.equals("mensagens")) {
			byte[] bytes = serializar(mensagens);
			jpvmBuffer jBuffer = new jpvmBuffer();
			jBuffer.pack(bytes.length);
			jBuffer.pack(bytes, bytes.length, 1);
			tryResponder(ResponseEscravo.OK, jBuffer);
		}
	}

	protected void doInternalGet(String item) {
		if (item.equalsIgnoreCase(GET_MENSAGENS)) {
			byte[] bytes = serializar(mensagens);
			jpvmBuffer buffer = new jpvmBuffer();
			buffer.pack(bytes.length);
			buffer.pack(bytes, bytes.length, 1);
			tryResponder(ResponseEscravo.OK, buffer);
			mensagens.clear();
		} else {
			doGet(item);
		}
	}

	protected void doGet(String item) {
		tryResponder(ResponseEscravo.FAILURE, String.format(MSG_ITEM_NAO_RECONHECIDO, item));
	}

	/*protected abstract void doRemove(String buffer);

	protected abstract void doUpdate(String buffer);

	protected abstract void doLock(jpvmTaskId taskId, String buffer);

	protected abstract void doUnlock(jpvmTaskId taskId, String buffer);*/

	protected void doOperation(Operacao op) {
	}

	protected void doSetSlave(String slaveName, jpvmTaskId taskId) {
		tryResponder(ResponseEscravo.OK);
	}

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

	protected final void responder(ResponseEscravo responseTag, jpvmBuffer buffer) throws jpvmException {
		pvm.pvm_send(buffer, destinatario, responseTag.tag());
		respondido = true;
	}

	protected final void responder(ResponseEscravo responseTag, String mensagem) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		if (mensagem != null) {
			buffer.pack(mensagem);
		}
		responder(responseTag, buffer);
	}

	protected final void responder(ResponseEscravo responseTag) throws jpvmException {
		jpvmBuffer emptyBuffer = new jpvmBuffer();
		responder(responseTag, emptyBuffer);
	}

	protected final void tryResponder(ResponseEscravo responseTag, jpvmBuffer buffer) {
		try {
			responder(responseTag, buffer);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	protected final void tryResponder(ResponseEscravo responseTag, String mensagem) {
		try {
			responder(responseTag, mensagem);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	protected final void tryResponder(ResponseEscravo responseTag) {
		try {
			responder(responseTag);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	private void garantirResposta() throws jpvmException {
		if (!respondido) {
			responder(ResponseEscravo.FAILURE, MSG_SEM_RESPOSTA);
		}
	}

	public static byte[] serializar(Serializable s) {
		byte[] bytes;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(s);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao serializar operação", e);
		}
		return bytes;
	}

	protected static Operacao converterGetParaOperacao(String getCmd) {
		int idx = getCmd.indexOf('\n');
		String nome = getCmd.substring(0, idx).toLowerCase();
		Operacao getOp = new Operacao(nome);

		String paramsStr = getCmd.substring(idx + 1);
		String[] params = paramsStr.split(";");
		for (int i = 0; i < params.length; i++) {
			String[] values = params[i].split("=");
			getOp.setParam(values[0], values[1]);
		}
		return getOp;
	}

	protected static <T> String writeToString(DataWriter<T> writer, Collection<T> dados) {
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			writer.gravarDados(dados, pw);
			return sw.getBuffer().toString();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gravar dados", e);
		}
	}

}
