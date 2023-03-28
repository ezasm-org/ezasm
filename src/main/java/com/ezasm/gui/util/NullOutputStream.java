package com.ezasm.gui.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * An output stream which does not have an output.
 */
public class NullOutputStream extends OutputStream {

    public static final PrintStream NULL_PRINT_STREAM = new PrintStream(new NullOutputStream());

    /**
     * Writes nothing.
     *
     * @param b required parameter which will not be written.
     */
    @Override
    public void write(int b) { // intentionally left blank
    }
}
