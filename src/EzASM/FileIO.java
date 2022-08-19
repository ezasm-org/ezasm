package EzASM;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;

public class FileIO {

    public static String readFile(File file) throws IOException {
        if(!file.exists() || !file.canRead() ) {
            throw new IOException("Could not load specified file");
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        reader.lines().forEachOrdered( line -> {
            sb.append(line).append(System.lineSeparator());
        });
        return sb.toString();
    }

    public static void writeFile(File file, String content) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    public static void filterFileChooser(JFileChooser fileChooser) {
        FileFilter ezasm = new QuickFileFilter(".ez", "EzASM file");
        FileFilter plain = new QuickFileFilter(".txt", "Text file");
        fileChooser.addChoosableFileFilter(ezasm);
        fileChooser.addChoosableFileFilter(plain);
        fileChooser.setFileFilter(ezasm);
    }


    private static class QuickFileFilter extends FileFilter {

        private final String extension;
        private final String description;

        public QuickFileFilter(String extension, String description) {
            this.extension = extension;
            this.description = description;
        }

        @Override
        public boolean accept(File file) {
            if(file.isDirectory()) return true;
            else return file.getName().toLowerCase().endsWith(extension);
        }

        @Override
        public String getDescription() {
            return description + String.format(" (*%s)", extension);
        }
    }

}
