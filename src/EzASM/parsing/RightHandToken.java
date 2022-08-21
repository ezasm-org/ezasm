package EzASM.parsing;

/**
 * Abstract representation of a token on the "right-hand side" of the equation.
 * Includes immediates, registers, dereferences, etc.
 */
public abstract class RightHandToken extends Token {

    /**
     * Stores the token text.
     * @param text the token text to store.
     */
    public RightHandToken(String text) {
        super(text);
    }

}
