package br.furb.su.dataset.reader;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.Aluno;

public class AlunosReader extends DataReader<Aluno> {

	public static final String FILE_NAME = "alunos.csv";
	private Map<Long, Aluno> alunosMap;

	public AlunosReader() {
	}

	public AlunosReader(File pastaOrigem) {
		super(new File(pastaOrigem, FILE_NAME));
	}

	@Override
	protected Aluno lerRegistro() {
		Scanner sc = scanner;
		long cod = sc.nextLong();
		String nome = sc.next();
		boolean isAtivo = sc.nextBoolean();

		return new Aluno(cod, nome, isAtivo);
	}

	@Override
	protected void insereRegistro(Aluno aluno) {
		alunosMap.put(aluno.getCod(), aluno);

	}

	@Override
	protected void inicializa(InDataset inDataset) {
		super.inicializa(inDataset);
		alunosMap = inDataset.getAlunosMap();
	}

}
