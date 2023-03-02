package com.ezasm.gui.tabpanes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

public class TabbedPanes extends JPanel implements IThemeable {
    private JTabbedPane tabbedPane;
    private Color selectedBG, unselectedBG, foreground;

    public TabbedPanes() {
        super(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);
        ImageIcon icon = createImageIcon("images/middle.gif");

        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Tab 1", icon, panel1, "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Tab 2", icon, panel2, "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        // Add the tabbed pane to this panel.
        add(tabbedPane);
    }

    public void applyTheme(Font font, Theme theme) {
        UIManager.put("TabbedPane.selected", theme.modifyAwayFromBackground(theme.background(), 3));
        SwingUtilities.updateComponentTreeUI(tabbedPane);
        tabbedPane.setBackground(theme.background());
        tabbedPane.setForeground(theme.foreground());
        tabbedPane.setFont(font);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        return null;
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

}
