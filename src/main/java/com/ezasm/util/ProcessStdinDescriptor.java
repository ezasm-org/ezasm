package com.ezasm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File descriptor wrapper for process stdin stream (write-only).
 */
public class ProcessStdinDescriptor implements FileDescriptor {

    private final ProcessPipe pipe;

    /**
     * Constructs a descriptor for process stdin.
     *
     * @param pipe the process pipe to wrap.
     */
    public ProcessStdinDescriptor(ProcessPipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public InputStream getInputStream() {
        return null; // write-only
    }

    @Override
    public OutputStream getOutputStream() {
        return pipe.getStdin();
    }

    @Override
    public void close() throws IOException {
        pipe.getStdin().close();
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
