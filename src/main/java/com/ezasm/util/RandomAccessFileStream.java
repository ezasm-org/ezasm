package com.ezasm.util;

import java.io.*;

/**
 * An input stream which allows access to file contents. Allows for seeking position within the file.
 */
public class RandomAccessFileStream extends InputStream {

    private final RandomAccessFile file;

    /**
     * Constructs a file access stream based on the given file.
     *
     * @param file the file to read from in the stream.
     * @throws FileNotFoundException if the file does not exist.
     */
    public RandomAccessFileStream(File file) throws FileNotFoundException {
        super();
        this.file = new RandomAccessFile(file, "r");
    }

    /**
     * Seeks to the given position in the file.
     *
     * @param pos the position to seek to.
     * @throws IOException if an error occurs in seeking.
     */
    public void seek(long pos) throws IOException {
        file.seek(pos);
    }

    /**
     * Reads one byte from the file. Returns a value in [0,255] normally or [-1] if the end of the file is reached.
     *
     * @return the character read or -1 if the end of the file is reached.
     * @throws IOException if there is an error reading from the file.
     */
    @Override
    public int read() throws IOException {
        return file.read();
    }

    /**
     * Gets the number of bytes available to read in the file from the current cursor position.
     *
     * @return the number of bytes available to read in the file from the current cursor position.
     * @throws IOException if an error occurs accessing the file data.
     */
    @Override
    public int available() throws IOException {
        return (int) (file.length() - file.getFilePointer());
    }

    /**
     * Closes the file stream.
     *
     * @throws IOException if there is an error closing the file stream.
     */
    @Override
    public void close() throws IOException {
        file.close();
    }

    /**
     * Moves the cursor ahead by n bytes.
     *
     * @param n the number of bytes to be skipped.
     * @throws IOException if an error occurs in moving the cursor.
     */
    @Override
    public void skipNBytes(long n) throws IOException {
        file.skipBytes((int) n);
    }

}
