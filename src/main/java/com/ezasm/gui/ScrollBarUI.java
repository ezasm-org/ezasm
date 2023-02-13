package com.ezasm.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * ScrollBarUI to apply to scrollbars in this application's GUI.
 */
public class ScrollBarUI extends BasicScrollBarUI {

    private final Theme theme;

    public ScrollBarUI(Theme theme) {
        this.theme = theme;
    }

    @Override
    protected void configureScrollBarColors() {
        this.trackColor = theme.currentLine();
        this.thumbColor = this.trackColor.darker().darker();
        this.thumbDarkShadowColor = this.thumbColor;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // Paint manually (if needed)
        super.paintTrack(g, c, trackBounds);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        // Paint thumb manually
        Graphics2D g2 = (Graphics2D) g;

        int borderDiameter = 8, offset = 4;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x + offset, thumbBounds.y + offset, thumbRect.width - offset * 2,
                thumbRect.height - offset * 2, borderDiameter, borderDiameter);
    }

    /**
     * Create a button of zero size to remove the up/down arrow buttons on the scroll bar.
     *
     * @return the zero-size button.
     */
    protected JButton createZeroButton() {
        JButton button = new JButton("zero button");
        Dimension zeroDim = new Dimension(0, 0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }

}
