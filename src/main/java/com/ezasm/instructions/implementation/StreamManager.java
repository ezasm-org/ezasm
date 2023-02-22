package com.ezasm.instructions.implementation;

import com.ezasm.gui.Window;
import com.ezasm.util.FileReader;

import java.io.*;
import java.util.Scanner;

import static com.ezasm.gui.util.DialogFactory.promptWarningDialog;

public class StreamManager {

    private InputStream inputStream;
    private OutputStream outputStream;
    private long cursorPosition;

    private Scanner inputReader;
    private PrintStream outputWriter;

    private static final int INPUT_STREAM_READ_LIMIT = 0x10000;

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

    public Scanner inputReader() {
        return inputReader;
    }

    public PrintStream outputWriter() {
        return outputWriter;
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
}
