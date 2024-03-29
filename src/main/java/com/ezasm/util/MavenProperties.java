package com.ezasm.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Loads static properties defined in the pom.xml file line the program name (artifact id), version, description, and
 * url. To load the pom properties file in development you must first compile using maven. You can add properties to
 * this by adding them to the properties tag in the pom.xml file and then loading them in the static block below.
 */
public class MavenProperties {

    private static final String PROPERTIES_LOCATION = "pom.properties";
    private static final java.util.Properties PROPERTIES;

    /**
     * The program name and artifact id.
     */
    public static final String NAME;

    /**
     * The program version.
     */
    public static final String VERSION;

    /**
     * The program description.
     */
    public static final String DESCRIPTION;

    /**
     * The program's website's url.
     */
    public static final String URL;

    static {
        PROPERTIES = new Properties();
        boolean error = false;
        try {
            PROPERTIES.load(MavenProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_LOCATION));
        } catch (IOException ignored) {
            error = true;
        }
        if (error) {
            NAME = "name error";
            VERSION = "version error";
            DESCRIPTION = "description error";
            URL = "url error";
        } else {
            NAME = PROPERTIES.getProperty("name");
            VERSION = PROPERTIES.getProperty("version");
            DESCRIPTION = PROPERTIES.getProperty("description");
            URL = PROPERTIES.getProperty("url");
        }
    }

}
