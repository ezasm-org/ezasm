package EzASM.parsing;

public class Line {

    private final InstructionToken instruction;
    private final RegisterToken storeRegister;
    private final RightHandToken[] arguments;

    public Line(String instruction, String storeRegister, String[] arguments) {
        this.instruction = new InstructionToken(instruction);
        this.storeRegister = new RegisterToken(storeRegister);
        this.arguments = new RightHandToken[arguments.length];
        for(int i = 0; i < arguments.length; ++i) {
            if(Tokenizer.isImmediate(arguments[i])) {
                this.arguments[i] = new ImmediateToken(arguments[i]);
            } else if(Tokenizer.isRegister(arguments[i])) {
                this.arguments[i] = new RegisterToken(arguments[i]);
            } else if(Tokenizer.isDereference(arguments[i])) {
                this.arguments[i] = new DereferenceToken(arguments[i]);
            }
        }
    }

    public InstructionToken getInstruction() {
        return instruction;
    }

    public RegisterToken getStoreRegister() {
        return storeRegister;
    }

    public RightHandToken[] getArguments() {
        return arguments;
    }
}
