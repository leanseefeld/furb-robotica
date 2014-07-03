package br.furb.su.dataset.reader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import br.furb.su.Sistema;
import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.writer.DataWriter;
import br.furb.su.modelo.dados.Historico;

public class HistoricosWriter extends DataWriter<Historico> {

	public static final String FILE_NAME = "estatisticas.txt";

	public HistoricosWriter() {
	}

	public HistoricosWriter(File pastaSaida) {
		super(new File(pastaSaida, FILE_NAME));
	}

	@Override
	protected void gravarRegistro(Historico h) throws IOException {
		pis.print(h.getCodAluno());
		sep();
		pis.print(h.getCodDisciplina());
		sep();
		pis.print(h.getCodCurso());
		sep();
		pis.print(Sistema.formatarData(h.getInicio()));
		sep();
		pis.print(h.getSituacao().name());
		nl();
	}

	@Override
	protected Iterator<Historico> iterador(OutDataset outDataset) {
		return outDataset.getHistoricos().iterator();
	}

}
