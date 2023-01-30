package com.ezasm.parsing;

public class ImmediateToken extends RightHandToken {

    private final long value;

    /**
     * Constructs the token based on the given text. Assumes the text has already been validated to be an immediate.
     *
     * @param text
     *            the token representing the immediate value.
     */
    public ImmediateToken(String text) {
        super(text);
        assert Lexer.isImmediate(text);
        this.value = Long.parseLong(text);
    }

    /**
     * Gets the value of the immediate.
     *
     * @return the value of the immediate.
     */
    public long getValue() {
        return value;
    }

}
