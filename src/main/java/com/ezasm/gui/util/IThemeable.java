package com.ezasm.gui.util;

import javax.swing.text.BadLocationException;
import java.awt.Font;

public interface IThemeable {

    void applyTheme(Font font, Theme theme) throws BadLocationException;

}
