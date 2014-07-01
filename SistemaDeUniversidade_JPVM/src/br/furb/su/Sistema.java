package br.furb.su;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.furb.su.dataset.InDataset;
import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.dados.Aluno;

public final class Sistema {

	public static boolean DEBUG = true;

	public static final double PERCENTUAL_MULTA = 1.15;
	public static final int NUM_ESCRAVOS = 4;
	private static final Sistema INSTANCE = new Sistema();
	private static boolean loaded = false;
	private static File pastaEntrada;
	private static File pastaSaida;
	private static InDataset inDataset;
	private static OutDataset outDataset;
	private static Calendar dataAtual;

	private static Estatisticas estatisticas = new Estatisticas();

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
}
