package com.ezasm.gui.tabpanes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

public class TabbedPanes extends JPanel implements IThemeable {
    private JTabbedPane tabbedPane;

    public TabbedPanes() {
        super();

        tabbedPane = new JTabbedPane();
        tabbedPane.setMinimumSize(new Dimension(0, 100));

        JComponent panel1 = makeTextPanel("hi gamer");
        tabbedPane.addTab("Terminal", null, panel1, "Terminal for input and output");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_0);
    }

    public void applyTheme(Font font, Theme theme) {
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
