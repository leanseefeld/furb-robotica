package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import br.furb.su.Sistema;
import br.furb.su.dataset.OutDataset;

public class EstatisticasWriter extends DataWriter<String> {

	public static final String FILE_NAME = "estatisticas.txt";

	public EstatisticasWriter(File pastaSaida) {
		super(new File(pastaSaida, FILE_NAME));
	}

	@Override
	protected void gravarRegistro(String registro) throws IOException {
		pis.println(registro);
	}

	@Override
	protected Iterator<String> iterador(OutDataset outDataset) {
		return Sistema.estatisticas().financeiras().toList().iterator();
	}

}
