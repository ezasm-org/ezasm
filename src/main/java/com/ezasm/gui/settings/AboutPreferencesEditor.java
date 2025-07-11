package com.ezasm.gui.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class AboutPreferencesEditor implements PreferencesEditor {

    private JPanel panel;

    public AboutPreferencesEditor() {
        panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html><h3>EzASM</h3><p>Version 1.0<br>Created by your team.</p></html>"), BorderLayout.CENTER);
    }

    @Override public JComponent getUI() {
        return panel;
    }

    @Override public String getTitle() {
        return "About";
    }

    @Override public int getMnemonic() {
        return KeyEvent.VK_A;
    }

    @Override public void savePreferences() {}

    @Override public void matchGuiToDefaultPreferences() {}
}
