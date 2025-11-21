package com.ezasm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File descriptor wrapper for RandomAccessFileStream (read-only file access).
 */
public class FileStreamDescriptor implements FileDescriptor {

    private final RandomAccessFileStream fileStream;

    /**
     * Constructs a file descriptor from a RandomAccessFileStream.
     *
     * @param fileStream the file stream to wrap.
     */
    public FileStreamDescriptor(RandomAccessFileStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public InputStream getInputStream() {
        return fileStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return null; // read-only
    }

    @Override
    public void close() throws IOException {
        fileStream.close();
    }

    /**
     * Gets the underlying file stream for seeking operations.
     *
     * @return the RandomAccessFileStream.
     */
    public RandomAccessFileStream getFileStream() {
        return fileStream;
    }

}
