package EzASM.instructions.exception;

public class IllegalInstructionException extends InstructionDispatchException {
    private final String bad;
    public IllegalInstructionException(String bad) {
        this.bad = bad;
    }

    @Override
    public String getMessage() {
        return "Unknown instruction: " + this.bad;
    }
}