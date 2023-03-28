package com.ezasm.gui.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.LockSupport;

/**
 * Represents the input stream which reads text from the console.
 */
public class ConsoleInputStream extends InputStream {

    private StringBuffer buffer;
    private int bufferIndex;

    private static final long DELAY_MS = 50;

    /**
     * Constructs the input stream from the console.
     */
    public ConsoleInputStream() {
        this.buffer = new StringBuffer();
        this.bufferIndex = 0;
    }

    /**
     * Add characters typed in the console to the buffer of characters in the input stream.
     *
     * @param string the characters input.
     */
    public void addToBuffer(String string) {
        buffer.append(string);
    }

    /**
     * Resets the internal buffer and hence the input stream.
     */
    public void resetBuffer() {
        buffer = new StringBuffer();
        bufferIndex = 0;
    }

    /**
     * Attempts to read a character from the input buffer until it is able to. This creates functionality where the
     * program will wait to continue until an input is given. The wait in between checks is defined in DELAY_MS.
     *
     * @return the character read.
     * @throws IOException if there is an error reading from the buffer.
     */
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

    /**
     * Reads len bytes into the given array starting at the position off. Attempts to read a character from the input
     * buffer until it is able to. This creates functionality where the program will wait to continue until an input is
     * given. The wait in between checks is defined in DELAY_MS.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array {@code b} at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the number of characters read.
     * @throws IOException if there is an error reading from the buffer.
     */
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

    /**
     * Reads len bytes into the given array starting at the position off. Attempts to read a character from the input
     * buffer until it is able to. This creates functionality where the program will wait to continue until an input is
     * given. The wait in between checks is defined in DELAY_MS.
     *
     * @param b the buffer into which the data is read.
     * @return the number of characters read.
     * @throws IOException if there is an error reading from the buffer.
     */
    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Reads len bytes into the given array starting at the position off. Attempts to read a character from the input
     * buffer until it is able to. This creates functionality where the program will wait to continue until an input is
     * given. The wait in between checks is defined in DELAY_MS.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array {@code b} at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the number of characters read.
     * @throws IOException if there is an error reading from the buffer.
     */
    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        return read(b, off, len);
    }

    /**
     * Reads bytes up to the length given, then returns the created array. Attempts to read a character from the input
     * buffer until it is able to. This creates functionality where the program will wait to continue until an input is
     * given. The wait in between checks is defined in DELAY_MS.
     *
     * @param len the maximum number of bytes to read.
     * @return the array of characters read.
     * @throws IOException if there is an error reading from the buffer.
     */
    @Override
    public byte[] readNBytes(int len) throws IOException {
        byte[] b = new byte[len];
        read(b);
        return b;
    }
}
