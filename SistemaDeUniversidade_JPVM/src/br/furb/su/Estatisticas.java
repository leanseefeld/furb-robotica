package br.furb.su;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Estatisticas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public class Financeiras implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private double totalMulta;
		private double totalMensalidades;
		private double totalProxMensalidades;
		private double totalMatriculas;

		public double getTotalMulta() {
			return totalMulta;
		}

		public void setTotalMulta(double totalMulta) {
			this.totalMulta = totalMulta;
		}

		public double getTotalMensalidades() {
			return totalMensalidades;
		}

		public void setTotalMensalidades(double totalMensalidades) {
			this.totalMensalidades = totalMensalidades;
		}

		public double getTotalProxMensalidades() {
			return totalProxMensalidades;
		}

		public void setTotalProxMensalidades(double totalProxMensalidades) {
			this.totalProxMensalidades = totalProxMensalidades;
		}

		public double getTotalMatriculas() {
			return totalMatriculas;
		}

		public void setTotalMatriculas(double totalMatriculas) {
			this.totalMatriculas = totalMatriculas;
		}

		public List<String> toList() {
			List<String> lista = new ArrayList<>(4);

			lista.add("Total de multas: R$ " + totalMulta);
			lista.add("Total de mensalidades: R$ " + totalMensalidades);
			lista.add("Total de próximas mensalidades: R$ " + totalProxMensalidades);
			lista.add("Total de próximas matrículas: R$ " + totalMatriculas);

			return lista;
		}

	}

	private Financeiras financeiras = new Financeiras();

	public Financeiras financeiras() {
		return financeiras;
	}

}
