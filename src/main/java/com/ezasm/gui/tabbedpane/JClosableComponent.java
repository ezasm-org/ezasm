package com.ezasm.gui.tabbedpane;

import javax.swing.JPanel;

/**
 * Represents a GUI element which is able to be closed (e.g., by a close button).
 */
public abstract class JClosableComponent extends JPanel {

    /**
     * The closing action of this GUI element.
     *
     * @return whether to close this GUI element.
     */
    public abstract boolean close();
}
