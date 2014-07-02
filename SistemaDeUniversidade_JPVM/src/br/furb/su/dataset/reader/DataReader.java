package br.furb.su.dataset.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;

/**
 * @author William Leander Seefeld
 * 
 * @param <T>
 *            Tipo lido
 */
public abstract class DataReader<T> {

	private final File arquivoOrigem;
	protected Scanner scanner;

	public DataReader(File arquivoOrigem) {
		this.arquivoOrigem = arquivoOrigem;
	}

	public DataReader() {
		arquivoOrigem = null;
	}

	public void lerArquivo(InDataset inDataset) throws FileNotFoundException {
		try {
			if (arquivoOrigem == null) {
				throw new IllegalArgumentException("um arquivo deve ser informado para leitura em batch");
			}
			if (!arquivoOrigem.exists()) {
				throw new IllegalArgumentException("arquivo de origem não existe: " + arquivoOrigem.getAbsolutePath());
			}
			Sistema.debug("iniciando leitura (" + getClass().getName() + ")");
			try (Scanner sc = new Scanner(arquivoOrigem)) {
				configScanner(sc);
				inicializa(inDataset);

				T registro;
				while (sc.hasNextLine() && (registro = lerRegistro()) != null) {
					insereRegistro(registro);
				}
			} finally {
				this.scanner = null;
			}
			Sistema.debug("leitura finalizada (" + getClass().getName() + ")");
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public List<T> ler(String entrada) {
		Sistema.debug("iniciando leitura (" + getClass().getName() + ")");
		List<T> registros = new ArrayList<>();

		try (Scanner sc = new Scanner(entrada)) {
			configScanner(sc);

			T registro;
			while (sc.hasNextLine() && (registro = lerRegistro()) != null) {
				registros.add(registro);
			}
		}
		Sistema.debug("leitura finalizada (" + getClass().getName() + ")");
		return registros;
	}

	protected void inicializa(InDataset inDataset) {
		// para ser sobrescrito
	}

	private void configScanner(Scanner sc) {
		sc.useDelimiter(",|[\r\n]+");
		this.scanner = sc;
	}

	protected abstract T lerRegistro();

	protected abstract void insereRegistro(T registro);

}
