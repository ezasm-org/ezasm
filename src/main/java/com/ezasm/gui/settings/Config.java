package com.ezasm.gui.settings;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.util.OperatingSystemUtils;
import com.ezasm.util.SystemStreams;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import static java.util.Map.entry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents the configuration of the program. Stores the configuration persistently at the given path.
 */
public class Config {

    /**
     * The configuration file for EzASM
     */
    private static final File CONFIG_FILE = new File(OperatingSystemUtils.EZASM_CONFIG_FILE);

    // All of the names of the configuration settings available to the user
    public static final String FONT_SIZE = "FONT_SIZE";
    public static final String SIMULATION_DELAY = "SIMULATION_SPEED";
    public static final String THEME = "THEME";
    public static final String TAB_SIZE = "TAB_SIZE";
    public static final String FONT_FAMILY = "FONT_FAMILY";
    public static final String AUTO_SAVE_INTERVAL = "AUTO_SAVE_INTERVAL";
    public static final String AUTO_SAVE_SELECTED = "AUTO_SAVE_SELECTED";
    public static final String MEMORY_RANDOMIZE_ON_RESET = "MEMORY_RANDOMIZE_ON_RESET";

    // Font size limits
    public static final String MIN_FONT_SIZE = "10"; // very small sizes make it difficult to set font size again
    public static final String MAX_FONT_SIZE = "60"; // very large sizes may prevent UI from loading

    // All default settings
    public static final String DEFAULT_FONT_SIZE = "16";
    public static final String DEFAULT_TAB_SIZE = "2";
    public static final String DEFAULT_AUTO_SAVE_SELECTED = "false";
    public static final String DEFAULT_AUTO_SAVE_INTERVAL = "10";
    public static final String DEFAULT_MEMORY_RANDOMIZE_ON_RESET = "true";
    public static final String DEFAULT_SIMULATION_DELAY = "250";
    public static final String DEFAULT_THEME = EditorTheme.Light.name();
    public static final String DEFAULT_FONT = "JetBrains Mono"; // unclear if this will be allowed to change

    private final Map<String, String> defaultProperties = Map.ofEntries(entry(FONT_SIZE, DEFAULT_FONT_SIZE),
            entry(TAB_SIZE, DEFAULT_TAB_SIZE), entry(SIMULATION_DELAY, DEFAULT_SIMULATION_DELAY),
            entry(THEME, DEFAULT_THEME), entry(FONT_FAMILY, DEFAULT_FONT),
            entry(AUTO_SAVE_INTERVAL, DEFAULT_AUTO_SAVE_INTERVAL),
            entry(AUTO_SAVE_SELECTED, DEFAULT_AUTO_SAVE_SELECTED),
            entry(MEMORY_RANDOMIZE_ON_RESET, DEFAULT_MEMORY_RANDOMIZE_ON_RESET));

    private final Map<String, Function<Config, Object>> propertyGetters = Map.ofEntries(
            entry(FONT_SIZE, Config::getFontSize), entry(TAB_SIZE, Config::getTabSize),
            entry(SIMULATION_DELAY, Config::getSimulationDelay), entry(THEME, Config::getTheme),
            entry(FONT_FAMILY, Config::getFont), entry(AUTO_SAVE_INTERVAL, Config::getAutoSaveInterval),
            entry(AUTO_SAVE_SELECTED, Config::getAutoSaveSelected),
            entry(MEMORY_RANDOMIZE_ON_RESET, Config::getMemoryRandomizeOnReset));

    // Possible themes
    private static final String[] THEME_ARRAY = { EditorTheme.Light.name(), EditorTheme.Dracula.name(),
            EditorTheme.Purple.name() };

    /**
     * A vector containing the types of possible themes.
     */
    public static final Vector<String> THEMES = new Vector<>(Arrays.asList(THEME_ARRAY));

    private Properties props = new Properties();

    /**
     * Gathers settings from the existing configuration if it exists, or creates a new configuration from the default
     * properties specified.
     */
    public Config() {
        if (CONFIG_FILE.exists()) {
            props = readProperties();
            validateConfig();
            System.out
                    .println("Config class made, config file exists: " + props.getProperty(MEMORY_RANDOMIZE_ON_RESET));
        } else {
            props.setProperty(FONT_SIZE, DEFAULT_FONT_SIZE);
            props.setProperty(SIMULATION_DELAY, DEFAULT_SIMULATION_DELAY);
            props.setProperty(THEME, DEFAULT_THEME);
            props.setProperty(TAB_SIZE, DEFAULT_TAB_SIZE);
            props.setProperty(AUTO_SAVE_INTERVAL, DEFAULT_AUTO_SAVE_INTERVAL);
            props.setProperty(AUTO_SAVE_SELECTED, DEFAULT_AUTO_SAVE_SELECTED);
            props.setProperty(MEMORY_RANDOMIZE_ON_RESET, DEFAULT_MEMORY_RANDOMIZE_ON_RESET);
            saveChanges();
            System.out.println(
                    "Config class made, config file doesn't exist: " + props.getProperty(MEMORY_RANDOMIZE_ON_RESET));
        }
    }

    /**
     * Gets the program font size.
     *
     * @return the program font size.
     */
    public int getFontSize() {
        return Integer.parseInt(props.getProperty(FONT_SIZE));
    }

