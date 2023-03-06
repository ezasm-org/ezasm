package com.ezasm.gui.util;

import java.io.OutputStream;

/**
 * An output stream which does not have an output.
 */
public class NullOutputStream extends OutputStream {

    /**
     * Writes nothing.
     *
     * @param b required parameter which will not be written.
     */
    @Override
    public void write(int b) { // intentionally left blank
    }
}
