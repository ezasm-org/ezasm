package com.ezasm.gui.console;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents the error output stream which prints errors to a console.
 */
public class ConsoleErrorOutputStream extends OutputStream {

    private final Console console;

    /**
     * Constructs an error output stream for the given console.
     *
     * @param console the console to write to.
     */
    public ConsoleErrorOutputStream(Console console) {
        this.console = console;
    }

    /**
     * Interprets the given int as a character and writes it to the console as an error.
     *
     * @param b the {@code byte}.
     *
     * @throws IOException if an error occurs writing the text to the console.
     */
    @Override
    public void write(int b) throws IOException {
        console.writeTextFromSystemError(Character.toString(b));
    }

    /**
     * Writes the given byte array to the console as an error, interpreting each byte as a character.
     *
     * @param b the data.
     *
     * @throws IOException if an error occurs writing the text to the console.
     */
    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Writes the given byte array to the console as an error, interpreting each byte as a character.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an error occurs writing the text to the console.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        StringBuilder toWrite = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            byte data = b[off + i];
            toWrite.append(Character.toString(data));
        }
        console.writeTextFromSystemError(toWrite.toString());
    }
}
