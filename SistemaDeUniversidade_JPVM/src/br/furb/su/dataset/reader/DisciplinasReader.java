package br.furb.su.dataset.reader;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.Disciplina;

public class DisciplinasReader extends DataReader<Disciplina> {

	public final static String FILE_NAME = "disciplinas.csv";
	private Map<Integer, Disciplina> disciplinasMap;

	public DisciplinasReader(File pastaOrigem) {
		super(new File(pastaOrigem, FILE_NAME));
	}

	public DisciplinasReader() {
		super();
	}

	@Override
	protected Disciplina lerRegistro() {
		Scanner sc = scanner;
		int cod = sc.nextInt();
		String nome = sc.next();
		double valorMatricula = Double.parseDouble(sc.next());
		double valorMensalidade = Double.parseDouble(sc.next());

		return new Disciplina(cod, nome, valorMatricula, valorMensalidade);
	}

	@Override
	protected void insereRegistro(Disciplina disciplina) {
		disciplinasMap.put(disciplina.getCod(), disciplina);
	}

	@Override
	protected void inicializa(InDataset inDataset) {
		super.inicializa(inDataset);
		disciplinasMap = inDataset.getDisciplinasMap();
	}

}
