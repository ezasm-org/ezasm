package com.ezasm.gui.ui;

import com.ezasm.gui.util.EditorTheme;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

/**
 * Represents UI theming for a combo-box GUI element.
 */
public class EzComboBoxUI extends BasicComboBoxUI {

    private final EditorTheme editorTheme;

    /**
     * Constructs the combobox with a given theme.
     *
     * @param editorTheme the theme to use.
     */
    public EzComboBoxUI(EditorTheme editorTheme) {
        super();
        this.editorTheme = editorTheme;
    }

    /**
     * Paints the combo box.
     *
     * @param g        an instance of {@code Graphics}.
     * @param bounds   a bounding rectangle to render to.
     * @param hasFocus is focused.
     */
    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        super.paintCurrentValueBackground(g, bounds, hasFocus);
        Graphics2D g2 = (Graphics2D) (g);

        Color oldColor = g2.getColor();
        g2.setColor(editorTheme.background());
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        g2.setColor(oldColor);
    }
}
