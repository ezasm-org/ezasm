package com.ezasm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import java.util.Arrays;

public class Config {

    /**
     * A path to the config file for EzASM
     */
    private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + ".ezasmrc";

    /**
     * The configuration file for EzASM
     */
    private static final File CONFIG_FILE = new File(CONFIG_PATH);

    // All of the names of the configuration settings available to the user
    public static final String FONT_SIZE = "FONT_SIZE";
    public static final String SIMULATION_SPEED = "SIMULATION_SPEED";
    public static final String THEME = "THEME";

    // All of EzASM's defaults
    public static final String DEFAULT_FONT_SIZE = "12";
    public static final String DEFAULT_SIMULATION_SPEED = "250";
    public static final String DEFAULT_THEME = "Light";
    public static final String DEFAULT_FONT = "Liberation Mono"; // unclear if this will be allowed to change

    // Possible themes
    private static final String[] THEMEARRAY = { "Light", "Dark", "Purple" };
    public static final Vector<String> THEMES = new Vector<String>(Arrays.asList(THEMEARRAY));

    private Properties props = new Properties();

    public Config() {
        if (CONFIG_FILE.exists()) {
            props = readProperties();
        } else {
            props.setProperty(FONT_SIZE, DEFAULT_FONT_SIZE);
            props.setProperty(SIMULATION_SPEED, DEFAULT_SIMULATION_SPEED);
            props.setProperty(THEME, DEFAULT_THEME);
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

    public void resetDefaults() {
        this.setTheme(DEFAULT_THEME);
        this.setFontSize(Integer.parseInt(DEFAULT_FONT_SIZE));
        this.setSimSpeed(Integer.parseInt(DEFAULT_SIMULATION_SPEED));
    }

    public void saveChanges() {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE);
            props.store(writer, "");
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR SAVING SETTING");
        }
    }

    public Properties readProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream propReader = new FileInputStream(CONFIG_FILE);
            properties.load(propReader);
            propReader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return properties;
    }

}
