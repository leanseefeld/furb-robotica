package br.furb.su;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.furb.su.dataset.InDataset;
import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.reader.AlunosReader;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DataReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.reader.SolicitacoesMatriculaReader;
import br.furb.su.escravo.CursoCenter;
import br.furb.su.escravo.DiplomaCenter;
import br.furb.su.escravo.EscravoBase;
import br.furb.su.escravo.MatriculaCenter;
import br.furb.su.escravo.MensalidadeCenter;
import br.furb.su.modelo.dados.Aluno;

/**
 * Possui funções utilitárias, o conjunto de dados trabalhado pela aplicação, no
 * lado do mestre.
 * 
 * @author wseefeld
 * 
 */
public final class Sistema {

	public static boolean DEBUG = true;

	public static final double PERCENTUAL_MULTA = 1.15;
	private static final Sistema INSTANCE = new Sistema();
	private static boolean loaded = false;
	private static File pastaEntrada;
	private static File pastaSaida;
	private static InDataset inDataset;
	private static OutDataset outDataset;
	private static Calendar dataAtual;
	private static Estatisticas estatisticas = new Estatisticas();
	public static final int NUM_ESCRAVOS;

	private static final Map<Class<? extends EscravoBase>, Integer> CONFIG_ESCRAVOS = new HashMap<>();

	static {
		CONFIG_ESCRAVOS.put(CursoCenter.class, 1);
		CONFIG_ESCRAVOS.put(DiplomaCenter.class, 1);
		CONFIG_ESCRAVOS.put(MatriculaCenter.class, 1);
		CONFIG_ESCRAVOS.put(MensalidadeCenter.class, 1);
		NUM_ESCRAVOS = 4;
	}

	private Sistema() {
		// no!
	}

	public static void load(File pastaEntrada, File pastaSaida, Calendar dataAtual) {
		if (loaded) {
			throw new IllegalStateException("Sistema já carregado");
		}
		if (!pastaEntrada.isDirectory()) {
			throw new IllegalArgumentException("pastaEntrada deve ser um diretório existente");
		}
		if (pastaSaida.exists()) {
			if (!pastaSaida.isDirectory()) {
				throw new IllegalArgumentException("pastaSaida deve ser um diretório");
			}
		} else {
			pastaSaida.mkdirs();
			if (!pastaSaida.isDirectory()) {
				throw new IllegalArgumentException("não foi possível criar a pastaSaida");
			}
		}

		Sistema.pastaEntrada = pastaEntrada;
		Sistema.pastaSaida = pastaSaida;

		Sistema.dataAtual = dataAtual;
		inDataset = new InDataset();
		outDataset = new OutDataset();
		loaded = true;
	}

	public static Sistema getInstance() {
		return INSTANCE;
	}

	public static File getPastaEntrada() {
		return pastaEntrada;
	}

	public static File getPastaSaida() {
		return pastaSaida;
	}

	public static InDataset inDataset() {
		return inDataset;
	}

	public static OutDataset outDataset() {
		return outDataset;
	}

	public static Calendar getDataAtual() {
		Calendar clone = new GregorianCalendar();
		clone.setTimeInMillis(dataAtual.getTimeInMillis());
		return clone;
	}

	public static Estatisticas estatisticas() {
		return estatisticas;
	}

	public static Calendar proxVecto(Calendar data) {
		Calendar prox = Calendar.getInstance();
		prox.setTimeInMillis(data.getTimeInMillis());
		int nextMonth = data.get(Calendar.MONTH) + 1;
		if (nextMonth > Calendar.DECEMBER) {
			nextMonth = Calendar.JANUARY;
		}
		prox.set(Calendar.MONTH, nextMonth);
		return prox;
	}

	public static List<Aluno> filtraAlunosAtivos(InDataset inDataset) {
		List<Aluno> ativos = new ArrayList<>();
		for (Aluno aluno : inDataset.getAlunos()) {
			if (aluno.isAtivo()) {
				ativos.add(aluno);
			}
		}
		return ativos;
	}

	public static void debug(String msg) {
		if (DEBUG) {
			Exception ex = new Exception();
			StackTraceElement[] stack = ex.getStackTrace();
			System.out.println("[" + Thread.currentThread().getName() + "] " + stack[1].toString() + ": " + msg);
		}

	}

	public static Map<Class<? extends EscravoBase>, Integer> getConfigEscravos() {
		return Collections.unmodifiableMap(CONFIG_ESCRAVOS);
	}

	public static void inicializar() {
		debug("requisitando parâmetros");
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Informe a pasta de origem dos dados: ");
			File pastaOrigem = new File(scanner.nextLine());

			System.out.print("Informe a pasta de destino dos dados: ");
			File pastaDestino = new File(scanner.nextLine());

			System.out.print("Informe a data atual no format 'MM/AAAA': ");
			Calendar data = Sistema.converterData(scanner.nextLine());

			load(pastaOrigem, pastaDestino, data);
		}
		try {
			Sistema.lerDados(inDataset());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("não foi possível carregar os dados");
		}
	}

	public static Calendar converterData(String string) {
		Matcher m = Pattern.compile("(\\d{2})/(\\d{4})").matcher(string);
		if (!m.matches()) {
			throw new IllegalArgumentException("data não reconhecida: " + string);
		}
		int mes = Integer.parseInt(m.group(1));
		int ano = Integer.parseInt(m.group(2));
		Calendar data = Calendar.getInstance();
		data.set(ano, mes - 1, 1, 0, 0, 0);
		return data;
	}

	/**
	 * Converte um objeto de {@code Calendar} em uma string contendo:
	 * {@code dia/mes/ano}
	 * 
	 * @param date
	 *            data a ser convertida.
	 * @return representação da data informada em string.
	 */
	public static String formatarData(Calendar date) {
		int dia = date.get(Calendar.DAY_OF_MONTH);
		int mes = date.get(Calendar.MONTH) + 1;
		int ano = date.get(Calendar.YEAR);
		return String.valueOf(dia).concat("/").concat(String.valueOf(mes)).concat("/").concat(String.valueOf(ano));
	}

	public static void lerDados(InDataset inDataset) throws FileNotFoundException {
		debug("lendo dados");
		File pastaEntrada = getPastaEntrada();
		DataReader<?>[] leitores = new DataReader[] { new AlunosReader(pastaEntrada), //
				new MensalidadesReader(pastaEntrada), //
				new SolicitacoesMatriculaReader(pastaEntrada), //
				new HistoricosReader(pastaEntrada), //
				new DisciplinasReader(pastaEntrada), //
				new CursoReader(pastaEntrada), //
				new SolicitacoesDiplomaReader(pastaEntrada) //
		};
		for (int i = 0; i < leitores.length; i++) {
			leitores[i].ler(inDataset);
		}
		debug("leu dados");
	}
}
