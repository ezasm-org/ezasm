package EzASM.instructions;

import EzASM.Conversion;
import EzASM.Registers;
import EzASM.instructions.targets.input.ImmediateInput;
import EzASM.instructions.targets.input.RegisterInput;
import EzASM.instructions.targets.output.RegisterOutput;
import EzASM.parsing.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DispatchInstruction {

    private final Method invocationTarget;
    private final Class<?> parent;

    public DispatchInstruction(Class<?> parent, Method invocationTarget) {
        this.parent = parent;
        this.invocationTarget = invocationTarget;
    }

    public Class<?> getParent() {
        return parent;
    }

    public void invoke(Object parent, Line line) {

        Object[] args = new Object[line.getArguments().length + 1];
        // TODO convert tokens to instruction targets

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
