package com.ezasm.gui.settings;

import com.ezasm.util.SystemStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import static java.util.Map.entry;

import java.util.Arrays;
import java.util.Map;

/**
 * Represents the configuration of the program. Stores the configuration persistently at the given path.
 */
public class Config {

    /**
     * The configuration file name.
     */
    private static final String FILE_NAME = ".ezasmrc";

    /**
     * A path to the config file for EzASM
     */
    private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + FILE_NAME;

    /**
     * The configuration file for EzASM
     */
    private static final File CONFIG_FILE = new File(CONFIG_PATH);

    // All of the names of the configuration settings available to the user
    public static final String FONT_SIZE = "FONT_SIZE";
    public static final String SIMULATION_SPEED = "SIMULATION_SPEED";
    public static final String THEME = "THEME";
    public static final String TAB_SIZE = "TAB_SIZE";
    public static final String FONT_FAMILY = "FONT_FAMILY";
    public static final String AUTO_SAVE = "AUTO_SAVE";

    // All of EzASM's defaults
    public static final String DEFAULT_FONT_SIZE = "12";
    public static final String DEFAULT_TAB_SIZE = "2";
    public static final String DEFAULT_AUTO_SAVE = "off";
    public static final String DEFAULT_SIMULATION_SPEED = "250";
    public static final String DEFAULT_THEME = "Light";
    public static final String DEFAULT_FONT = "Monospaced"; // unclear if this will be allowed to change
    Map<String, String> defaultProperties = Map.ofEntries(entry(FONT_SIZE, DEFAULT_FONT_SIZE),
            entry(TAB_SIZE, DEFAULT_TAB_SIZE), entry(SIMULATION_SPEED, DEFAULT_SIMULATION_SPEED),
            entry(THEME, DEFAULT_THEME), entry(FONT_FAMILY, DEFAULT_FONT), entry(AUTO_SAVE, DEFAULT_AUTO_SAVE));

    // Possible themes
    private static final String[] THEME_ARRAY = { "Light", "Dark", "Purple" };
    public static final Vector<String> THEMES = new Vector<String>(Arrays.asList(THEME_ARRAY));

    private Properties props = new Properties();

    public Config() {
        if (CONFIG_FILE.exists()) {
            props = readProperties();
            for (String s : defaultProperties.keySet()) {
                if (props.getProperty(s) == null) {
                    props.setProperty(s, defaultProperties.get(s));
                }
            }
            saveChanges();
        } else {
            props.setProperty(FONT_SIZE, DEFAULT_FONT_SIZE);
            props.setProperty(SIMULATION_SPEED, DEFAULT_SIMULATION_SPEED);
            props.setProperty(THEME, DEFAULT_THEME);
            props.setProperty(TAB_SIZE, DEFAULT_TAB_SIZE);
            props.setProperty(AUTO_SAVE, DEFAULT_AUTO_SAVE);
            saveChanges();
        }
    }

    public int getFontSize() {
        return Integer.parseInt(props.getProperty(FONT_SIZE));
    }

    public void setFontSize(int size) {
        props.setProperty(FONT_SIZE, String.valueOf(size));
    }

    public String getTheme() {
        return props.getProperty(THEME);
    }

    public void setTheme(String newTheme) {
        props.setProperty(THEME, newTheme);
    }

    public int getSimSpeed() {
        return Integer.parseInt(props.getProperty(SIMULATION_SPEED));
    }

    public void setSimSpeed(int speed) {
        props.setProperty(SIMULATION_SPEED, String.valueOf(speed));
    }

    public int getTabSize() {
        return Integer.parseInt(props.getProperty(TAB_SIZE));
    }

    public void setTabSize(int size) {
        props.setProperty(TAB_SIZE, String.valueOf(size));
    }

    public String getAutoSave() {
        return props.getProperty(AUTO_SAVE);
    }

    public void setAutoSave(String status) {
        props.setProperty(AUTO_SAVE, status);
    }

    public void resetDefaults() {
        this.setTheme(DEFAULT_THEME);
        this.setFontSize(Integer.parseInt(DEFAULT_FONT_SIZE));
        this.setSimSpeed(Integer.parseInt(DEFAULT_SIMULATION_SPEED));
        this.setTabSize(Integer.parseInt(DEFAULT_TAB_SIZE));
        this.setAutoSave(DEFAULT_AUTO_SAVE);
    }

    public void saveChanges() {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE);
            props.store(writer, "");
            writer.close();
        } catch (IOException e) {
            SystemStreams.printlnCurrentErr("Error saving settings");
        }
    }

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
