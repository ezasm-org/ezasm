package com.ezasm.gui.ui;

import com.ezasm.gui.util.EditorTheme;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/**
 * Represents the themed UI for a tabbed pane.
 */
public class EzTabbedPaneUI extends BasicTabbedPaneUI {

    private final EditorTheme editorTheme;
    private Color foreground;
    private Color unselectedBackground;
    private Color selectedBackground;

    /**
     * Constructs a themed tabbed pane UI.
     *
     * @param editorTheme the theme to apply.
     */
    public EzTabbedPaneUI(EditorTheme editorTheme) {
        super();
        this.editorTheme = editorTheme;
    }

    /**
     * Installs the UI to the given component.
     *
     * @param component the component where this UI delegate is being installed.
     */
    @Override
    public void installUI(JComponent component) {
        super.installUI(component);
        selectedBackground = editorTheme.background();
        unselectedBackground = editorTheme.currentLine();
        foreground = editorTheme.foreground();
    }

    /**
     * Gets the inset for the tabbed pane.
     *
     * @param tabPlacement the placement (left, right, bottom, top) of the tab.
     * @param tabIndex     the tab index.
     * @return the generated insets.
     */
    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        return new Insets(3, 10, 10, 3);
    }

    /**
     * Does not paint a border for the tab.
     *
     * @param graphics     the graphics context in which to paint
     * @param tabPlacement the placement (left, right, bottom, top) of the tab
     * @param tabIndex     the index of the tab with respect to other tabs
     * @param x            the x coordinate of tab
     * @param y            the y coordinate of tab
     * @param w            the width of the tab
     * @param h            the height of the tab
     * @param isSelected   a {@code boolean} which determines whether or not the tab is selected
     */
    @Override
    protected void paintTabBorder(Graphics graphics, int tabPlacement, int tabIndex, int x, int y, int w, int h,
            boolean isSelected) {
    }

    /**
     * Does not paint the border.
     *
     * @param graphics      the graphics context in which to paint
     * @param tabPlacement  the placement (left, right, bottom, top) of the tab
     * @param selectedIndex the tab index of the selected component
     */
    @Override
    protected void paintContentBorder(Graphics graphics, int tabPlacement, int selectedIndex) {
    }

    /**
     * Paints the text within the tab.
     *
     * @param g            the graphics
     * @param tabPlacement the tab placement
     * @param font         the font
     * @param metrics      the font metrics
     * @param tabIndex     the tab index
     * @param title        the title
     * @param textRect     the text rectangle
     * @param isSelected   selection status
     */
    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title,
            Rectangle textRect, boolean isSelected) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setFont(font);

        View v = getTextViewForTab(tabIndex);
        if (v != null) { // html
            v.paint(graphics2D, textRect);
        } else { // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                if (isSelected) {
                    graphics2D.setColor(foreground);
                } else {
                    graphics2D.setColor(editorTheme.modifyAwayFromBackground(unselectedBackground, 3));
                }
                BasicGraphicsUtils.drawStringUnderlineCharAt(tabPane, graphics2D, title, mnemIndex, textRect.x,
                        textRect.y + metrics.getAscent());
            } else { // tab disabled
                graphics2D.setColor(unselectedBackground.brighter());
                BasicGraphicsUtils.drawStringUnderlineCharAt(tabPane, graphics2D, title, mnemIndex, textRect.x,
                        textRect.y + metrics.getAscent());
                graphics2D.setColor(unselectedBackground.darker());
                BasicGraphicsUtils.drawStringUnderlineCharAt(tabPane, graphics2D, title, mnemIndex, textRect.x - 1,
                        textRect.y + metrics.getAscent() - 1);
            }
        }
    }

    /**
     * Do not paint a focus indicator.
     *
     * @param g            the graphics
     * @param tabPlacement the tab placement
     * @param rects        rectangles
     * @param tabIndex     the tab index
     * @param iconRect     the icon rectangle
     * @param textRect     the text rectangle
     * @param isSelected   selection status
     */
    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
            Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    }

    /**
     * Paints the tab background based on whether the tab is selected.
     *
     * @param g            the graphics context in which to paint
     * @param tabPlacement the placement (left, right, bottom, top) of the tab
     * @param tabIndex     the index of the tab with respect to other tabs
     * @param x            the x coordinate of tab
     * @param y            the y coordinate of tab
     * @param w            the width of the tab
     * @param h            the height of the tab
     * @param isSelected   a {@code boolean} which determines whether or not the tab is selected
     */
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
            boolean isSelected) {
        if (isSelected) {
            g.setColor(selectedBackground);
        } else {
            g.setColor(unselectedBackground);
        }
        g.fillRect(x, y, w, h);
    }
}
