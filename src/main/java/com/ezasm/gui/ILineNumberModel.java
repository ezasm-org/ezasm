package com.ezasm.gui;

import java.awt.Rectangle;

/**
 * To be implemented as a private class contained within the editor class
 */
public interface ILineNumberModel {
    /**
     * @return number of lines in the JTextArea
     */
    public int getNumberLines();

    /**
     * @param line
     * @return A rectangle defining the view coordinates of that line
     */
    public Rectangle getLineRect(int line);

}
