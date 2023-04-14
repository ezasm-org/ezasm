package com.ezasm.gui.editor;

import com.ezasm.util.SystemStreams;

import java.io.File;

public class ThemeReader {

    ThemeReader(String fileName, String fileType){
        File file = new File(ThemeWriter.THEMES_DIR + "/" + fileName + "." + fileType);

        switch(fileType) {
            case "xml":
                xmlThemeReader(file);
                break;
            default:
                SystemStreams.err.print("File type \"" + fileType + "\" not supported for reading.");
        }
    }

    private void xmlThemeReader(File file) {
    }
}
