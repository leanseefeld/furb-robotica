package br.furb.su.dataset.writer;

import java.io.IOException;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.SolicitacaoDiploma;

public class SolicitacaoDiplomaWriter extends DataWriter<SolicitacaoDiploma> {

	@Override
	protected void gravarRegistro(SolicitacaoDiploma sol) throws IOException {
		pis.print(sol.getCodAluno());
		sep();
		pis.print(sol.getCodCurso());
		nl();
	}

	@Override
	protected Iterator<SolicitacaoDiploma> iterador(OutDataset outDataset) {
		return null;
	}


}
