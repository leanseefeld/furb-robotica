package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.modelo.dados.Mensalidade;

public class MensalidadesWriter extends DataWriter<Mensalidade> {

	public MensalidadesWriter(File pastaSaida) {
		super(new File(pastaSaida, MensalidadesReader.FILE_NAME));
	}

	@Override
	protected void gravar(Mensalidade mensalidade) throws IOException {
		pis.print(mensalidade.getCodAluno());
		sep();
		pis.print(mensalidade.getValor());
		sep();
		pis.print(dateToString(mensalidade.getCompetencia()));
		sep();
		pis.print(dateToString(mensalidade.getVencimento()));
		sep();
		pis.print(mensalidade.isPaga());
		nl();
	}

	@Override
	protected Iterator<Mensalidade> iterador(OutDataset outDataset) {
		return outDataset.getMensalidades().iterator();
	}

}
