package com.ezasm.util;

import com.ezasm.gui.Window;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.io.FileReader;
import java.util.Objects;

/**
 * A utility class to provide file I/O functionality.
 */
public class FileIO {

    public static final String LINE_SEPARATOR = "\n";

    /**
     * Store the home directory for use with file operations default locations.
     */
    private static final File HOME_DIRECTORY_FILE = SystemUtils.getUserHome();

    /**
     * Get the user's home directory as a file.
     *
     * @return the user's home directory as a file.
     */
    public static File getHomeDirectoryFile() {
        return HOME_DIRECTORY_FILE;
    }

    /**
     * Reads the text from a given file.
     *
     * @param file the file to read from.
     * @return the text read.
     * @throws IOException if an error occurred reading from the file.
     */
    public static String readFile(File file) throws IOException {
        if (!file.exists() || !file.canRead()) {
            throw new IOException(String.format("Could not load specified file %s", file.getPath()));
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        reader.lines().forEachOrdered(line -> sb.append(line).append(LINE_SEPARATOR));
        return sb.toString();
    }

    /**
     * Reads the image from a given file.
     *
     * @param path the path to the image file to read from.
     * @return the image found.
     * @throws IOException if an error occurred reading from the file.
     */
    public static Image loadImage(String path) throws IOException {
        try {
            return new ImageIcon(Objects.requireNonNull(FileIO.class.getClassLoader().getResource(path))).getImage();
        } catch (NullPointerException e) {
            throw new IOException(String.format("Unable to load image from %s", path));
        }
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

    // Extensible file type masks. Larger numbers have higher priority for being the default type selected

    /**
     * The absence of a type mask for constructing a file chooser.
     */
    public static final int NO_FILE_MASK = 0b0000_0000;

    /**
     * Text file type mask for constructing a file chooser.
     */
    public static final int TEXT_FILE_MASK = 0b0000_0001;

    /**
     * Text file type mask for constructing a file chooser.
     */
    public static final int EZ_FILE_MASK = 0b0000_1000;

    /**
     * Construct a file chooser with the given window title and file type selection.
     *
     * @param windowTitle          the title of the popup file chooser window.
     * @param acceptedFileTypeMask the mask of the file type desired e.g., <code>EZ_FILE_MASK & TEXT_FILE_MASK</code>
     *                             for both EzASM files and text files or <code>NO_FILE_MASK</code> to not apply any
     *                             file mask.
     * @return the new JFileChooser object.
     */
    public static JFileChooser createFileChooser(String windowTitle, int acceptedFileTypeMask) {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setDialogTitle(windowTitle);
        String currentFilePath = Window.getInstance().getEditor().getOpenFilePath();
        File currentDir = new File(currentFilePath).getParentFile();
        if (currentDir != null && currentDir.exists() && currentDir.isDirectory()) {
            fileChooser.setCurrentDirectory(currentDir);
        } else {
            fileChooser.setCurrentDirectory(getHomeDirectoryFile());
        }

        // This if statement chain should be ordered from the least significant extension type to most significant
        if ((acceptedFileTypeMask & TEXT_FILE_MASK) != 0) {
            addFileFilter(fileChooser, ".txt", "Text file");
        }
        if ((acceptedFileTypeMask & EZ_FILE_MASK) != 0) {
            addFileFilter(fileChooser, ".ez", "EzASM code file");
        }

        return fileChooser;
    }

    /**
     * Adds the given file filter to the given JFileChooser
     *
     * @param fileChooser the JFileChooser to apply the settings to.
     * @param extension   the file extension.
     * @param description the file extension's description.
     */
    private static void addFileFilter(JFileChooser fileChooser, String extension, String description) {
        FileFilter filter = new QuickFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
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
