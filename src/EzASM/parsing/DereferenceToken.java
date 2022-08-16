package EzASM.parsing;

public class DereferenceToken extends RightHandToken {

    private final long offset;
    private final String register;

    public DereferenceToken(String text) {
        super(text);
        int first = text.indexOf('(');
        int last = text.indexOf(')');
        offset = Long.parseLong(text.substring(0, first));
        register = text.substring(first + 1, last);
    }

    public long getOffset() {
        return offset;
    }

    public String getRegister() {
        return register;
    }

}
