package br.furb.robotica.common;

import java.io.File;

/**
 * Faz a leitura da entrada do console ou arquivo informado na linha de comando
 * e processa de acordo com a classe que estiver derivando.
 * 
 * @author Gielez Feldhaus Goulart
 * @author William Leander Seefeld
 */
public abstract class BaseMain {

    public static Class<? extends BaseMain> clazz;

    public static void main(String[] args) {
	Scanner sc = null;
	try {
	    if (args.length > 0) {
		sc = new Scanner(new File(args[0]));
	    } else {
		sc = new Scanner(System.in);
	    }
	    if (clazz == null) {
		throw new RuntimeException("nao sobrescreveu direito!");
	    }
	    BaseMain main = clazz.newInstance();
	    main.process(sc);
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {
	    if (sc != null) {
		sc.close();
	    }
	}
    }

    public abstract void process(Scanner sc);

    public static void assertInRange(int val, int min, int max, String varName) {
	if (val > max || val < min) {
	    throw new IllegalArgumentException(String.format(
		    "%s deve estar entre %d e %d. encontrado: %d", varName,
		    min, max, val));
	}
    }
}
