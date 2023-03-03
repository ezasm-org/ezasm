package com.ezasm.gui.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.LockSupport;

public class ConsoleInputStream extends InputStream {

    private StringBuffer buffer;
    private int bufferIndex;

    private static final long DELAY_MS = 50;

    public ConsoleInputStream() {
        this.buffer = new StringBuffer();
        this.bufferIndex = 0;
    }

    public void addToBuffer(String string) {
        buffer.append(string);
    }

    public void resetBuffer() {
        buffer = new StringBuffer();
        bufferIndex = 0;
    }

    @Override
    public int read() throws IOException {
        while (true) {
            try {
                char c = buffer.charAt(bufferIndex);
                ++bufferIndex;
                return c;
            } catch (Exception e) {
                LockSupport.parkNanos(DELAY_MS * 1_000_000);
            }
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int i = 0;
        while (i < len) {
            try {
                for (; i < len; ++i) {
                    b[off + i] = (byte) buffer.charAt(i + bufferIndex);
                    ++bufferIndex;
                }
            } catch (Exception e) {
                LockSupport.parkNanos(DELAY_MS * 1_000_000 * 10);
            }
        }
        return i;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        return read(b, off, len);
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        byte[] b = new byte[len];
        read(b);
        return b;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return readNBytes(buffer.length());
    }
}
