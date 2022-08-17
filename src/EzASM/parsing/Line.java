package EzASM.parsing;

public class Line {

    private final InstructionToken instruction;
    private final RegisterToken storeRegister;
    private final RightHandToken[] arguments;
    // TODO add line number maybe

    public Line(String instruction, String storeRegister, String[] arguments) throws ParseException {
        if(!Lexer.isRegister(storeRegister)) {
            throw new ParseException("Error parsing register '" + storeRegister + "'");
        }
        if(!Lexer.isInstruction(instruction)) {
            throw new ParseException("Error parsing instruction '" + instruction + "'");
        }


        this.instruction = new InstructionToken(instruction);
        this.storeRegister = new RegisterToken(storeRegister);
        this.arguments = new RightHandToken[arguments.length];
        for(int i = 0; i < arguments.length; ++i) {
            if(Lexer.isImmediate(arguments[i])) {
                this.arguments[i] = new ImmediateToken(arguments[i]);
            } else if(Lexer.isRegister(arguments[i])) {
                this.arguments[i] = new RegisterToken(arguments[i]);
            } else if(Lexer.isDereference(arguments[i])) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(instruction.getText()).append(' ').append(storeRegister.getText()).append(' ');
        for(int i = 0; i < arguments.length - 1; ++i) {
            sb.append(arguments[i].getText()).append(' ');
        }
        if(arguments.length > 0) {
            sb.append(arguments[arguments.length-1].getText());
        }

        return sb.toString();
    }
}
