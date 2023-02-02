package com.ezasm.parsing;

/**
 * Abstract representation of a token from lexed code.
 */
public abstract class Token {

    private final String text;

    /**
     * Stores the text of the token into a local variable.
     *
     * @param text the text of the token
     */
    public Token(String text) {
        this.text = text;
    }

    /**
     * Gets the token text.
     *
     * @return the token text.
     */
    public String getText() {
        return text;
    }

}
