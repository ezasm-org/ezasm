package com.ezasm.gui.tabpanes;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

public class TabbedPanes extends JPanel implements IThemeable {

    private final JTabbedPane tabbedPane;

    public TabbedPanes() {
        super(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new EzTabbedPaneUI(Window.currentTheme()));
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

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font  the font to apply.
     * @param theme the theme to apply.
     */
    public void applyTheme(Font font, Theme theme) {
        setBackground(theme.currentLine());
        setForeground(theme.foreground());
        setFont(font);
        tabbedPane.setUI(new EzTabbedPaneUI(theme));
        tabbedPane.setFont(font);

        for (Component component : tabbedPane.getComponents()) {
            if (component instanceof IThemeable themeable) {
                themeable.applyTheme(font, theme);
            } else {
                component.setBackground(theme.background());
                component.setForeground(theme.foreground());
                component.setFont(font);
            }
        }
    }

    /**
     * Adds a tab to display the specified component with no mnemonic.
     *
     * @param component the component to be displayed when the tab is selected.
     * @param icon      the icon to be displayed next to the text.
     * @param title     the title of the tab.
     * @param tip       the tooltip displayed when the tab is hovered on.
     */
    public void addTab(JComponent component, Icon icon, String title, String tip) {
        addTab(component, icon, title, tip, -1);
    }

    /**
     * Adds a tab to display the specified component with a mnemonic.
     *
     * @param component the component to be displayed when the tab is selected.
     * @param icon      the icon to be displayed next to the text.
     * @param title     the title of the tab.
     * @param tip       the tooltip displayed when the tab is hovered on.
     */
    public void addTab(JComponent component, Icon icon, String title, String tip, int mnemonic) {
        tabbedPane.addTab(title, icon, component, tip);
        if (mnemonic != -1) {
            tabbedPane.setMnemonicAt(tabbedPane.getTabCount() - 1, mnemonic);
        }
    }

    /**
     * Removes the component at a given index.
     *
     * @param index the index of the component to remove.
     */
    public void removeTab(int index) {
        tabbedPane.removeTabAt(index);
    }

    /**
     * Removes the given component if it is in the data structure.
     *
     * @param component the component to remove.
     */
    public void removeTab(JComponent component) {
        for (int i = 0; i < tabbedPane.getTabCount(); ++i) {
            if (tabbedPane.getTabComponentAt(i) == component) {
                removeTab(i);
                return;
            }
        }
    }

    /**
     * Gets the index of a tab given the title.
     *
     * @param title the title to seek for.
     * @return the index of a tab given the title.
     */
    public int indexOfTab(String title) {
        return tabbedPane.indexOfTab(title);
    }

    /**
     * Returns the number of tabs in this object.
     *
     * @return the number of tabs in this object.
     */
    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    /**
     * Gets the component at the given index.
     *
     * @param index the index to seek a component at.
     * @return the component at the given index.
     */
    public JComponent getComponentAt(int index) {
        return (JComponent) tabbedPane.getTabComponentAt(index);
    }

    /**
     * Gets the currently selected component.
     *
     * @return the currently selected component.
     */
    public JComponent getSelectedComponent() {
        return (JComponent) tabbedPane.getSelectedComponent();
    }

    /**
     * Gets the array of components stored in the tab pane.
     *
     * @return the array of components stored in the tab pane.
     */
    public JComponent[] getTabComponents() {
        return (JComponent[]) tabbedPane.getComponents();
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
