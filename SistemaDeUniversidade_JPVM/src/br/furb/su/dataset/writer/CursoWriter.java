package br.furb.su.dataset.writer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Curso;

public class CursoWriter extends DataWriter<Curso> {

	@Override
	protected void gravarRegistro(Curso curso) throws IOException {
		pis.print(curso.getCod());
		sep();
		pis.print(curso.getNome());
		sep();
		pis.print(codsDisciplinas(curso.getCodDisciplinas()));
		nl();
	}

	@Override
	protected Iterator<Curso> iterador(OutDataset outDataset) {
		return null;
	}

	private static String codsDisciplinas(Collection<Integer> codigos) {
		StringBuilder sb = new StringBuilder();
		Iterator<Integer> it = codigos.iterator();
		boolean hasNext = it.hasNext();
		while (hasNext) {
			sb.append(it.next());
			hasNext = it.hasNext();
			if (hasNext) {
				sb.append(';');
			}
		}
		return sb.toString();
	}
}
