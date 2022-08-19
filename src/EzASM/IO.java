package EzASM;

import EzASM.parsing.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IO {

    public static String readFile(File file) throws IOException, ParseException {
        if(!file.exists() || !file.canRead() ) {
            throw new ParseException("Could not load specified file");
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        reader.lines().forEachOrdered(sb::append);
        return sb.toString();
    }

}
