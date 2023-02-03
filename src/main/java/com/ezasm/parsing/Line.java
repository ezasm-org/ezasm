package com.ezasm.parsing;

/**
 * The tokenized representation of a line. Consists of: one instruction token representing the
 * instruction, one register token representing where the result of the operation will be stored,
 * and any number of "right-hand side" tokens (e.g. any tokens which can represent a value).
 */
public class Line {

    private final InstructionToken instruction;
    private final RegisterToken storeRegister;
    private final RightHandToken[] arguments;

    /**
     * Creates and validates a line based on the given tokens.
     *
     * @param instruction   the String representing the instruction.
     * @param storeRegister the String representing the register to store into for this operation.
     * @param arguments     the variable sized arguments token String list.
     * @throws ParseException if any of the given String tokens cannot be parsed into their
     *                        corresponding types.
     */
    public Line(String instruction, String storeRegister, String[] arguments) throws ParseException {
        if (!Lexer.isRegister(storeRegister)) {
            throw new ParseException("Error parsing register '" + storeRegister + "'");
        }
        if (!Lexer.isInstruction(instruction)) {
            throw new ParseException("Error parsing instruction '" + instruction + "'");
        }

        this.instruction = new InstructionToken(instruction);
        this.storeRegister = new RegisterToken(storeRegister);
        this.arguments = new RightHandToken[arguments.length];

        // Determine the type of each argument and create the token respectively
        for (int i = 0; i < arguments.length; ++i) {
            if (Lexer.isImmediate(arguments[i])) {
                this.arguments[i] = new ImmediateToken(arguments[i]);
            } else if (Lexer.isRegister(arguments[i])) {
                this.arguments[i] = new RegisterToken(arguments[i]);
                // Code for parsing a dereference
                // } else if(Lexer.isDereference(arguments[i])) {
                // this.arguments[i] = new DereferenceToken(arguments[i]);
            } else {
                // The argument did not match any of the given types
                throw new ParseException("Error parsing token '" + arguments[i] + "'");
            }
        }
    }

    /**
     * Gets the instruction token of this line.
     *
     * @return the instruction token.
     */
    public InstructionToken getInstruction() {
        return instruction;
    }

    /**
     * Gets the store register token of this line.
     *
     * @return the store register token.
     */
    public RegisterToken getStoreRegister() {
        return storeRegister;
    }

    /**
     * Gets the "right-hand side" tokens of this line.
     *
     * @return the "right-hand side" tokens.
     */
    public RightHandToken[] getArguments() {
        return arguments;
    }

    /**
     * Gets the interpreted String representation of this line of code.
     *
     * @return the interpreted String representation of this line of code.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(instruction.getText()).append(' ').append(storeRegister.getText()).append(' ');
        for (int i = 0; i < arguments.length - 1; ++i) {
            sb.append(arguments[i].getText()).append(' ');
        }
        if (arguments.length > 0) {
            sb.append(arguments[arguments.length - 1].getText());
        }

        return sb.toString();
    }
}
