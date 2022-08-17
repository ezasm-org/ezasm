package EzASM.parsing;

import EzASM.Registers;

public class RegisterToken extends RightHandToken {

    private final String register;
    private final int number;

    public RegisterToken(String text) {
        super(text);
        assert Lexer.isRegister(text);
        this.register = text.substring(1);
        this.number = Registers.getRegisterNumber(this.register);
    }

    public String getRegister() {
        return register;
    }

    public int getRegisterNumber() {
        return number;
    }
}
