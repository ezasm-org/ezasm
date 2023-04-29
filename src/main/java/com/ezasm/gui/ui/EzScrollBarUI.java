package com.ezasm.gui.ui;

import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * ScrollBarUI themed UI to apply to scrollbars in this application's GUI.
 */
public class EzScrollBarUI extends BasicScrollBarUI {

    private final EditorTheme editorTheme;

    /**
     * Constructs a custom scrollbar ui based on an editor theme.
     *
     * @param editorTheme the theme for the scrollbar.
     */
    public EzScrollBarUI(EditorTheme editorTheme) {
        this.editorTheme = editorTheme;
    }

    /**
     * Configures the colors for the scrollbar based on the given editor theme.
     */
    @Override
    protected void configureScrollBarColors() {
        this.trackColor = editorTheme.currentLine();
        this.thumbColor = editorTheme.modifyAwayFromBackground(this.trackColor, 2);
        this.thumbDarkShadowColor = this.thumbColor;
    }

    /**
     * Creates a zero-sized button so that the decrease button does not exist.
     *
     * @param orientation the orientation.
     * @return the zero-sized button.
     */
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    /**
     * Creates a zero-sized button so that the increase button does not exist.
     *
     * @param orientation the orientation.
     * @return the zero-sized button.
     */
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    /**
     * Paints the scrollbar track.
     * @param g the graphics.
     * @param c the component.
     * @param trackBounds the track bounds.
     */
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // Paint manually (if needed)
        super.paintTrack(g, c, trackBounds);
    }

    /**
     * Paints the scrollbar thumb manually.
     *
     * @param g the graphics.
     * @param c the component.
     * @param thumbBounds the thumb bounds.
     */
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
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
