package br.furb.su.dataset.writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.furb.su.dataset.OutDataset;
import br.furb.su.modelo.Mensagem;

public class MensagensWriter extends DataWriter<Mensagem> {

	public static final String FILE_NAME = "mensagens.csv";

	public MensagensWriter(File pastaSaida) {
		super(new File(pastaSaida, FILE_NAME));
	}

	@Override
	protected void gravarRegistro(Mensagem registro) throws IOException {
		pis.print(registro.getCodAluno());
		sep();
		pis.print(registro.getEtapa());
		sep();
		pis.print(registro.getMensagem());
		nl();
	}

	@Override
	protected Iterator<Mensagem> iterador(OutDataset outDataset) {
		List<Mensagem> mensagens = new ArrayList<>(outDataset.getAvisos());
		mensagens.addAll(outDataset.getProblemas());
		return mensagens.iterator();
	}

}
