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
import java.util.Map;
import java.util.Objects;

/**
 * Contains a mapping of all instruction String names and their corresponding DispatchInstruction.
 */
public class InstructionDispatcher {

    /**
     * The internal backing map for Strings and loaded instructions.
     */
    private static final HashMap<String, DispatchInstruction> instructions = new HashMap<>();

    static {
        // load the instructions
        registerInstructions(ArithmeticInstructions.class);
    }

    /**
     * Registers instructions from a class. Instructions are registered by
     * scanning the class's declared methods for those annotated with
     * {@link Instruction}. It enumerates the methods and scans their parameters
     * to deduce the appropriate operands.
     * @param clazz The class to register instructions from.
     */
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

    /**
     * Registers a single instruction. The method is assumed to be annotated with {@link Instruction} at this point.
     * This function deduces the operands based on the method's parameters (TODO).
     * @param parent The parent class of the method.
     * @param method The method to register as an instruction.
     */
    private static void registerInstruction(Class<?> parent, Method method) {
        instructions.put(method.getName().toLowerCase(), new DispatchInstruction(parent, method));
    }

    /**
     * Retrieves the map of registered instructions.
     * @return the map of registered Instructions.
     */
    public static Map<String, DispatchInstruction> getInstructions() {
        // TODO immutable map
        // maven library ImmutableMap.of()
        return instructions;
    }

    /**
     * Stores instances of the classes that implement the instructions. For every instantiated InstructionDispatcher,
     * there's a set of instances that manage the instructions. This allows us to bind the Simulator to the instructions.
     */
    private final HashMap<Class<?>, Object> instructionHandlerInstances = new HashMap<>();


    /**
     * The bound simulator for this dispatcher.
     */
    private final Simulator simulator;

    /**
     * Create a new Instruction Dispatcher, and bind it to an existing {@link Simulator}.
     * @param simulator the simulator to bind to.
     */
    public InstructionDispatcher(Simulator simulator) {
        this.simulator = simulator;
        loadInstructionHandlers();
    }

    /**
     * For all registered instructions, load an instance for each handler and bind it to a simulator.
     */
    private void loadInstructionHandlers() {
        InstructionDispatcher.instructions.values().stream()
                .filter(instruction -> instructionHandlerInstances.get(instruction.getParent()) == null)
                .forEach(this::loadInstructionHandler);
    }

    /**
     * Load an instance of an instruction handler for an instruction (or skip if already cached).
     * @param instruction the registered instruction to load an instance for.
     */
    private void loadInstructionHandler(DispatchInstruction instruction) {
        try {
            Constructor<?> constructor = instruction.getParent().getDeclaredConstructor(Simulator.class);
            Object inst = constructor.newInstance(this.simulator);
            this.instructionHandlerInstances.put(instruction.getParent(), inst);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Execute an instruction based on a parsed line.
     * @param line the parsed line.
     * @throws InstructionDispatchException when a parsed line cannot be interpreted as a function.
     * This could be an {@link IllegalInstructionException} if the instruction is unrecognized, or an
     * {@link EzASM.instructions.exception.IllegalArgumentException IllegalArgumentException} if the provided parsed arguments cannot
     * fit to the instruction (not yet implemented).
     */
    public void execute(Line line) throws InstructionDispatchException {
        DispatchInstruction dispatch = instructions.get(line.getInstruction().getText());
        if (dispatch == null) throw new IllegalInstructionException(line.getInstruction().getText());

        Object object = this.instructionHandlerInstances.get(dispatch.getParent());
        // TODO assume loaded for now
        assert object != null;

        dispatch.invoke(object, line);

    }

}
