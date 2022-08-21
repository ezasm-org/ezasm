package EzASM.instructions;

import EzASM.simulation.Simulator;
import EzASM.instructions.exception.IllegalInstructionException;
import EzASM.instructions.exception.InstructionDispatchException;
import EzASM.instructions.impl.ArithmeticInstructions;
import EzASM.parsing.Line;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class InstructionDispatcher {

    private static final HashMap<String, DispatchInstruction> instructions = new HashMap<>();

    static {
        registerInstructions(ArithmeticInstructions.class);
    }

    public static void registerInstructions(Class<?> clazz) {
        try {
            clazz.getDeclaredConstructor(Simulator.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Arrays.stream(clazz.getDeclaredMethods())
                .map(c -> {
                    if (c.isAnnotationPresent(Instruction.class)) {
                        return c;
                    } else return null;
                })
                .filter(Objects::nonNull)
                .forEach(method -> registerInstruction(clazz, method));
    }

    private static void registerInstruction(Class<?> parent, Method method) {
        instructions.put(method.getName().toLowerCase(), new DispatchInstruction(parent, method));
    }

    public static HashMap<String, DispatchInstruction> getInstructions() {
        return instructions;
    }

    private final HashMap<Class<?>, Object> instructionHandlerInstances = new HashMap<>();
    private final Simulator simulator;

    public InstructionDispatcher(Simulator simulator) {
        this.simulator = simulator;
        loadInstructionHandlers();
    }


    private void loadInstructionHandlers() {
        InstructionDispatcher.instructions.values().stream()
                .filter(instruction -> instructionHandlerInstances.get(instruction.getParent()) == null)
                .forEach(this::loadInstructionHandler);
    }

    private void loadInstructionHandler(DispatchInstruction instruction) {
        try {
            Constructor<?> constructor = instruction.getParent().getDeclaredConstructor(Simulator.class);
            Object inst = constructor.newInstance(this.simulator);
            this.instructionHandlerInstances.put(instruction.getParent(), inst);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public void execute(Line line) throws InstructionDispatchException {
        DispatchInstruction dispatch = instructions.get(line.getInstruction().getText());
        if (dispatch == null) throw new IllegalInstructionException(line.getInstruction().getText());

        Object object = this.instructionHandlerInstances.get(dispatch.getParent());
        // TODO assume loaded for now
        assert object != null;

        dispatch.invoke(object, line);

    }

}
