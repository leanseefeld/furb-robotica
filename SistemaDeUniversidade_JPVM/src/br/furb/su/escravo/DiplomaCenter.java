package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.List;

import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.modelo.dados.SolicitacaoDiploma;
import br.furb.su.operacoes.Operacao;

public class DiplomaCenter extends EscravoBase {

	private SolicitacoesDiplomaReader reader = new SolicitacoesDiplomaReader();
	private List<SolicitacaoDiploma> sols = new ArrayList<>();

	private jpvmTaskId mensalidadeCenter;
	private jpvmTaskId cursoCenter;

	public DiplomaCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) {
		try {
			new DiplomaCenter().run();
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doSetSlave(String buffer) {
		String[] setCmd = buffer.split(";");
		final String hostPart = setCmd[0];
		final String portPart = setCmd[1];
		int idx = hostPart.indexOf("=");
		String slaveName = hostPart.substring(0, idx);
		String host = hostPart.substring(idx + 1);
		int port = Integer.parseInt(portPart.substring(portPart.indexOf("=") + 1));
		jpvmTaskId taskId = new jpvmTaskId(host, port);
		if (slaveName.equals("mensalidadeCenter")) {
			mensalidadeCenter = taskId;
		} else if (slaveName.equals("cursoCenter")) {
			cursoCenter = taskId;
		}
		try {
			responder(ResponseEscravo.OK, null);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doUpload(String buffer) {
		sols.addAll(reader.ler(buffer));
		try {
			responder(ResponseEscravo.OK, null);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doDownload(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doGet(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doOperation(Operacao op) {
		// TODO Auto-generated method stub

	}

}
