package br.furb.su.escravo;

/**
 * Comandos que todo escravo deve saber tratar.
 * 
 * @author William Leander Seefeld
 * 
 */
public enum RequestEscravo {

	/**
	 * Finaliza o escravo, descartando todos os dados apropriadamente.
	 */
	KILL,
	/**
	 * Carrega o conteúdo do buffer para o escravo.
	 */
	UPLOAD,
	/**
	 * Solicita todos os dados do escravo, que devem ser gravados no buffer.
	 */
	DOWNLOAD,
	/**
	 * Solicita os registros que satisfaçam o filtro presente no buffer.
	 */
	GET,
	/**
	 * Remove os registros que satisfaçam o filtro presente no buffer.
	 */
	REMOVE,
	/**
	 * Recupera o registro que possui o(s) código(s) informado(s) no buffer e
	 * sobrescreve os demais campos.
	 */
	UPDATE,
	/**
	 * Impede que o registro indicado no buffer seja alterado ou recuperado pelo
	 * tempo indicado.
	 */
	LOCK,
	/**
	 * Libera o registro indicado no buffer.
	 */
	UNLOCK,
	/**
	 * Executa a operação serializada no buffer, com os respectivos parâmetros.
	 */
	OPERATION,
	/**
	 * Identifica outro escravo.
	 */
	SET_SLAVE;

	public static final int TOTAL_REQUESTS = values().length;

	public int tag() {
		return ordinal();
	}

}
