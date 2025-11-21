package com.ezasm.gui.util;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.nio.file.Path;
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

    // The themes folder within EzASM's config directory, in the user's file system
    private static final File THEMES_DIRECTORY = new File(OperatingSystemUtils.EZASM_THEMES);

    /**
     * theme values within JSON resources/themes folder are inspired by: 
     * light theme, https://github.com/atom/one-light-syntax, 
     * dark theme, https://github.com/dracula/dracula-theme, 
     * purple theme, https://github.com/endormi/vscode-2077-theme
     */
    private static final String[] DEFAULT_THEME_NAMES = { "Light", "Dark", "Purple" };

    public static EditorTheme Light, Dark, Purple;

    // default themes are retrieved at the start of runtime
    static {
        loadDefaultThemes();
        Light = getTheme("Light");
        Dark = getTheme("Dark");
        Purple = getTheme("Purple");
    }

    // TODO: ConfigurationPreferencesEditor needs to display accurate list of available themes

    /**
     * Loads default theme JSONs into a theme folder within the user's config directory, but only if that folder doesn't
     * exist yet (first time opening app/folder was deleted).
     */
    public static void loadDefaultThemes() {
        if (EditorTheme.THEMES_DIRECTORY.exists()) {
            return;
        }
        try {
            // initialize a new themes directory within the user's EzASM config folder
            EditorTheme.THEMES_DIRECTORY.mkdirs();
            // find each default theme in the theme resources directory and copy it to the user's theme config directory
            for (String themeName : EditorTheme.DEFAULT_THEME_NAMES) {
                InputStream defaultThemeInputStream = EditorTheme.class.getClassLoader()
                        .getResourceAsStream("themes/" + themeName + ".json");
                File newThemeDestination = new File(EditorTheme.THEMES_DIRECTORY, themeName);
                Files.copy(defaultThemeInputStream, newThemeDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            SystemStreams.printlnCurrentErr("Error loading themes");
        }
    }

    /**
     * Accepts a string and returns a Color whose value is that of the string
     *
     * @param s A string (representing a hexadecimal value)
     * @return A Color whose value is that of the input string
     */
    private static Color StringToColor(String s) {
        return new Color(Integer.parseInt(s, 16));
    }

    /**
     * Generates and returns an EditorTheme object based on an input JSONObject input
     *
     * @param json The JSONObject to convert into an EditorTheme
     * @return the generated EditorTheme
     */
    private static EditorTheme JSONObjectToEditorTheme(JSONObject json) {
        return new EditorTheme(json.getString("name"), StringToColor(json.getString("background")),
                StringToColor(json.getString("foreground")), StringToColor(json.getString("currentLine")),
                StringToColor(json.getString("selection")), StringToColor(json.getString("comment")),
                StringToColor(json.getString("cyan")), StringToColor(json.getString("green")),
                StringToColor(json.getString("orange")), StringToColor(json.getString("pink")),
                StringToColor(json.getString("purple")), StringToColor(json.getString("red")),
                StringToColor(json.getString("yellow")), json.getBoolean("isLight"));
    }

    /**
     * Takes a string theme name and returns the corresponding theme object from within the user's config/themes folder.
     *
     * @param s the theme in plain text.
     * @return the theme object.
     */
    public static EditorTheme getTheme(String s) {
        if (!(new File(EditorTheme.THEMES_DIRECTORY, s).exists())) {
            throw new RuntimeException("Theme \"" + s + "\" not found");
        }
        try {
            String themeString = Files.readString(Path.of(EditorTheme.THEMES_DIRECTORY.toString(), s));
            JSONObject json = new JSONObject(themeString);
            return JSONObjectToEditorTheme(json);
        } catch (Exception e) {
            throw new RuntimeException("Theme \"" + s + "\" could not be retrieved");
        }
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
