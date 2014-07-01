package br.furb.su.dataset.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public void ler(InDataset inDataset) throws FileNotFoundException {
		try {
			if (!arquivoOrigem.exists()) {
				throw new IllegalArgumentException("arquivo de origem não existe: " + arquivoOrigem.getAbsolutePath());
			}
			Sistema.debug("iniciando leitura (" + getClass().getName() + ")");
			try (Scanner sc = new Scanner(arquivoOrigem)) {
				sc.useDelimiter(",|[\r\n]+");
				this.scanner = sc;
				inicializa(inDataset);

				T registro;
				while (sc.hasNextLine() && (registro = lerRegistro()) != null) {
//					Sistema.debug("leu um registro (" + getClass().getName() + ")");
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

	protected void inicializa(InDataset inDataset) {
		// para ser sobrescrito
	}

	protected abstract T lerRegistro();

	protected abstract void insereRegistro(T registro);

	protected static Calendar stringToDate(String str) {
		Matcher m = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})").matcher(str);
		if (!m.matches()) {
			throw new IllegalArgumentException("data não reconhecida: " + str);
		}
		int dia = Integer.parseInt(m.group(1));
		int mes = Integer.parseInt(m.group(2));
		int ano = Integer.parseInt(m.group(3));
		Calendar data = Calendar.getInstance();
		data.set(ano, mes - 1, dia, 0, 0, 0);
		return data;
	}
}
