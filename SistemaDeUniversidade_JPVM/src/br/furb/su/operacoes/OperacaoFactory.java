package br.furb.su.operacoes;

import java.util.Calendar;

public final class OperacaoFactory {

	public static final String NOME_PROCESS_DIPLOMAS = "processarDiplomas";
	public static final String NOME_PROCESS_NOVAS_MENSALIDADES = "processarNovasMensalidades";
	public static final String NOME_PROCESS_MENSALIDADES_ATRASADAS = "processarMensalidadesAtrasadas";

	public static Operacao processarMensalidadesAtrasadas(Calendar dataReferencia) {
		Operacao op = new Operacao(NOME_PROCESS_MENSALIDADES_ATRASADAS);
		op.setParam("dataReferencia", dataReferencia);
		return op;
	}

	public static Operacao processarNovasMensalidades() {
		Operacao op = new Operacao(NOME_PROCESS_NOVAS_MENSALIDADES);
		return op;
	}

	public static Operacao processarDiplomas() {
		Operacao op = new Operacao(NOME_PROCESS_DIPLOMAS);
		return op;
	}

}
