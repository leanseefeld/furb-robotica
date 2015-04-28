package br.furb.robotica.algoritmos;

/**
 * Interface implementada pelos algoritmos geradores de caminhos.
 */
public interface GeradorCaminho {

	/**
	 * Gera o caminho para um mapa anteriormente configurado.
	 * 
	 * @return caminho para chegar da posição do robô até o destino.
	 */
	Caminho gerarCaminho();

}
