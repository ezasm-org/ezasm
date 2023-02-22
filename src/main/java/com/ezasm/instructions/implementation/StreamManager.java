package com.ezasm.instructions.implementation;

import com.ezasm.gui.Window;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.FileReader;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

import static com.ezasm.gui.util.DialogFactory.promptWarningDialog;

public class StreamManager {

    private InputStream inputStream;
    private OutputStream outputStream;
    private long cursorPosition;

    private Scanner inputReader;
    private PrintStream outputWriter;

    public StreamManager(InputStream inputStream, OutputStream outputStream) {
        setInputStream(inputStream);
        setOutputStream(outputStream);
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.inputReader = new Scanner(this.inputStream);
    }

    public void setOutputStream(OutputStream outputStream) {
        this.cursorPosition = 0;
        this.outputStream = outputStream;
        this.outputWriter = new PrintStream(this.outputStream);
    }

    public void resetInputStream() {
        try {
            if (inputStream instanceof FileReader f) {
                cursorPosition = 0;
                inputReader = new Scanner(new FileReader(new File(Window.getInputFilePath())));
            } else {
                clearBuffer();
            }
        } catch (IOException e) {
            promptWarningDialog("Error Reading File",
                    String.format("There was an error reading from '%s'", Window.getInputFilePath()));
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
            inputReader = new Scanner(inputStream);
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
            cursorPosition = nextPosition;
            try {
                fileReader.seek(cursorPosition);
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

    /**
     * Updates the cursor variable to where the cursor currently is within the file.
     *
     * @throws IOException if an error occurs finding the cursor position.
     */
    private void updateCursor() throws IOException {
        if (inputStream instanceof FileReader fileReader) {
            cursorPosition = fileReader.cursorPosition();
        }
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
            throw new SimulationException("Unable to write double");
        }
    }

    public void write(String s) throws SimulationException {
        try {
            outputWriter.print(s);
        } catch (Exception e) {
            throw new SimulationException("Unable to write double");
        }
    }

    public long readLong() throws SimulationException {
        try {
            long x = inputReader.nextLong();
            updateCursor();
            return x;
        } catch (Exception e) {
            throw new SimulationException("Unable to read double");
        }
    }

    public double readDouble() throws SimulationException {
        try {
            double x = inputReader.nextDouble();
            updateCursor();
            return x;
        } catch (Exception e) {
            throw new SimulationException("Unable to read double");
        }
    }

    public char readChar() throws SimulationException {
        Pattern oldDelimiter = inputReader.delimiter();
        try {
            inputReader.useDelimiter("");
            String current = " ";
            while (current.matches("\\s")) {
                current = inputReader.next();
            }
            updateCursor();
            inputReader.useDelimiter(oldDelimiter);
            return current.charAt(0);
        } catch (Exception e) {
            inputReader.useDelimiter(oldDelimiter);
            throw new SimulationException("Unable to read character");
        }
    }

    public String readString() throws SimulationException {
        try {
            String x = inputReader.next();
            updateCursor();
            return x;
        } catch (Exception e) {
            throw new SimulationException("Unable to read double");
        }
    }

    public String readLine() throws SimulationException {
        try {
            String x = inputReader.nextLine();
            updateCursor();
            return x;
        } catch (Exception e) {
            throw new SimulationException("Unable to read double");
        }
    }

}
