package EzASM.instructions;

import EzASM.Conversion;
import EzASM.instructions.targets.input.ImmediateInput;
import EzASM.instructions.targets.input.RegisterInput;
import EzASM.instructions.targets.output.RegisterOutput;
import EzASM.parsing.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An instruction which can be dispatched. This has all
 * the information necessary to interpret a parsed {@link Line} and
 * caches it for quick interpretation.
 */
public class DispatchInstruction {

    /**
     * The method that corresponds to the instruction.
     */
    private final Method invocationTarget;

    /**
     * The parent class of the method that corresponds to the instruction.
     */
    private final Class<?> parent;

    /**
     * Create a new dispatchable instruction based on a method with specific parameters and its parent class.
     * @param parent the parent class.
     * @param invocationTarget the method for which to deduce operands for instructions and compile into a dispatchable instruction.
     */
    public DispatchInstruction(Class<?> parent, Method invocationTarget) {
        this.parent = parent;
        this.invocationTarget = invocationTarget;
    }

    /**
     * Gets the parent class of the instruction (the instruction handler).
     * @return a Class object corresponding to the parent class.
     */
    public Class<?> getParent() {
        return parent;
    }

    /**
     * Invoke an instruction based on the parsed line (interpret the arguments and invoke the bound method).
     * @param parent the parent instruction handler. An instance of {@link DispatchInstruction#getParent()}.
     * @param line the parsed line to interpret.
     */
    public void invoke(Object parent, Line line) {

        // TODO, change construction based on argument types. This will be necessary for deref
        Object[] args = new Object[line.getArguments().length + 1];

        args[args.length-1] = new RegisterOutput(line.getStoreRegister().getRegisterNumber());

        for(int i = 0; i < line.getArguments().length; ++i) {
            RightHandToken token = line.getArguments()[i];
            if(token instanceof RegisterToken) {
                args[i] = new RegisterInput(((RegisterToken) token).getRegisterNumber());
            } else if(token instanceof ImmediateToken) {
                args[i] = new ImmediateInput(Conversion.longToBytes(((ImmediateToken) token).getValue()));
            } else {
                // Invalid token type
                throw new IllegalArgumentException();
            }
        }
        try {
            this.invocationTarget.invoke(parent, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO handle
            throw new RuntimeException(e);
        }

    }

}
