package com.ezasm.util;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Represents system streams. Contains the default console streams and allows printing to the current System.x stream if
 * necessary.
 */
public class SystemStreams {

    /**
     * The system console output.
     */
    public static final PrintStream out = System.out;

    /**
     * The system console error output.
     */
    public static final PrintStream err = System.err;

    /**
     * The system console input.
     */
    public static final InputStream in = System.in;

    /**
     * Print to the current System.out and then print newline.
     *
     * @param text the text to print.
     */
    public static void printlnCurrentOut(String text) {
        System.out.print(text + '\n');
    }

    /**
     * Print to the current System.out.
     *
     * @param text the text to print.
     */
    public static void printCurrentOut(String text) {
        System.out.print(text);
    }

    /**
     * Print to the current System.err and then print newline.
     *
     * @param text the text to print.
     */
    public static void printlnCurrentErr(String text) {
        System.err.print(text + '\n');
    }

    /**
     * Print to the current System.err.
     *
     * @param text the text to print.
     */
    public static void printCurrentErr(String text) {
        System.err.print(text);
    }

}
