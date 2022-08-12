package EzASM.parsing;

public class Line {

    private final InstructionToken instruction;
    private final RegisterToken storeRegiser;
    private final RightHandToken[] arguments;

    public Line(String instruction, String storeRegister, String[] arguments) {
        this.instruction = new InstructionToken(instruction);
        this.storeRegiser = new RegisterToken(storeRegister);
        this.arguments = new RightHandToken[arguments.length];
        for(int i = 0; i < arguments.length; ++i) {

        }
    }

}
