package br.furb.robotica;

import lejos.nxt.Button;

public class Debug {

    public static boolean debug = false;
    private static StringBuilder sb = new StringBuilder();

    public static void print(String message) {
	doPrint(message);
    }

    public static void println() {
	doPrint();
    }

    public static void println(String message) {
	doPrint(message);
    }

    public static void step(String message) {
	doPrint(message);
	//	    Button.ENTER.waitForPressAndRelease();
    }

    public static void toggle() {
	//	debug = !debug;
    }

    private static void doPrint() {
	System.out.println();
    }

    private static void doPrint(String message) {
	if (debug) {
	    System.out.println(message);
	}
	sb.append(message).append("|");
    }

    public static void throwUp() {
	//	System.out.println(sb.substring(Math.max(0, sb.length() - 20)));
	System.out.println(sb.toString());
	Button.ENTER.waitForPressAndRelease();
    }

}
