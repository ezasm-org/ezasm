package com.ezasm.util;

import java.io.File;

/**
 * Utilities for operating system specific code.
 */
public class OperatingSystemUtil {

    /**
     * An enum representing the possible types of known or unknown operating systems.
     */
    public enum OperatingSystem {
        WINDOWS, MAC, UNIX, OTHER
    }

    /**
     * The operating system of the host machine.
     */
    public static final OperatingSystem operatingSystem = getOS();

    /**
     * The path to the configuration folder based on the operating system of the host machine e.g., ~/.config/ezasm .
     */
    public static final String EZASM_CONFIG = getConfigPathByOS() + File.separator + "ezasm";

    /**
     * The path to the configuration file for ezasm based on the operating system of the host machine.
     */
    public static final String EZASM_CONFIG_FILE = EZASM_CONFIG + File.separator + "config.properties";

    /**
     * Gets the configuration path based on the operating system.
     *
     * @return the configuration path for this machine.
     */
    private static String getConfigPathByOS() {
        switch (operatingSystem) {
        case WINDOWS -> {
            return getConfigPathWindows();
        }
        case MAC -> {
            return getConfigPathMac();
        }
        case UNIX -> {
            return getConfigPathUnix();
        }
        default -> {
            return getConfigPathUnknownOS();
        }
        }
    }

    /**
     * Gets the OperatingSystem type of the host machine.
     *
     * @return the OperatingSystem type of the host machine.
     */
    private static OperatingSystem getOS() {
        String os = System.getProperty("os.name", "").toLowerCase();

        if (os.contains("windows")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("mac os")) {
            return OperatingSystem.MAC;
        } else if (os.contains("ux") || os.contains("ix") || os.contains("bsd") || os.contains("sun")) {
            return OperatingSystem.UNIX;
        } else {
            // Unsupported operating system
            return OperatingSystem.OTHER;
        }
    }

    /**
     * Gets the configuration path for a Windows machine.
     *
     * @return the configuration path for a Windows machine.
     */
    private static String getConfigPathWindows() {
        return System.getenv("APPDATA");
    }

    /**
     * Gets the configuration path for a Mac machine.
     *
     * @return the configuration path for a Mac machine.
     */
    private static String getConfigPathMac() {
        return System.getenv("HOME") + "/Library/Application Support";
    }

    /**
     * Gets the configuration path for a Unix-like machine.
     *
     * @return the configuration path for a Unix-like machine.
     */
    private static String getConfigPathUnix() {
        String configDefault = System.getenv("HOME") + "/.config";
        String config = System.getenv("XDG_CONFIG_DIR");
        if (config == null || config.isEmpty()) {
            return configDefault;
        } else {
            return config;
        }
    }

    /**
     * Gets the configuration path for an unknown machine type.
     *
     * @return the configuration path for an unknown machine type.
     */
    private static String getConfigPathUnknownOS() {
        return System.getProperty("java.io.tmpdir", "");
    }

}
