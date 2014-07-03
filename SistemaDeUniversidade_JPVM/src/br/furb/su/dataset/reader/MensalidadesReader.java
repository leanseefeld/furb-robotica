package br.furb.su.dataset.reader;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import br.furb.su.Sistema;
import br.furb.su.dataset.InDataset;
import br.furb.su.modelo.dados.Mensalidade;

public class MensalidadesReader extends DataReader<Mensalidade> {

	public static final String FILE_NAME = "mensalidades.csv";

	private List<Mensalidade> mensalidades;
	
	public MensalidadesReader() {
	}

	public MensalidadesReader(File pastaEntrada) {
		super(new File(pastaEntrada, FILE_NAME));
	}

	@Override
	protected void inicializa(InDataset inDataset) {
		mensalidades = inDataset.getMensalidades();
	}

	@Override
	protected void insereRegistro(Mensalidade registro) {
		mensalidades.add(registro);
	}

	@Override
	protected Mensalidade lerRegistro() {
		Scanner sc = scanner;
		int codAluno = sc.nextInt();
		double valor = Double.parseDouble(sc.next());
		String competencia = sc.next();
		String vencimento = sc.next();
		boolean isPaga = sc.nextBoolean();

		return new Mensalidade(codAluno, valor, Sistema.converterData(competencia), Sistema.converterData(vencimento), isPaga);
	}

}
