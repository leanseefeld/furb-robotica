package br.furb.su.dataset.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.escravo.EscravoBase;

/**
 * @author William Leander Seefeld
 * 
 * @param <T>
 *            Tipo lido
 */
public abstract class DataReader<T> {

	private final File arquivoOrigem;
	protected Scanner scanner;
	private static final Pattern NON_BLANK_LINE = Pattern.compile("\\S+");

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
				while (sc.hasNext(NON_BLANK_LINE) && (registro = lerRegistro()) != null) {
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
			try {
				while (sc.hasNext(NON_BLANK_LINE) && (registro = lerRegistro()) != null) {
					registros.add(registro);
				}
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(null, EscravoBase.last);
				throw e;
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
