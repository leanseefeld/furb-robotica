package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import jpvm.jpvmException;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.modelo.dados.Mensalidade;
import br.furb.su.operacoes.Operacao;

public class MensalidadeCenter extends EscravoBase {

	private final Collection<Mensalidade> mensalidades = new ArrayList<>();
	private final MensalidadesReader reader = new MensalidadesReader();

	public MensalidadeCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) {
		try {
			new MensalidadeCenter().run();
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doUpload(String buffer) {
		mensalidades.addAll(reader.ler(buffer));
		tryResponder(ResponseEscravo.OK, null);
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
		JOptionPane.showMessageDialog(null, op, "CursoCenter", JOptionPane.INFORMATION_MESSAGE);

	}

}
