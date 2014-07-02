package br.furb.su.dataset.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.Curso;

public class CursoReader extends DataReader<Curso> {

	public final static String FILE_NAME = "cursos.csv";
	private Map<Integer, Curso> cursos;

	public CursoReader(File pastaOrigem) {
		super(new File(pastaOrigem, FILE_NAME));
	}
	
	public CursoReader() {
		super();
	}

	@Override
	protected Curso lerRegistro() {
		Scanner sc = scanner;
		int cod = sc.nextInt();
		String nome = sc.next();
		String[] disciplinas = sc.next().split(";");
		List<Integer> codsDisciplinas = new ArrayList<>(disciplinas.length);
		for (String disciplina : disciplinas) {
			codsDisciplinas.add(Integer.parseInt(disciplina));
		}
		return new Curso(cod, nome, codsDisciplinas);
	}

	@Override
	protected void insereRegistro(Curso curso) {
		cursos.put(curso.getCod(), curso);
	}

	@Override
	protected void inicializa(InDataset inDataset) {
		super.inicializa(inDataset);
		cursos = inDataset.getCursosMap();
	}
	

}
