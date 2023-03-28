package com.ezasm.gui.tabbedpane;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Implementation of the button on a tab which, when pressed, will close the tab it is connected to.
 */
public class TabCloseButton extends JButton implements IThemeable, ActionListener {
    private final JTabbedPane parent;
    private final String name;
    private static final int SIZE = 17;
    private EditorTheme editorTheme;

    /**
     * Constructs a button based on the tabbed pane parent.
     *
     * @param parent   the parent tabbed pane.
     * @param tabTitle the title of the tab.
     */
    public TabCloseButton(JTabbedPane parent, String tabTitle) {
        this.parent = parent;
        this.name = tabTitle;
        setPreferredSize(new Dimension(SIZE, SIZE));
        setToolTipText("Close this tab");

        setUI(new BasicButtonUI());
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);
        setOpaque(false);
        addActionListener(this);
        addMouseListener(buttonMouseListener);
        setRolloverEnabled(true);
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
        this.editorTheme = editorTheme;
    }

    /**
     * Close the tab if the button is pressed.
     */
    public void actionPerformed(ActionEvent e) {
        int i = parent.indexOfTab(name);
        if (i != -1) {
            parent.remove(i);
        }
    }

    /**
     * Paints the component.
     *
     * @param g The graphics provided by the system.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // shift the image for pressed buttons
        if (getModel().isPressed()) {
            g2.translate(1, 1);
        }
        g2.setStroke(new BasicStroke(2));
        g2.setColor(editorTheme.foreground());
        if (getModel().isRollover()) {
            g2.setColor(editorTheme.yellow());
        }
        int delta = 6;
        g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
        g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
        g2.dispose();
    }

    /**
     * Listener to paint the button differently when hovered.
     */
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton button) {
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton button) {
                button.setBorderPainted(false);
            }
        }
    };
}
