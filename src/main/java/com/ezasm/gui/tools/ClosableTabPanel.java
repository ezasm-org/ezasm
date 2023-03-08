package com.ezasm.gui.tools;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <code>IThemeable</code> used to display a closable tab in a 
 * <code>TabbedPane</code>
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
     * Adds a tab to display the specified component with no mnemonic.
     *
     * @param component the component to be displayed when the tab is selected.
     * @param icon      the icon to be displayed next to the text.
     * @param title     the title of the tab.
     * @param tip       the tooltip displayed when the tab is hovered on.
     */
    @Override
    public void applyTheme(Font font, Theme theme) {
        setBackground(theme.background());
        button.applyTheme(font, theme);
        label.setFont(font);
        label.setForeground(theme.foreground());
    }

}
