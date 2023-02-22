package com.ezasm.util;

import java.io.*;

public class FileReader extends InputStream {

    RandomAccessFile file;

    public FileReader(File file) throws FileNotFoundException {
        super();
        this.file = new RandomAccessFile(file, "r");
    }

    public void seek(long pos) throws IOException {
        file.seek(pos);
    }

    @Override
    public int read() throws IOException {
        return file.read();
    }

    @Override
    public void reset() throws IOException {
        seek(0);
    }

    @Override
    public int available() throws IOException {
        return (int) file.length();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void close() throws IOException {
        file.close();
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        file.skipBytes((int) n);
    }

}
