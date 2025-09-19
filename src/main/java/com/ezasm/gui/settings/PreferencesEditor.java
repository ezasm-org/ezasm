package com.ezasm.gui.settings;

import javax.swing.JComponent;

/**
 * Interface for GUI components that manage and apply user-editable preferences. Implementing classes should provide a
 * user interface, title, keyboard mnemonic, and functionality for saving and resetting preferences.
 */
public interface PreferencesEditor {
    /**
     * Returns the root UI component of the preferences' editor. This component can be embedded in a tab or dialog.
     *
     * @return the editor's UI panel or container
     */
    JComponent getUI();

    /**
     * Returns the display title for this preferences section. Used as the label for a tab or dialog section.
     *
     * @return the section title
     */
    String getTitle();

    /**
     * Returns the keyboard mnemonic key code for this preferences section. Used for keyboard navigation within a tabbed
     * settings panel.
     *
     * @return a {@code KeyEvent.VK_*} constant representing the mnemonic
     */
    int getMnemonic();

    /**
     * Applies and persists all preference changes made by the user in the UI. Should perform any necessary validation
     * before saving.
     */
    void savePreferences();

    /**
     * Resets all UI fields to their corresponding default preference values. This does not immediately apply changes;
     * it only updates the UI state.
     */
    void matchGuiToDefaultPreferences();
}
