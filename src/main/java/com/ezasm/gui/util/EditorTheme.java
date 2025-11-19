package com.ezasm.gui.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.json.JSONObject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.Border;

import com.ezasm.gui.ui.EzScrollBarUI;
import com.ezasm.util.OperatingSystemUtils;
import com.ezasm.util.SystemStreams;

/**
 * Represents a theme for components and text in the application.
 */
public record EditorTheme(String name, Color background, Color foreground, Color currentLine, Color selection,
        Color comment, Color cyan, Color green, Color orange, Color pink, Color purple, Color red, Color yellow,
        boolean isLight) {

    private static final String LIGHT_NAME = "Light";
    private static final String DARK_NAME = "Dark";
    private static final String PURPLE_NAME = "Purple";

    /**
     * A light theme based on <a href="https://github.com/atom/one-light-syntax">this</a>.
     */
    public static EditorTheme Light = new EditorTheme(LIGHT_NAME, // name
            new Color(0xebf8ff), // background
            new Color(0x161b1d), // foreground
            new Color(0xc1e4f6), // currentLine
            new Color(0x7ea2b4), // selection
            new Color(0x383a42), // comment
            new Color(0x004E9D), // cyan , used for reserved word tokens
            new Color(0x1F811D), // green , used for char and sting literal tokens
            new Color(0xAD5A00), // orange , used for number literal tokens
            new Color(0x8c329a), // pink , used for variable (register) tokens
            new Color(0x9604C7), // purple , used for X button to close out documents
            new Color(0xC70D05), // red , used for error tokens, error stream, and active registers
            new Color(0xFFEC1A), // yellow , used for text/register highlighter
            true); // is a light theme

    /**
     * A dark theme based on <a href="https://github.com/dracula/dracula-theme">this</a>.
     */
    public static EditorTheme Dracula = new EditorTheme(DARK_NAME, // name
            new Color(0x282a36), // background
            new Color(0xf8f8f2), // foreground
            new Color(0x44475a), // currentLine
            new Color(0x44475a), // selection
            new Color(0x6272a4), // comment
            new Color(0x8be9fd), // cyan
            new Color(0x50fa7b), // green
            new Color(0xffb86c), // orange
            new Color(0xff79c6), // pink
            new Color(0xbd93f9), // purple
            new Color(0xff5555), // red
            new Color(0xf1fa8c), // yellow
            false); // is a light theme

    /**
     * A purple theme based on <a href="https://github.com/endormi/vscode-2077-theme">this</a>.
     */
    public static EditorTheme Purple = new EditorTheme(PURPLE_NAME, // name
            new Color(0x030d22), // background
            new Color(0xfdfeff), // foreground
            new Color(0x310072), // currentLine
            new Color(0x35008b), // selection
            new Color(0x6272a4), // comment
            new Color(0x0ab2fa), // cyan
            new Color(0x06ad00), // green
            new Color(0xffd400), // orange
            new Color(0xea00d9), // pink
            new Color(0x9778C9), // purple
            new Color(0xF40D00), // red
            new Color(0xffff99), // yellow
            false); // is a light theme

    /**
     * The themes folder within EzASM's config directory
     */
    private static final File THEMES_DIRECTORY = new File(OperatingSystemUtils.EZASM_THEMES);

    // Possible themes
    private static final String[] DEFAULT_THEMES = { EditorTheme.Light.name(), EditorTheme.Dracula.name(),
            EditorTheme.Purple.name() };

    // plan:
    // one function to load default themes to the themes config folder le if they don't exist
    // array that contains all the current themes within the themes config folder
    // AND function to update this array
    // function to read json file from themes config folder into a themes object to send around
    // settings in ConfigurationPreferencesEditor will request a copy of the themes array
    // to display all current theme jsons available

    /**
     * Loads the theme JSONs from within the themes directory into EditorTheme objects.
     */
    public static void loadThemes() {
        System.out.println(EditorTheme.THEMES_DIRECTORY);
        if (EditorTheme.THEMES_DIRECTORY != null) {
            return;
        }
        try {
            EditorTheme.THEMES_DIRECTORY.mkdirs();
            for (String name : DEFAULT_THEMES) {
                System.out.println(EditorTheme.THEMES_DIRECTORY + " " + name);
                File THEME_FILE = new File(EditorTheme.THEMES_DIRECTORY, name);
                FileWriter writer = new FileWriter(THEME_FILE);
                //use Files.copy after this
                //Files.copy(file1.toPath(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (IOException e) {
            SystemStreams.printlnCurrentErr("Error loading themes");
        }
    }

    // File[] themeFiles = THEMES_DIRECTORY.listFiles();
    // if (themeFiles != null) {
    // try {
    // for (File themeFile : themeFiles) {
    // JSONObject themeJSON = new JSONObject(new FileReader(themeFile));
    // System.out.println(themeJSON);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // } else {
    // throw new RuntimeException("Theme directory not found");
    // }

    // /**
    // * Saves the changes to the ezasm configuration folder.
    // */
    // public void saveChanges() {
    // try {
    // CONFIG_FILE.getParentFile().mkdirs();
    // FileWriter writer = new FileWriter(CONFIG_FILE);
    // props.store(writer, "");
    // writer.close();
    // } catch (IOException e) {
    // SystemStreams.printlnCurrentErr("Error saving settings");
    // }
    // }

    /**
     * Takes a string theme name and returns the corresponding theme object.
     *
     * @param s the theme in plain text.
     * @return the theme object.
     */
    public static EditorTheme getTheme(String s) {
        return switch (s) {
        case LIGHT_NAME -> EditorTheme.Light;
        case DARK_NAME -> EditorTheme.Dracula;
        case PURPLE_NAME -> EditorTheme.Purple;
        default -> throw new RuntimeException("Theme not found");
        };
    }

    /**
     * Change a color relative to the theme's background color, if it's dark make it brighter and vice versa.
     *
     * @param c the color to be modified.
     *
     * @return new color.
     */
    public Color modifyAwayFromBackground(Color c) {
        return isLight ? c.darker() : c.brighter();
    }

    /**
     * Change a color relative to the theme's background color multiple times, if it's dark make it brighter and vice
     * versa.
     *
     * @param c     the color to be modified.
     * @param times how many times to do the modification.
     *
     * @return the new color.
     */
    public Color modifyAwayFromBackground(Color c, int times) {
        for (int i = 0; i < times; ++i) {
            c = modifyAwayFromBackground(c);
        }
        return c;
    }

    /**
     * Change a color relative to the theme's background color, if it's dark make it darker and vice versa.
     *
     * @param c the color to be modified.
     *
     * @return new color.
     */
    public Color modifyTowardsBackground(Color c) {
        return isLight ? c.brighter() : c.darker();
    }

    /**
     * Change a color relative to the theme's background color multiple times, if it's dark make it darker and vice
     * versa.
     *
     * @param c     the color to be modified.
     * @param times how many times to do the modification.
     *
     * @return the new color.
     */
    public Color modifyTowardsBackground(Color c, int times) {
        for (int i = 0; i < times; ++i) {
            c = modifyAwayFromBackground(c);
        }
        return c;
    }

    /**
     * Apply a given theme to a given component.
     *
     * @param component the component to be themed.
     */
    public void applyTheme(JComponent component) {
        component.setBackground(background);
        component.setForeground(foreground);
        component.setOpaque(true);
        if (component instanceof JPanel || component instanceof JLabel) {
            component.setBorder(BorderFactory.createLineBorder(foreground));
        }

        if (component instanceof JButton button) {
            button.setOpaque(true);
            button.setBorderPainted(true);
            button.setFocusPainted(false);
            button.setContentAreaFilled(true);
        }
        for (Component child : component.getComponents()) {
            if (!(child instanceof IThemeable) && child instanceof JComponent jcomponent) {
                applyTheme(jcomponent);
            }
        }
    }

    /**
     * Apply a given theme to a given component, but without a border.
     *
     * @param component the component to be themed.
     */
    public void applyThemeBorderless(JComponent component) {
        applyTheme(component);
        component.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Apply a given theme to a scrollbar specifically.
     *
     * @param scrollbar the scrollbar to be themed.
     */
    public void applyThemeScrollbar(JScrollBar scrollbar) {
        applyThemeBorderless(scrollbar);
        scrollbar.setUI(new EzScrollBarUI(this));
    }

    /**
     * Apply a given theme to a button specifically.
     *
     * @param button the button to be themed.
     * @param font   the font for the button text.
     */
    public void applyThemeButton(AbstractButton button, Font font) {
        Color buttonBackgroundColor = modifyAwayFromBackground(background);
        Color borderColor = modifyAwayFromBackground(buttonBackgroundColor);
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor);

        button.setForeground(foreground);
        button.setBackground(buttonBackgroundColor);
        button.setBorder(border);
        button.setFont(font);
    }

    /**
     * Applies a font and theme to the given component.
     *
     * @param component   the component to apply theming to.
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public static void applyFontTheme(JComponent component, Font font, EditorTheme editorTheme) {
        editorTheme.applyTheme(component);
        component.setFont(font);
    }

    /**
     * Applies a font and theme to the given component then removes its borders.
     *
     * @param component   the component to apply theming to.
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public static void applyFontThemeBorderless(JComponent component, Font font, EditorTheme editorTheme) {
        editorTheme.applyThemeBorderless(component);
        component.setFont(font);
    }

    /**
     * Applies a font, theme, and border to the given component.
     *
     * @param component   the component to apply theming to.
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     * @param border      the border to apply.
     */
    public static void applyFontThemeBorder(JComponent component, Font font, EditorTheme editorTheme, Border border) {
        editorTheme.applyThemeBorderless(component);
        component.setBorder(border);
        component.setFont(font);
    }

}
