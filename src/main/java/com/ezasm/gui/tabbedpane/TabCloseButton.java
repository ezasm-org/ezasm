package com.ezasm.gui.tabbedpane;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Implementation of the button on a tab which, when pressed, will close the tab it is connected to.
 */
public class TabCloseButton extends JButton implements IThemeable, ActionListener {
    private final ClosableTabbedPane parent;
    private final String name;
    private static final int SIZE = 17;
    private EditorTheme editorTheme;

    /**
     * Constructs a button based on the tabbed pane parent.
     *
     * @param parent   the parent tabbed pane.
     * @param tabTitle the title of the tab.
     */
    public TabCloseButton(ClosableTabbedPane parent, String tabTitle) {
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
            parent.removeTab(i);
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

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // shift the image for pressed buttons
        if (getModel().isPressed()) {
            g2.translate(1, 1);
        }
        g2.setStroke(new BasicStroke(2.5f));
        g2.setColor(editorTheme.purple());
        if (getModel().isRollover()) {
            g2.setColor(editorTheme.cyan());
        }
        int delta = 3;
        g2.drawLine(delta, getHeight() / 2 + getWidth() / 2 - delta, getWidth() - delta,
                getHeight() / 2 - getWidth() / 2 + delta);
        g2.drawLine(delta, getHeight() / 2 - getWidth() / 2 + delta, getWidth() - delta,
                getHeight() / 2 + getWidth() / 2 - delta);
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
