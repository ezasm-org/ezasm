package com.ezasm.gui.util.spinner;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

/**
 * Represents a factory which can construct a hexadecimal formatter. Needed to have a hexadecimal spinner.
 */
public class HexFormatterFactory extends DefaultFormatterFactory {

    /**
     * Gets the hexadecimal number formatter as the default formatter.
     *
     * @return the hexadecimal number formatter.
     */
    @Override
    public JFormattedTextField.AbstractFormatter getDefaultFormatter() {
        return new HexFormatter();
    }
}
