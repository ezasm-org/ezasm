package com.ezasm.gui.settings;


import javax.swing.JComponent;

public interface PreferencesEditor {
    JComponent getUI();
    String getTitle();
    int getMnemonic();
    void savePreferences();
    void matchGuiToDefaultPreferences();
}



