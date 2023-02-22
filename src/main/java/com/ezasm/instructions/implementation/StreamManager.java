package com.ezasm.instructions.implementation;

import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.FileReader;

import java.io.*;

public class StreamManager {

    private InputStream inputStream;
    private OutputStream outputStream;
    private long cursorPosition;

    private BufferedReader inputReader;
    private PrintStream outputWriter;

    public StreamManager(InputStream inputStream, OutputStream outputStream) {
        setInputStream(inputStream);
        setOutputStream(outputStream);
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.inputReader = new BufferedReader(new InputStreamReader(this.inputStream));
    }

    public void setOutputStream(OutputStream outputStream) {
        this.cursorPosition = 0;
        this.outputStream = outputStream;
        this.outputWriter = new PrintStream(this.outputStream);
    }

    public void resetInputStream() {
        if (inputStream instanceof FileReader f) {
            moveCursor(0);
        } else {
            clearBuffer();
        }
    }

    /**
     * Clears the scanner's buffer for use on error and program end.
     */
    private void clearBuffer() {
        try {
            inputStream.skipNBytes(inputStream.available());
            // modifications to the input stream do not update the scanner
            // and the scanner has no way to clear its buffer... so evil hack
            setInputStream(inputStream);
        } catch (Exception ignored) {
        }
    }

    /**
     * If possible, seeks to the given position in the input stream.
     *
     * @param nextPosition the new position to seek to.
     */
    public void moveCursor(long nextPosition) {
        if (inputStream instanceof FileReader fileReader) {
            try {
                fileReader.seek(nextPosition);
                cursorPosition = nextPosition;
                setInputStream(inputStream);
            } catch (IOException e) {
                System.err.println("Unable to seek to new location");
            }
        }
    }

    /**
     * Gets the cursor's position within the currently open file.
     *
     * @return the cursor's position within the currently open file.
     */
    public long getCursor() {
        return cursorPosition;
    }

    public void write(long l) throws SimulationException {
        try {
            outputWriter.print(l);
        } catch (Exception e) {
            throw new SimulationException("Unable to write long");
        }
    }

    public void write(double d) throws SimulationException {
        try {
            outputWriter.print(d);
        } catch (Exception e) {
            throw new SimulationException("Unable to write double");
        }
    }

    public void write(char c) throws SimulationException {
        try {
            outputWriter.print(c);
        } catch (Exception e) {
            throw new SimulationException("Unable to write character");
        }
    }

    public void write(String s) throws SimulationException {
        try {
            outputWriter.print(s);
        } catch (Exception e) {
            throw new SimulationException("Unable to write string");
        }
    }

    private char walkChar() throws SimulationException {
        try {
            int c;
            do {
                c = inputReader.read();
                ++cursorPosition;
            } while (Character.isWhitespace(c));
            if (c == -1) {
                throw new SimulationException("Reached the end of file while reading");
            }
            return (char) c;
        } catch (Exception e) {
            throw new SimulationException("Unable to read from input stream");
        }
    }

    private String walkWord() throws SimulationException {
        try {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = inputReader.read();
                ++cursorPosition;
            } while (Character.isWhitespace(c));
            if (c == -1) {
                throw new SimulationException("Reached the end of file while reading");
            }
            do {
                sb.append((char) c);
                c = inputReader.read();
                ++cursorPosition;
            } while (!Character.isWhitespace(c) && !(c == -1));

            return sb.toString();
        } catch (Exception e) {
            throw new SimulationException("Unable to read from input stream");
        }
    }

    private String walkLine() throws SimulationException {
        String eol = System.lineSeparator();
        // the EOL delimiter is assumed to be of at least length 1
        try {
            StringBuilder sb = new StringBuilder();
            int c = inputReader.read();
            if (c == -1) {
                throw new SimulationException("Reached the end of file while reading");
            }
            while (c != -1) {
                c = inputReader.read();
                ++cursorPosition;
                sb.append((char) c);

                // Check if reached full newline signal
                if (sb.length() >= eol.length() && sb.substring(sb.length() - eol.length()).equals(eol)) {
                    break;
                }
            }

            cursorPosition += inputReader.skip(eol.length() - 1);

            return sb.toString();
        } catch (Exception e) {
            throw new SimulationException("Unable to read from input stream");
        }
    }

    public long readLong() throws SimulationException {
        try {
            return Long.parseLong(walkWord());
        } catch (Exception e) {
            throw new SimulationException("Unable to read long");
        }
    }

    public double readDouble() throws SimulationException {
        try {
            return Double.parseDouble(walkWord());
        } catch (Exception e) {
            throw new SimulationException("Unable to read double");
        }
    }

    public char readChar() throws SimulationException {
        try {
            return walkChar();
        } catch (Exception e) {
            throw new SimulationException("Unable to read character");
        }
    }

    public String readString() throws SimulationException {
        try {
            return walkWord();
        } catch (Exception e) {
            throw new SimulationException("Unable to read string");
        }
    }

    public String readLine() throws SimulationException {
        try {
            return walkLine();
        } catch (Exception e) {
            throw new SimulationException("Unable to read line");
        }
    }

}
