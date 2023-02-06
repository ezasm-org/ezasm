package com.ezasm.parsing;

import com.ezasm.Conversion;
import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.targets.IAbstractTarget;
import com.ezasm.instructions.targets.input.ImmediateInput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;

import java.lang.reflect.Method;

/**
 * The tokenized representation of a line. Consists of: one instruction token representing the
 * instruction, one register token representing where the result of the operation will be stored,
 * and any number of "right-hand side" tokens (e.g. any tokens which can represent a value).
 */
public class Line {

    private final InstructionToken instruction;
    private final IAbstractTarget[] arguments;

    /**
     * Creates and validates a line based on the given tokens.
     *
     * @param instruction   the String representing the instruction.
     * @param arguments     the variable sized arguments token String list.
     * @throws ParseException if any of the given String tokens cannot be parsed into their corresponding types.
     */
    public Line(String instruction, String[] arguments) throws ParseException {
        if (!Lexer.isInstruction(instruction)) {
            throw new ParseException("Error parsing instruction '" + instruction + "'");
        }

        Method instructionTarget = InstructionDispatcher.getInstructions().get(instruction).getInvocationTarget();

        if (instructionTarget.getParameterCount() != arguments.length) {
            throw new ParseException(String.format(
                    "Incorrect number of arguments for instruction '%s': expected %d but got %d", instruction,
                    instructionTarget.getParameterCount(), arguments.length));
        }

        this.instruction = new InstructionToken(instruction);
        this.arguments = new IAbstractTarget[arguments.length];

        // Determine the type of each argument and create the token respectively
        for (int i = 0; i < arguments.length; ++i) {
            if (Lexer.isImmediate(arguments[i])) {
                this.arguments[i] = new ImmediateInput(Conversion.longToBytes(Long.parseLong(arguments[i])));
            } else if (Lexer.isRegister(arguments[i])) {
                this.arguments[i] = new RegisterInputOutput(arguments[i]);
                // Code for parsing a dereference
                // } else if(Lexer.isDereference(arguments[i])) {
                // this.arguments[i] = new DereferenceToken(arguments[i]);
            } else {
                // The argument did not match any of the given types
                throw new ParseException("Error parsing token '" + arguments[i] + "'");
            }
            if (! instructionTarget.getParameterTypes()[i].isInstance(this.arguments[i])) {
                throw new ParseException("Expected token of type '" +
                        instructionTarget.getParameterTypes()[i].getSimpleName().replace("IAbstract", "")
                        + "' but got '" + this.arguments[i].getClass().getSimpleName() + "' instead");
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
     * Gets the "right-hand side" tokens of this line.
     *
     * @return the "right-hand side" tokens.
     */
    public IAbstractTarget[] getArguments() {
        return arguments;
    }

    /**
     * Gets the interpreted String representation of this line of code.
     *
     * @return the interpreted String representation of this line of code.
     */
    @Override
    public String toString() {
        return instruction.getText();
    }
}
