package com.ezasm.instructions.implementation;

import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RandomAccessFileStream;

import java.io.*;

/**
 * A class representing file I/O for use in simulation.
 */
public class StreamManager {

    private InputStream inputStream;
    private OutputStream outputStream;
    private long cursorPosition;

    private BufferedReader inputReader;
    private PrintStream outputWriter;

    /**
     * Construct a stream manager based on an input stream and an output stream.
     *
     * @param inputStream  the input stream to use.
     * @param outputStream the output stream to use.
     */
    public StreamManager(InputStream inputStream, OutputStream outputStream) {
        setInputStream(inputStream);
        setOutputStream(outputStream);
    }

    /**
     * Sets the input stream being used by this object to read from.
     *
     * @param inputStream the new input stream to use.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.inputReader = new BufferedReader(new InputStreamReader(this.inputStream));
    }

    /**
     * Sets the output stream being used by this object to write to.
     *
     * @param outputStream the new output stream to use.
     */
    public void setOutputStream(OutputStream outputStream) {
        this.cursorPosition = 0;
        this.outputStream = outputStream;
        this.outputWriter = new PrintStream(this.outputStream);
    }

    /**
     * Resets the state of the input stream.
     */
    public void resetInputStream() {
        if (inputStream instanceof RandomAccessFileStream f) {
            moveCursor(0);
        } else {
            clearBuffer();
        }
    }

    /**
     * Clears the inputs buffer for use on program end.
     */
    private void clearBuffer() {
        try {
            inputStream.skipNBytes(inputStream.available());
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
        if (inputStream instanceof RandomAccessFileStream fileReader) {
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
     * Gets the cursor's position within the currently open file if it exists, 0 otherwise.
     *
     * @return the cursor's position within the currently open file if it exists, 0 otherwise.
     */
    public long getCursor() {
        return cursorPosition;
    }

    /**
     * Writes a long to the output stream.
     *
     * @param l the long to write.
     * @throws SimulationException if an error occurs in writing the number.
     */
    public void write(long l) throws SimulationException {
        try {
            outputWriter.print(l);
        } catch (Exception e) {
            throw new SimulationException("Unable to write long");
        }
    }

    /**
     * Writes a double to the output stream.
     *
     * @param d the double to write.
     * @throws SimulationException if an error occurs in writing the number.
     */
    public void write(double d) throws SimulationException {
        try {
            outputWriter.print(d);
        } catch (Exception e) {
            throw new SimulationException("Unable to write double");
        }
    }

    /**
     * Writes a character to the output stream.
     *
     * @param c the character to write.
     * @throws SimulationException if an error occurs in writing the character.
     */
    public void write(char c) throws SimulationException {
        try {
            outputWriter.print(c);
        } catch (Exception e) {
            throw new SimulationException("Unable to write character");
        }
    }

    /**
     * Writes a string to the output stream.
     *
     * @param s the string to write.
     * @throws SimulationException if an error occurs in writing the character.
     */
    public void write(String s) throws SimulationException {
        try {
            outputWriter.print(s);
        } catch (Exception e) {
            throw new SimulationException("Unable to write string");
        }
    }

    /**
     * Walks the stream forward until it reaches a non-whitespace character, then returns the character. The cursor is
     * then pointed at the character after the first non-whitespace character found.
     *
     * @return the first non-whitespace character found.
     * @throws SimulationException if an error occurs reading from the stream or if it ends while seeking a char.
     */
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

    /**
     * Walks te stream forward through whitespace until it reaches a string "word", then returns the string.
     *
     * @return the word string found.
     * @throws SimulationException if an error occurs reading from the stream of if it ends while seeking a word.
     */
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
            } while (!Character.isWhitespace(c) && c != -1);

            return sb.toString();
        } catch (Exception e) {
            throw new SimulationException("Unable to read from input stream");
        }
    }

    /**
     * Walks te stream forward through until it reaches the end-of-line sequence, then returns the read string.
     *
     * @return the line string found.
     * @throws SimulationException if an error occurs reading from the stream of if it ends while seeking a word.
     */
    private String walkLine() throws SimulationException {
        String eol = System.lineSeparator();
        // the EOL delimiter is assumed to be of at least length 1
        try {
            StringBuilder sb = new StringBuilder();
            int c = inputReader.read();
            if (c == -1) {
                throw new SimulationException("Reached the end of file while reading");
            } else {
                sb.append((char) c);
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

            return sb.toString();
        } catch (Exception e) {
            throw new SimulationException("Unable to read from input stream");
        }
    }

    /**
     * Reads a long from the input stream.
     *
     * @return the next non-whitespace long read from the input stream.
     * @throws SimulationException if there was an error reading from the stream or the word read was not a long.
     */
    public long readLong() throws SimulationException {
        try {
            return Long.parseLong(walkWord());
        } catch (Exception e) {
            throw new SimulationException("Unable to read long");
        }
    }

    /**
     * Reads a double from the input stream.
     *
     * @return the next non-whitespace double read from the input stream.
     * @throws SimulationException if there was an error reading from the stream or the word read was not a double.
     */
    public double readDouble() throws SimulationException {
        try {
            return Double.parseDouble(walkWord());
        } catch (Exception e) {
            throw new SimulationException("Unable to read double");
        }
    }

    /**
     * Reads a character from the input stream.
     *
     * @return the first non-whitespace character read from the input stream.
     * @throws SimulationException if there was an error reading from the stream.
     */
    public char readChar() throws SimulationException {
        try {
            return walkChar();
        } catch (Exception e) {
            throw new SimulationException("Unable to read character");
        }
    }

    /**
     * Reads a string from the input stream.
     *
     * @return the string read from the input stream.
     * @throws SimulationException if there was an error reading from the stream.
     */
    public String readString() throws SimulationException {
        try {
            return walkWord();
        } catch (Exception e) {
            throw new SimulationException("Unable to read string");
        }
    }

    /**
     * Reads from the input stream until it reaches the end-of-line symbol.
     *
     * @return the line read from the input stream.
     * @throws SimulationException if there was an error reading from the stream.
     */
    public String readLine() throws SimulationException {
        try {
            return walkLine();
        } catch (Exception e) {
            throw new SimulationException("Unable to read line");
        }
    }

}
