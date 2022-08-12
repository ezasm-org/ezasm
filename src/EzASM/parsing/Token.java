package EzASM.parsing;

public abstract class Token {

    private final String text;

    public Token(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
