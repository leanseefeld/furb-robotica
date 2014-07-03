package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import br.furb.su.Sistema;
import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Diploma;

public class DiplomasWriter extends DataWriter<Diploma> {

	public DiplomasWriter(File pastaSaida) {
		super(new File(pastaSaida, "diplomas.csv"));
	}

	public DiplomasWriter() {
	}

	@Override
	protected void gravarRegistro(Diploma r) throws IOException {
		pis.print(r.getCodAluno());
		sep();
		pis.print(r.getCodCurso());
		sep();
		pis.print(Sistema.formatarData(r.getData()));
		nl();
	}

	@Override
	protected Iterator<Diploma> iterador(OutDataset outDataset) {
		return outDataset.getDiplomas().iterator();
	}

}
