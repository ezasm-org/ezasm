package com.ezasm.gui.util;

import java.awt.Font;

/**
 * An interface for things that can be themed using EditorTheme.
 */
public interface IThemeable {

    /**
     * Apply a given theme and font to implementer.
     *
     * @param font        the font in question.
     * @param editorTheme the theme in question.
     */
    void applyTheme(Font font, EditorTheme editorTheme);

}
