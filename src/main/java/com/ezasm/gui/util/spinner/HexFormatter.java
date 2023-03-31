package com.ezasm.gui.util.spinner;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;

/**
 * Represents a formatter which can parse and print hexadecimal values. Needed to have a hexadecimal spinner.
 */
public class HexFormatter extends DefaultFormatter {

    /**
     * Interprets the given string as a hexadecimal number.
     *
     * @param text String to convert.
     * @return the numeric value of the given hex string.
     * @throws ParseException if there is an error parsing the number
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text.startsWith("0x")) {
            text = text.substring(2);
        }

        if (text.startsWith("-")) {
            throw new ParseException(text, 0);
        }

        try {
            return Integer.valueOf(text, 16);
        } catch (NumberFormatException e) {
            throw new ParseException(text, 0);
        }
    }

    /**
     * Converts the numeric value back into a hexadecimal string.
     *
     * @param value Value to convert.
     * @return the hex string of the given number.
     */
    @Override
    public String valueToString(Object value) {
        return "0x" + Integer.toHexString((int) value).toUpperCase();
    }

    /**
     * Force the interface to use insert mode, not overwrite mode.
     *
     * @return false.
     */
    @Override
    public boolean getOverwriteMode() {
        return false;
    }
}
