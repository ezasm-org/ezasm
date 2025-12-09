package com.ezasm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File descriptor wrapper for process stdout stream (read-only).
 */
public class ProcessStdoutDescriptor implements FileDescriptor {

    private final ProcessPipe pipe;

    /**
     * Constructs a descriptor for process stdout.
     *
     * @param pipe the process pipe to wrap.
     */
    public ProcessStdoutDescriptor(ProcessPipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public InputStream getInputStream() {
        return pipe.getStdout();
    }

    @Override
    public OutputStream getOutputStream() {
        return null; // read-only
    }

    @Override
    public void close() throws IOException {
        pipe.getStdout().close();
    }

    /**
     * Gets the underlying process pipe.
     *
     * @return the ProcessPipe.
     */
    public ProcessPipe getPipe() {
        return pipe;
    }

}
