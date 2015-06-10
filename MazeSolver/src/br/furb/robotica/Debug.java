package br.furb.robotica;

import lejos.nxt.Button;

public class Debug {

    public static boolean debug = false;

    public static void print(String message) {
	if (debug) {
	    System.out.print(message);
	}
    }

    public static void println() {
	if (debug) {
	    System.out.println();
	}
    }

    public static void println(String message) {
	if (debug) {
	    System.out.println(message);
	}
    }

    public static void step(String message) {
	if (debug) {
	    System.out.println(message);
	    Button.ENTER.waitForPressAndRelease();
	}
    }

    public static void toggle() {
	debug = !debug;
    }

}
