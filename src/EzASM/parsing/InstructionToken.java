package EzASM.parsing;

/**
 * A token which represents an instruction.
 */
public class InstructionToken extends Token {

    /**
     * Constructs the token based on the given text.
     * Assumes the text has already been validated to be an instruciton.
     * @param text the instruction text.
     */
    public InstructionToken(String text) {
        super(text);
    }

}
