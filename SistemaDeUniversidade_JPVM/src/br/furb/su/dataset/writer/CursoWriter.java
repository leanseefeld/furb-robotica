package br.furb.su.dataset.writer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Disciplina;

public class CursoWriter extends DataWriter<Curso> {

	@Override
	protected void gravarRegistro(Curso curso) throws IOException {
		pis.print(curso.getCod());
		sep();
		pis.print(curso.getNome());
		sep();
		pis.print(codsDisciplinas(curso.getDisciplinas()));
		nl();
	}

	@Override
	protected Iterator<Curso> iterador(OutDataset outDataset) {
		return null;
	}

	private static String codsDisciplinas(List<Disciplina> disciplinas) {
		StringBuilder sb = new StringBuilder();
		final int qt = disciplinas.size();
		for (int i = 0; i < qt; i++) {
			sb.append(disciplinas.get(i).getCod());
			if (i < qt - 1) {
				sb.append(';');
			}
		}
		return sb.toString();
	}
}
