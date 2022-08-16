package EzASM.parsing;

public class Line {

    private final InstructionToken instruction;
    private final RegisterToken storeRegister;
    private final RightHandToken[] arguments;
    // TODO add line number maybe


    public Line(String instruction, String storeRegister, String[] arguments) throws ParseException {
        if(!Tokenizer.isRegister(storeRegister)) {
            // TODO add instruction checking
            throw new ParseException("Error parsing register '" + storeRegister + "'");
        }


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
            } else {
                throw new ParseException("Error parsing token '" + arguments[i] + "'");
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
