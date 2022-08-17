package EzASM.parsing;

public class RegisterToken extends RightHandToken {

    private final String register;

    public RegisterToken(String text) {
        super(text);
        assert Lexer.isRegister(text);
        this.register = text.substring(1);
    }

    public String getRegister() {
        return register;
    }

}
