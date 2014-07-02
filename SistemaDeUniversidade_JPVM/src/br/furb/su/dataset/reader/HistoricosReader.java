package br.furb.su.dataset.reader;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.SituacaoDisciplina;

public class HistoricosReader extends DataReader<Historico> {

	public static final String FILE_NAME = "historicos.csv";
	private List<Historico> historicos;

	public HistoricosReader(File pastaOrigem) {
		super(new File(pastaOrigem, FILE_NAME));
	}

	public HistoricosReader() {
		super();
	}

	@Override
	protected Historico lerRegistro() {
		Scanner sc = scanner;
		long codAluno = sc.nextLong();
		int codDisciplina = sc.nextInt();
		int codCurso = sc.nextInt();
		Calendar inicio = Sistema.converterData(sc.next());
		SituacaoDisciplina situacao = SituacaoDisciplina.valueOf(sc.next());

		return new Historico(codAluno, codDisciplina, codCurso, situacao, inicio);
	}

	@Override
	protected void insereRegistro(Historico registro) {
		historicos.add(registro);
	}

	@Override
	protected void inicializa(InDataset inDataset) {
		super.inicializa(inDataset);
		historicos = inDataset.getHistoricos();
	}

}
