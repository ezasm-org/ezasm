package com.ezasm.gui.console;

import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {

    private final Console console;

    public ConsoleOutputStream(Console console) {
        this.console = console;
    }

    @Override
    public void write(int b) throws IOException {
        console.insert(Character.toString(b));
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
        console.insert(toWrite.toString());
    }
}
