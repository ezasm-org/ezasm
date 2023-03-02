package com.ezasm.gui.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.LockSupport;

public class ConsoleInputStream extends InputStream {

    private StringBuffer buffer;

    private static final long DELAY_MS = 50;

    public ConsoleInputStream() {
        this.buffer = new StringBuffer();
    }

    public void addToBuffer(String string) {
        buffer.append(string);
    }

    public void resetBuffer() {
        buffer = new StringBuffer();
    }

    @Override
    public int read() throws IOException {
        while (true) {
            try {
                int c = buffer.charAt(0);
                buffer.deleteCharAt(0);
                return c;
            } catch (Exception e) {
                try {
                    LockSupport.parkNanos(DELAY_MS * 1_000_000);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
