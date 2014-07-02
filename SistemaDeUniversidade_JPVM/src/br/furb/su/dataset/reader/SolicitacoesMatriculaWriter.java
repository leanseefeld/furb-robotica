package br.furb.su.dataset.reader;

import java.io.IOException;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.writer.DataWriter;
import br.furb.su.modelo.dados.SolicitacaoMatricula;

public class SolicitacoesMatriculaWriter extends DataWriter<SolicitacaoMatricula> {

	@Override
	protected void gravarRegistro(SolicitacaoMatricula s) throws IOException {
		pis.print(s.getCodAluno());
		sep();
		pis.print(s.getCodDisciplina());
		sep();
		pis.print(s.getCodCurso());
		nl();
	}

	@Override
	protected Iterator<SolicitacaoMatricula> iterador(OutDataset outDataset) {
		return null;
	}

}
