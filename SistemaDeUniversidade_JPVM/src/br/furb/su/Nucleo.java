package br.furb.su;

import java.io.File;
import java.io.IOException;

import br.furb.su.dataset.InDataset;
import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.writer.DataWriter;
import br.furb.su.dataset.writer.DiplomasWriter;
import br.furb.su.dataset.writer.EstatisticasWriter;
import br.furb.su.dataset.writer.MensagensWriter;
import br.furb.su.dataset.writer.MensalidadesWriter;

/**
 * Ofere as funções de manipulação sobre os dados.
 * 
 * @deprecated as funções de processamento serão gradativamente transferidas
 *             para os escravos correspondentes.
 * 
 * @author wseefeld
 * 
 */
@Deprecated
public class Nucleo {

	public static void main(String[] args) throws IOException {
		System.err.println("Ponto de entrada desatualizado: Nucleo.main()\nExecute via JPVM.\n");
		Sistema.inicializar();
		InDataset inDataset = Sistema.inDataset();
		OutDataset outDataset = Sistema.outDataset();

		ligarDados(inDataset);
		processar(inDataset, outDataset);
		gravarDados(outDataset);
	}

	private static void gravarDados(OutDataset outDataset) throws IOException {
		Sistema.debug("iniciando gravacao de dados");
		File pastaSaida = Sistema.getPastaSaida();
		DataWriter<?>[] gravadores = new DataWriter[] { // 
		/* ****/new MensagensWriter(pastaSaida), //
				new MensalidadesWriter(pastaSaida), //
				new DiplomasWriter(pastaSaida), //
				new EstatisticasWriter(pastaSaida), //
		// ...
		};

		for (int i = 0; i < gravadores.length; i++) {
			gravadores[i].gravarArquivo(outDataset);
		}
		Sistema.debug("dados gravados");
	}

}
