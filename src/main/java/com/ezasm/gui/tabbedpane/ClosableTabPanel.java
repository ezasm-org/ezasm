package com.ezasm.gui.tabbedpane;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * <code>IThemeable</code> used to display a closable tab in a <code>ClosableTabbedPane</code>.
 */
public class ClosableTabPanel extends JPanel implements IThemeable {

    private final TabCloseButton button;
    private final JLabel label;

    public ClosableTabPanel(BorderLayout layout, TabCloseButton button, JLabel label) {
        super(layout);
        setOpaque(false);

        add(label, BorderLayout.WEST);
        add(button, BorderLayout.EAST);
        this.label = label;
        this.button = button;
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        setBackground(editorTheme.background());
        button.applyTheme(font, editorTheme);
        label.setFont(font);
        label.setForeground(editorTheme.foreground());
        label.setBorder(new EmptyBorder(0, 0, 0, font.getSize() * 2 / 3));
    }

    /**
     * Sets the text of the tab title.
     *
     * @param text the new tab title.
     */
    public void setLabelText(String text) {
        label.setText(text);
    }

}
