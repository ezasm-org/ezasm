package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.ui.EzComboBoxUI;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.KeyEvent;


/**
 * The GUI settings popup. There can only be one of these instantiated at a time to avoid confusion. Allows the user to
 * configure and save their preferences about program operations.
 */
public class SettingsPopup implements IThemeable {
    private static SettingsPopup instance;
    private List<PreferencesEditor> editors; // 🔧 CHANGED: moved into field for reuse
    public final Config config;

    private ConfigurationPreferencesEditor configEditor;
    private JFrame popup;
    private JButton save, resetDefaults;
    private static final String SAVE = "Save Changes";
    private static final String RESET = "Reset to Defaults";


    /**
     * Constructs a new singleton instance of the settings popup.
     */
    private SettingsPopup() {
        instance = this;
        config = new Config();
        initialize();
    }

    /**
     * Gets the running instance of the settings popup object.
     *
     * @return the running instance of the settings popup object.
     */
    public static SettingsPopup getInstance() {
        return instance;
    }

    /**
     * Instantiates this singleton if it does not already exist.
     */
    public static void instantiate() {
        if (instance == null || !instance.popup.isVisible())
            new SettingsPopup();
    }
    /**
     * Recursively applies the given font and color theme to the specified component and all of its child components.

     */
    private void applyThemeRecursively(Component comp, Font font, EditorTheme theme) {
        if (comp instanceof JComponent jc) {
            jc.setFont(font);
            jc.setBackground(theme.background());
            jc.setForeground(theme.foreground());
            if (jc instanceof JLabel || jc instanceof JButton || jc instanceof JPanel) {
                jc.setOpaque(true);
            }
        }

        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursively(child, font, theme);
            }
        }
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, editorTheme.foreground());
        // Do not call grid.setBackground — instead, apply theme to each PreferencesEditor UI if needed.setBackground(editorTheme.background());
        //fontInput.setCaretColor(editorTheme.foreground());
        for (PreferencesEditor editor : editors) {
            if (editor instanceof IThemeable themeableEditor) {
                themeableEditor.applyTheme(font, editorTheme);
            } else if (editor.getUI() != null) {
                applyThemeRecursively(editor.getUI(), font, editorTheme);
            }
        }

        editorTheme.applyThemeButton(save, font);
        editorTheme.applyThemeButton(resetDefaults, font);
        popup.getContentPane().setBackground(editorTheme.background());
        popup.getContentPane().setForeground(editorTheme.foreground());
        popup.setFont(font);
        popup.revalidate();
        popup.repaint();
    }

    /**
     * Constructs the GUI elements of the settings popup.
     */
    private void initialize() {
        popup = new JFrame("EzASM Settings");
        popup.setLayout(new BorderLayout());
        popup.setMinimumSize(new Dimension(500, 300));
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create consistent editors (NO duplicates)
        configEditor = new ConfigurationPreferencesEditor(config);
        editors = List.of(configEditor); // only config editor

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Configuration", configEditor.getUI());
        tabbedPane.setMnemonicAt(0, configEditor.getMnemonic());

        // Add About tab using AboutPopup panel
        JComponent aboutPanel = AboutPopup.getAboutPanel();
        tabbedPane.addTab("About", aboutPanel);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_A);



        // Save/Reset buttons
        save = new JButton(SAVE);
        resetDefaults = new JButton(RESET);

        save.addActionListener(e -> {
            for (PreferencesEditor editor : editors) {
                editor.savePreferences();
            }
            applyTheme(config.getFont(), config.getTheme());
            Window.getInstance().applyConfiguration(config);
            Window.getInstance().getEditor().applyTheme(config.getFont(), config.getTheme());

        });

        resetDefaults.addActionListener(e -> {
            for (PreferencesEditor editor : editors) {
                editor.matchGuiToDefaultPreferences();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(save);
        buttonPanel.add(resetDefaults);


        popup.add(buttonPanel, BorderLayout.SOUTH);
        for (PreferencesEditor editor : editors) {
            tabbedPane.addTab(editor.getTitle(), editor.getUI());
            tabbedPane.setMnemonicAt(tabbedPane.getTabCount() - 1, editor.getMnemonic());
        }

        popup.add(tabbedPane, BorderLayout.CENTER);
        popup.add(buttonPanel, BorderLayout.SOUTH);

        popup.validate();
        popup.pack();
        popup.setVisible(true);
        this.applyTheme(config.getFont(), config.getTheme());
        popup.pack();
        popup.setVisible(true);

    }



}