    /**
     * Sets the program font size.
     *
     * @param size the program font size.
     */
    public void setFontSize(int size) {
        int minSize = Integer.parseInt(MIN_FONT_SIZE);
        int maxSize = Integer.parseInt(MAX_FONT_SIZE);
        size = Math.min(maxSize, Math.max(minSize, size));
        props.setProperty(FONT_SIZE, String.valueOf(size));
    }

    /**
     * Gets the program font.
     *
     * @return the program font.
     */
    public Font getFont() {
        return new Font(DEFAULT_FONT, Font.PLAIN, getFontSize());
    }

    /**
     * Gets the program theme.
     *
     * @return the program theme.
     */
    public EditorTheme getTheme() {
        return EditorTheme.getTheme(props.getProperty(THEME));
    }

    /**
     * Sets the program theme.
     *
     * @param newTheme the program theme.
     */
    public void setTheme(String newTheme) {
        props.setProperty(THEME, newTheme);
    }

    /**
     * Gets the simulation delay in ms.
     *
     * @return the simulation delay in ms.
     */
    public int getSimulationDelay() {
        return Integer.parseInt(props.getProperty(SIMULATION_DELAY));
    }

    /**
     * Sets the simulation delay in ms.
     *
     * @param delay the simulation delay in ms.
     */
    public void setSimulationDelay(int delay) {
        props.setProperty(SIMULATION_DELAY, String.valueOf(delay));
    }

    /**
     * Gets the editor tab size.
     *
     * @return the editor tab size.
     */
    public int getTabSize() {
        return Integer.parseInt(props.getProperty(TAB_SIZE));
    }

    /**
     * Sets the editor tab size.
     *
     * @param size the editor tab size.
     */
    public void setTabSize(int size) {
        props.setProperty(TAB_SIZE, String.valueOf(size));
    }

    /**
     * Gets the autosave interval in seconds.
     *
     * @return the autosave interval in seconds.
     */
    public int getAutoSaveInterval() {
        return Integer.parseInt(props.getProperty(AUTO_SAVE_INTERVAL));
    }

    /**
     * Sets the autosave interval in seconds.
     *
     * @param intervalSeconds the autosave interval in seconds.
     */
    public void setAutoSaveInterval(int intervalSeconds) {
        props.setProperty(AUTO_SAVE_INTERVAL, String.valueOf(intervalSeconds));
    }

    /**
     * Gets whether autosave is enabled.
     *
     * @return true if autosave is enabled, false otherwise.
     */
    public boolean getAutoSaveSelected() {
        return Boolean.parseBoolean(props.getProperty(AUTO_SAVE_SELECTED));
    }

    /**
     * Sets the status of autosave being enabled.
     *
     * @param enabled the status of autosave being enabled.
     */
    public void setAutoSaveSelected(boolean enabled) {
        props.setProperty(AUTO_SAVE_SELECTED, String.valueOf(enabled));
    }

    /**
     * Gets whether memory randomization on reset is enabled.
     *
     * @return true if memory randomization is enabled, false otherwise.
     */
    public boolean getMemoryRandomizeOnReset() {
        return Boolean.parseBoolean(props.getProperty(MEMORY_RANDOMIZE_ON_RESET));
    }

    /**
     * Sets the status of memory randomization being enabled.
     *
     * @param enabled the status of memory randomization being enabled.
     */
    public void setMemoryRandomizeOnReset(boolean enabled) {
        props.setProperty(MEMORY_RANDOMIZE_ON_RESET, String.valueOf(enabled));
    }

    /**
     * Resets all settings in this configuration to their default values.
     */
    public void resetDefaults() {
        this.setTheme(DEFAULT_THEME);
        this.setFontSize(Integer.parseInt(DEFAULT_FONT_SIZE));
        this.setSimulationDelay(Integer.parseInt(DEFAULT_SIMULATION_DELAY));
        this.setTabSize(Integer.parseInt(DEFAULT_TAB_SIZE));
        this.setAutoSaveInterval(Integer.parseInt(DEFAULT_AUTO_SAVE_INTERVAL));
        this.setAutoSaveSelected(false);
        this.setMemoryRandomizeOnReset(Boolean.parseBoolean(DEFAULT_MEMORY_RANDOMIZE_ON_RESET));
    }

    /**
     * Saves the changes to the ezasm configuration folder.
     */
    public void saveChanges() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(CONFIG_FILE);
            props.store(writer, "");
            System.out.println("Configs saved, randomize on reset = " + props.getProperty(MEMORY_RANDOMIZE_ON_RESET));
            writer.close();
        } catch (IOException e) {
            SystemStreams.printlnCurrentErr("Error saving settings");
        }
    }

    /**
     * Validates expected values. If they are null, blank, or of invalid type, resets them to the default value.
     */
    private void validateConfig() {
        for (String property : defaultProperties.keySet()) {
            if (props.getProperty(property) == null || props.getProperty(property).isEmpty()) {
                props.setProperty(property, defaultProperties.get(property));
            } else {
                try {
                    propertyGetters.get(property).apply(this);
                } catch (Exception e) {
                    props.setProperty(property, defaultProperties.get(property));
                }
            }
        }
        saveChanges();
    }

    /**
     * Reads properties from the ezasm configuration file.
     *
     * @return the read properties.
     */
    public Properties readProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream propReader = new FileInputStream(CONFIG_FILE);
            properties.load(propReader);
            propReader.close();
        } catch (IOException e) {
            SystemStreams.printlnCurrentErr("Error loading settings");
        }
        return properties;
    }

}
