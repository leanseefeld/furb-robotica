package br.furb.su.dataset.reader;

import java.io.IOException;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.writer.DataWriter;
import br.furb.su.modelo.dados.Aluno;

public class AlunosWriter extends DataWriter<Aluno> {

	@Override
	protected void gravarRegistro(Aluno a) throws IOException {
		pis.print(a.getCod());
		sep();
		pis.print(a.getNome());
		sep();
		pis.print(a.isAtivo());
		nl();
	}

	@Override
	protected Iterator<Aluno> iterador(OutDataset outDataset) {
		return null;
	}

}
