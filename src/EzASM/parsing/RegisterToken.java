package EzASM.parsing;

import EzASM.simulation.Registers;

/**
 * A token which represents a register within the registers.
 */
public class RegisterToken extends RightHandToken {

    private final String register;
    private final int number;

    /**
     * Constructs a register token based on the given text.
     * Assumes that the given text has already been validated to be a register.
     * Must begin with '$' and have the suffix contained within the registers list.
     * @param text the register.
     */
    public RegisterToken(String text) {
        super(text);
        assert Lexer.isRegister(text);
        this.register = text.substring(1);
        this.number = Registers.getRegisterNumber(this.register);
    }

    /**
     * Gets the register text corresponding to this register token (without the '$').
     * @return the register text (without the '$')
     */
    public String getRegister() {
        return register;
    }

    /**
     * Gets the number of the register specified by this token.
     * @return the number of the register specified by this token.
     */
    public int getRegisterNumber() {
        return number;
    }
}
