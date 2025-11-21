package com.ezasm.util;

import java.io.*;

/**
 * Wraps an external process providing access to stdin and stdout streams. This allows bidirectional communication with
 * an external program.
 */
public class ProcessPipe implements Closeable {

    private final Process process;
    private final OutputStream stdin;
    private final InputStream stdout;
    private final InputStream stderr;

    /**
     * Constructs a ProcessPipe from an existing Process.
     *
     * @param process the external process to wrap.
     */
    public ProcessPipe(Process process) {
        this.process = process;
        this.stdin = process.getOutputStream();
        this.stdout = process.getInputStream();
        this.stderr = process.getErrorStream();
    }

    /**
     * Gets the output stream for writing to the process stdin.
     *
     * @return the output stream connected to the process stdin.
     */
    public OutputStream getStdin() {
        return stdin;
    }

    /**
     * Gets the input stream for reading from the process stdout.
     *
     * @return the input stream connected to the process stdout.
     */
    public InputStream getStdout() {
        return stdout;
    }

    /**
     * Gets the input stream for reading from the process stderr.
     *
     * @return the input stream connected to the process stderr.
     */
    public InputStream getStderr() {
        return stderr;
    }

    /**
     * Gets the wrapped process.
     *
     * @return the Process object.
     */
    public Process getProcess() {
        return process;
    }

    /**
     * Checks if the process is still running.
     *
     * @return true if the process is alive, false otherwise.
     */
    public boolean isAlive() {
        return process.isAlive();
    }

    /**
     * Destroys the process and closes all streams.
     *
     * @throws IOException if there is an error closing streams.
     */
    @Override
    public void close() throws IOException {
        try {
            stdin.close();
        } catch (IOException ignored) {
        }
        try {
            stdout.close();
        } catch (IOException ignored) {
        }
        try {
            stderr.close();
        } catch (IOException ignored) {
        }
        if (process.isAlive()) {
            process.destroy();
        }
    }

}
