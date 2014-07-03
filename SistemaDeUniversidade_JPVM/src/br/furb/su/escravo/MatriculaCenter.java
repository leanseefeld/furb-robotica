package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.Collection;

import br.furb.su.dataset.reader.SolicitacoesMatriculaReader;
import br.furb.su.modelo.dados.SolicitacaoMatricula;
import jpvm.jpvmException;

public class MatriculaCenter extends EscravoBase {
	
	private final Collection<SolicitacaoMatricula> sols = new ArrayList<SolicitacaoMatricula>();
	private final SolicitacoesMatriculaReader reader = new SolicitacoesMatriculaReader();

	public MatriculaCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) {
		try {
			new MatriculaCenter().run();
		} catch (jpvmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doUpload(String buffer) {
		sols.addAll(reader.ler(buffer));
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
	protected void doOperation(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetSlave(String buffer) {
		// TODO Auto-generated method stub

	}

}
