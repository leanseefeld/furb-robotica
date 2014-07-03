package br.furb.su.operacoes;

import java.util.Calendar;

public final class OperacaoFactory {

	public static final String NOME_PROCESS_MATRICULAS = "processarMatriculas";
	public static final String NOME_PROCESS_DIPLOMAS = "processarDiplomas";
	public static final String NOME_PROCESS_NOVAS_MENSALIDADES = "processarNovasMensalidades";
	public static final String NOME_PROCESS_MENSALIDADES_ATRASADAS = "processarMensalidadesAtrasadas";

	public static Operacao processarMensalidadesAtrasadas(Calendar dataReferencia) {
		Operacao op = new Operacao(NOME_PROCESS_MENSALIDADES_ATRASADAS);
		op.setParam("dataReferencia", dataReferencia);
		return op;
	}

	public static Operacao processarNovasMensalidades(String alunosAtivos, Calendar dataReferencia) {
		Operacao op = new Operacao(NOME_PROCESS_NOVAS_MENSALIDADES);
		op.setParam("dataReferencia", dataReferencia);
		op.setParam("alunosAtivos", alunosAtivos);
		return op;
	}

	public static Operacao processarDiplomas(Calendar dataAtual) {
		Operacao op = new Operacao(NOME_PROCESS_DIPLOMAS);
		op.setParam("dataAtual", dataAtual);
		return op;
	}

	public static Operacao processarMatriculas(Calendar dataAtual) {
		Operacao op = new Operacao(NOME_PROCESS_MATRICULAS);
		op.setParam("dataAtual", dataAtual);
		return op;
	}

}
