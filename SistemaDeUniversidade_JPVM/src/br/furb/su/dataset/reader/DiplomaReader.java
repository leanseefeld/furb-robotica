package br.furb.su.dataset.reader;

import java.util.Scanner;

import br.furb.su.Sistema;
import br.furb.su.modelo.dados.Diploma;

public class DiplomaReader extends DataReader<Diploma> {

	@Override
	protected Diploma lerRegistro() {
		Scanner sc = scanner;
		int codAluno = sc.nextInt();
		int codCurso = sc.nextInt();
		String data = sc.next();
		return new Diploma(codAluno, codCurso, Sistema.converterData(data));
	}

	@Override
	protected void insereRegistro(Diploma registro) {
		// TODO Auto-generated method stub

	}

}
