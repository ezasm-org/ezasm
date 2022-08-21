package EzASM.parsing;

/**
 * A token which represents a dereference statement.
 */
public class DereferenceToken extends RightHandToken {

    private final long offset;
    private final String register;

    /**
     * Constructs a dereference token based on the given text.
     * Assumes that the given text has already been validated to be a dereference.
     * The text must begin with an immediate value.
     * There must then be a statement comprised of a register surrounded by parenthesis i.e. "($sp)".
     * An example of a valid token text would be "-4($sp)".
     * @param text the dereference token text.
     */
    public DereferenceToken(String text) {
        super(text);
        assert Lexer.isDereference(text);
        int first = text.indexOf('(');
        int last = text.indexOf(')');
        offset = Long.parseLong(text.substring(0, first));
        register = text.substring(first + 1, last);
    }

    /**
     * Gets the offset from the pointer to read.
     * @return the offset from the pointer to read.
     */
    public long getOffset() {
        return offset;
    }

    /**
     * Gets the register containing the reference.
     * @return the register containing the reference.
     */
    public String getRegister() {
        return register;
    }

}
