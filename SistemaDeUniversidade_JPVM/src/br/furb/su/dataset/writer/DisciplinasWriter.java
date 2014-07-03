package br.furb.su.dataset.writer;

import java.io.IOException;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Disciplina;

public class DisciplinasWriter extends DataWriter<Disciplina> {

	@Override
	protected void gravarRegistro(Disciplina registro) throws IOException {
		pis.print(registro.getCod());
		sep();
		pis.print(registro.getNome());
		sep();
		pis.print(registro.getValorMatricula());
		sep();
		pis.print(registro.getValorMensal());
		nl();
	}

	@Override
	protected Iterator<Disciplina> iterador(OutDataset outDataset) {
		return null;
	}

}
