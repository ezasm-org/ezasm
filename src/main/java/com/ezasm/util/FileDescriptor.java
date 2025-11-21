package com.ezasm.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a file descriptor resource that can be managed in the simulator's FD table. Provides unified access to
 * both file streams and process pipes.
 */
public interface FileDescriptor extends Closeable {

    /**
     * Gets an input stream for reading from this file descriptor. Returns null if reading is not supported.
     *
     * @return the input stream or null if not available.
     */
    InputStream getInputStream();

    /**
     * Gets an output stream for writing to this file descriptor. Returns null if writing is not supported.
     *
     * @return the output stream or null if not available.
     */
    OutputStream getOutputStream();

    /**
     * Closes this file descriptor and releases all associated resources.
     *
     * @throws IOException if an error occurs during closing.
     */
    @Override
    void close() throws IOException;

}
