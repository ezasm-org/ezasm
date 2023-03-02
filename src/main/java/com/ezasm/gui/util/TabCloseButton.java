package com.ezasm.gui.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
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

public class TabCloseButton extends JButton implements IThemeable {
    private JTabbedPane parent;
    private static int SIZE = 17;

    public TabCloseButton(JTabbedPane parent) {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setToolTipText("Close this tab");

        setUI(new BasicButtonUI());
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);
        addMouseListener(buttonMouseListener);
        setRolloverEnabled(true);
    }

    @Override
    public void applyTheme(Font font, Theme theme) {

    }

    public void actionPerformed(ActionEvent e) {
        int i = parent.indexOfTabComponent(TabCloseButton.this);
        if (i != -1) {
            parent.remove(i);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // shift the image for pressed buttons
        if (getModel().isPressed()) {
            g2.translate(1, 1);
        }
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        if (getModel().isRollover()) {
            g2.setColor(Color.MAGENTA);
        }
        int delta = 6;
        g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
        g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
        g2.dispose();
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}
