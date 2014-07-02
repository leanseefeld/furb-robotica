package br.furb.su.dataset.reader;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.SolicitacaoDiploma;

public class SolicitacoesDiplomaReader extends DataReader<SolicitacaoDiploma> {

	public static final String FILE_NAME = "solicitacoesDiploma.csv";
	private List<SolicitacaoDiploma> solicitacoesDiploma;

	public SolicitacoesDiplomaReader() {
	}
	
	public SolicitacoesDiplomaReader(File pastaOrigem) {
		super(new File(pastaOrigem, FILE_NAME));
	}

	@Override
	protected SolicitacaoDiploma lerRegistro() {
		Scanner sc = scanner;
		long codAluno = sc.nextLong();
		int codCurso = sc.nextInt();
		return new SolicitacaoDiploma(codAluno, codCurso);
	}

	@Override
	protected void insereRegistro(SolicitacaoDiploma registro) {
		solicitacoesDiploma.add(registro);
	}

	@Override
	protected void inicializa(InDataset inDataset) {
		super.inicializa(inDataset);
		solicitacoesDiploma = inDataset.getSolicitacoesDiploma();
	}

}
