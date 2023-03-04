package com.ezasm.gui.console;

import java.io.IOException;
import java.io.OutputStream;

public class ConsoleErrorOutputStream extends OutputStream {

    private final Console console;

    public ConsoleErrorOutputStream(Console console) {
        this.console = console;
    }

    @Override
    public void write(int b) throws IOException {
        console.writeTextFromSystemError(Character.toString(b));
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

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
