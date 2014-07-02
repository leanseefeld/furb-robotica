package br.furb.su.escravo;

/**
 * Possíveis respostas aos comandos enviados a um escravo.
 * 
 * @author William Leander Seefeld
 * 
 */
public enum ResponseEscravo {

	/**
	 * Request realizado com sucesso. O retorno consta no buffer.
	 */
	OK,
	/**
	 * Um dos registros solicitados pelo request está bloqueado. O registro e
	 * dono do lock estão no buffer.
	 */
	LOCKED,
	/**
	 * Ocorreu um problema ao executar o request. Detalhes da falha estão no
	 * buffer.
	 */
	FAILURE;

	public int getTag() {
		return ordinal();
	}

}
