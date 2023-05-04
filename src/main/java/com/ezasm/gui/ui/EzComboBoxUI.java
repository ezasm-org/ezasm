package com.ezasm.gui.ui;

import com.ezasm.gui.util.EditorTheme;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class EzComboBoxUI extends BasicComboBoxUI {

    private final EditorTheme editorTheme;

    public EzComboBoxUI(EditorTheme editorTheme) {
        super();
        this.editorTheme = editorTheme;
    }

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
