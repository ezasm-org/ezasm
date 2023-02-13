package com.ezasm.gui;

import com.ezasm.Theme;

import javax.swing.text.BadLocationException;
import java.awt.Font;

public interface IThemeable {

    void applyTheme(Font font, Theme theme) throws BadLocationException;

}
