package com.ezasm.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class LineNumber extends JComponent {

    private ILineNumberModel model;

    int HORIZONTAL_PADDING = 1;
    int VERTICAL_PADDING = 3;

    public LineNumber() {
        super();
    }

    public LineNumber(ILineNumberModel model) {
        super();
        this.model = model;
    }

    public void adjustWidth() {
        int max = model.getNumberLines();
        if (getGraphics() == null) {
            return;
        }
        int width = getGraphics().getFontMetrics().stringWidth(String.valueOf(max)) + 2 * HORIZONTAL_PADDING;
        JComponent containing = (JComponent) getParent();
        if (containing == null) {
            return;
        }
        Dimension d = containing.getPreferredSize();
        if (containing instanceof JViewport vp) {
            Component parent = vp.getParent();
            if (parent instanceof JScrollPane scroller) {
                d = scroller.getViewport().getPreferredSize();
            }
        }
        if (width != getPreferredSize().width) {
            setPreferredSize(new Dimension(width + 2 * HORIZONTAL_PADDING, d.height));
            revalidate();
            repaint();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (model == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        Rectangle rect;
        String text;
        int xPos, yPos;
        for (int i = 0; i < model.getNumberLines(); i++) {
            rect = model.getLineRect(i);
            text = String.valueOf(i + 1);
            yPos = rect.y + rect.height - VERTICAL_PADDING;
            xPos = getPreferredSize().width - g.getFontMetrics().stringWidth(text) - HORIZONTAL_PADDING;
            g2d.drawString(text, xPos, yPos);
        }

    }
}
