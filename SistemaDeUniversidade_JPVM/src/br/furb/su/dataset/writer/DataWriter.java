package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Mensalidade;

public abstract class DataWriter<T> {

	private File arquivoSaida;
	protected PrintStream pis;
	protected Iterator<Mensalidade> iterator;

	public DataWriter(File arquivoSaida) {
		this.arquivoSaida = arquivoSaida;
	}

	public void gravarDados(OutDataset outDataset) throws IOException {
		try (PrintStream pis = new PrintStream(arquivoSaida)) {
			this.pis = pis;
			Iterator<T> it = iterador(outDataset);

			while (it.hasNext()) {
				gravarRegistro(it.next());
			}
		} finally {
			this.pis = null;
		}
	}

	protected abstract void gravarRegistro(T registro) throws IOException;

	protected abstract Iterator<T> iterador(OutDataset outDataset);

	protected void sep() throws IOException {
		pis.print(',');
	}

	protected void nl() throws IOException {
		pis.println();
	}

}
