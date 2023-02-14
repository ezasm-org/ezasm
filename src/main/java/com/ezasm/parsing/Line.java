package com.ezasm.parsing;

import com.ezasm.instructions.DispatchInstruction;
import com.ezasm.instructions.InstructionPrototype;
import com.ezasm.util.Conversion;
import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.targets.IAbstractTarget;
import com.ezasm.instructions.targets.input.ImmediateInput;
import com.ezasm.instructions.targets.input.LabelReferenceInput;
import com.ezasm.instructions.targets.inputoutput.DereferenceInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.util.RawData;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The representation of a line of code. Consists of an Instruction object and the arguments thereof.
 */
public class Line {

    private final Instruction instruction;
    private final IAbstractTarget[] arguments;
    private final String label;

    /**
     * Creates and validates a line based on the given tokens.
     *
     * @param instruction the String representing the instruction.
     * @param arguments   the variable sized arguments token String list.
     * @throws ParseException if any of the given String tokens cannot be parsed into their corresponding types.
     */
    public Line(String instruction, String[] arguments) throws ParseException {
        if (Lexer.isLabel(instruction)) {
            this.label = instruction.substring(0, instruction.length() - 1);
            this.instruction = null;
            this.arguments = null;
            if (arguments != null && arguments.length > 0) {
                throw new ParseException(String.format("Unexpected token after label: '%s'", arguments[0]));
            }
            return;
        } else if (!Lexer.isInstructionName(instruction)) {
            throw new ParseException("Error parsing instruction '" + instruction + "'");
        }


        this.arguments = new IAbstractTarget[arguments.length];
        this.label = null;

//        if (this.instruction.target().getParameterCount() != arguments.length) {
//            throw new ParseException(
//                    String.format("Incorrect number of arguments for instruction '%s': expected %d but got %d",
//                            instruction, this.instruction.target().getParameterCount(), arguments.length));
//        }

        // Determine the type of each argument and create the token respectively
        for (int i = 0; i < arguments.length; ++i) {
            if (Lexer.isImmediate(arguments[i].toLowerCase())) {
                this.arguments[i] = new ImmediateInput(Lexer.textToBytes(arguments[i].toLowerCase()));
            } else if (Lexer.isCharacterImmediate(arguments[i])) {
                this.arguments[i] = new ImmediateInput(new RawData(Lexer.getCharacterImmediate(arguments[i])));
            } else if (Lexer.isRegister(arguments[i])) {
                this.arguments[i] = new RegisterInputOutput(arguments[i]);
            } else if (Lexer.isDereference(arguments[i])) {
                this.arguments[i] = new DereferenceInputOutput(arguments[i]);
            } else if (Lexer.isLabelReference(arguments[i])) {
                this.arguments[i] = new LabelReferenceInput(arguments[i]);
            } else {
                // The argument did not match any of the given types
                throw new ParseException("Error parsing token '" + arguments[i] + "'");
            }

            // Ensure that the given token is of the required type
//            if (!this.instruction.target().getParameterTypes()[i].isInstance(this.arguments[i])) {
//                throw new ParseException("Expected token of type '"
//                        + this.instruction.target().getParameterTypes()[i].getSimpleName().replace("IAbstract", "")
//                        + "' but got '" + this.arguments[i].getClass().getSimpleName() + "' instead");
//            }

        }

        Class<?>[] argTypes = new Class[this.arguments.length];
        for (int i = 0; i < this.arguments.length; i++) {
            argTypes[i] = this.arguments[i].getClass();
        }

        InstructionPrototype prototype = new InstructionPrototype(instruction, argTypes);
        System.out.println("Looking For: " + prototype + " " + prototype.hashCode());

        DispatchInstruction dispatchInstruction = InstructionDispatcher.getInstructions().get(prototype);
        if (dispatchInstruction == null) {
            throw new ParseException("Instruction not found");
        }

        this.instruction = new Instruction(instruction,
                dispatchInstruction.getInvocationTarget());
    }

    /**
     * Gets the instruction token of this line.
     *
     * @return the instruction token.
     */
    public Instruction getInstruction() {
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
     * Check if the line is a label.
     *
     * @return true if the line is a label, false otherwise.
     */
    public boolean isLabel() {
        return label != null;
    }

    /**
     * Gets the label text of that line.
     *
     * @return the label text of that line.
     */
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return instruction.text();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Line line = (Line) o;
        return Objects.equals(instruction, line.instruction) && Arrays.equals(arguments, line.arguments)
                && Objects.equals(label, line.label);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(instruction, label);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

}
