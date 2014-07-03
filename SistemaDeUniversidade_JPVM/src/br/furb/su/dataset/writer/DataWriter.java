package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Mensalidade;

public abstract class DataWriter<T> {

	private File arquivoSaida;
	protected PrintWriter pis;
	protected Iterator<Mensalidade> iterator;

	public DataWriter(File arquivoSaida) {
		this.arquivoSaida = arquivoSaida;
	}

	public DataWriter() {
	}

	public void gravarArquivo(OutDataset outDataset) throws IOException {
		try (PrintWriter pis = new PrintWriter(arquivoSaida)) {
			this.pis = pis;
			Iterator<T> it = iterador(outDataset);

			while (it.hasNext()) {
				gravarRegistro(it.next());
			}
		} finally {
			this.pis = null;
		}
	}

	public void gravarArquivo(Collection<T> dados) throws IOException {
		try (PrintWriter pw = new PrintWriter(arquivoSaida)) {
			gravarDados(dados, pw);
		}
	}

	public void gravarDados(Collection<T> dados, PrintWriter out) throws IOException {
		try {
			this.pis = out;
			for (T dado : dados) {
				gravarRegistro(dado);
			}
		} finally {
			this.pis = null;
		}
	}

	protected abstract void gravarRegistro(T registro) throws IOException;

	protected abstract Iterator<T> iterador(OutDataset outDataset);

	protected void sep() {
		pis.print(',');
	}

	protected void nl() {
		pis.println();
	}

}
