package com.ezasm;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;

/**
 * A utility class to provide file I/O functionality.
 */
public class FileIO {

    /**
     * Reads the text from a given file.
     *
     * @param file the file to read from.
     * @return the text read.
     * @throws IOException if an error occurred reading from the file.
     */
    public static String readFile(File file) throws IOException {
        if (!file.exists() || !file.canRead()) {
            throw new IOException("Could not load specified file");
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        reader.lines().forEachOrdered(line -> {
            sb.append(line).append(System.lineSeparator());
        });
        return sb.toString();
    }

    /**
     * Writes the given content to the given file.
     *
     * @param file    the file to write to.
     * @param content the String to write.
     * @throws IOException if an error occurred writing to the file.
     */
    public static void writeFile(File file, String content) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    /**
     * Initializes a JFileChooser with the file types which could be associated with code for an EzASM
     * program.
     *
     * @param fileChooser the JFileChooser to act on.
     */
    public static void filterFileChooser(JFileChooser fileChooser) {
        FileFilter ezasm = new QuickFileFilter(".ez", "EzASM file");
        FileFilter plain = new QuickFileFilter(".txt", "Text file");
        fileChooser.addChoosableFileFilter(ezasm);
        fileChooser.addChoosableFileFilter(plain);
        fileChooser.setFileFilter(ezasm);
    }

    /**
     * Helper class to generate a file filter based on an extension and a description.
     */
    private static class QuickFileFilter extends FileFilter {

        private final String extension;
        private final String description;

        public QuickFileFilter(String extension, String description) {
            this.extension = extension;
            this.description = description;
        }

        @Override
        public boolean accept(File file) {
            if (file.isDirectory())
                return true;
            else
                return file.getName().toLowerCase().endsWith(extension);
        }

        @Override
        public String getDescription() {
            return description + String.format(" (*%s)", extension);
        }
    }

}
