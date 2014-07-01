package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Diploma;

public class DiplomasWriter extends DataWriter<Diploma> {

	public DiplomasWriter(File pastaSaida) {
		super(new File(pastaSaida, "diplomas.csv"));
	}

	@Override
	protected void gravar(Diploma r) throws IOException {
		pis.print(r.getAluno().getCod());
		sep();
		pis.print(r.getCurso().getCod());
		sep();
		pis.print(dateToString(r.getData()));
	}

	@Override
	protected Iterator<Diploma> iterador(OutDataset outDataset) {
		return outDataset.getDiplomas().iterator();
	}

}
