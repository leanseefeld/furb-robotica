package br.furb.su.dataset.reader;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.SolicitacaoMatricula;

public class SolicitacoesMatriculaReader extends DataReader<SolicitacaoMatricula> {

	public static final String FILE_NAME = "solicitacoesMatriculas.csv";
	private List<SolicitacaoMatricula> solicitacoesMatricula;

	public SolicitacoesMatriculaReader(File pastaOrigem) {
		super(new File(pastaOrigem, FILE_NAME));
	}

	public SolicitacoesMatriculaReader() {
	}

	@Override
	protected SolicitacaoMatricula lerRegistro() {
		Scanner sc = scanner;
		long codAluno = sc.nextLong();
		int codDisciplina = sc.nextInt();
		int codCurso = sc.nextInt();
		return new SolicitacaoMatricula(codAluno, codDisciplina, codCurso);
	}

	@Override
	protected void insereRegistro(SolicitacaoMatricula registro) {
		solicitacoesMatricula.add(registro);
	}

	@Override
	protected void inicializa(InDataset inDataset) {
		super.inicializa(inDataset);
		solicitacoesMatricula = inDataset.getSolicitacoesMatricula();
	}

}
