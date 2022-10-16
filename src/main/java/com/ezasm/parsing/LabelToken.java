package com.ezasm.parsing;

/**
 * A token which represents a line label.
 */
public class LabelToken extends Token {

    private final String label;
    private final int lineNumber;

    /**
     * Constructs a label based on the text and the line number.
     * Assumes the text has already been validated to be a label.
     * @param text the text of the token.
     * @param lineNumber the line number which the label points to.
     */
    public LabelToken(String text, int lineNumber) {
        super(text);
        this.label = text.substring(0, text.length()-1);
        this.lineNumber = lineNumber;
    }

    /**
     * Gets the label's text (without the ':').
     * @return label's text (without the ':').
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the line number which the label corresponds to.
     * @return the line number which the label corresponds to.
     */
    public long getLineNumber() {
        return lineNumber;
    }
}
