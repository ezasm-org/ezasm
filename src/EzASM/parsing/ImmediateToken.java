package EzASM.parsing;

public class ImmediateToken extends RightHandToken {

    private final long value;

    public ImmediateToken(String text) {
        super(text);
        this.value = Long.parseLong(text);
    }

    public long getValue() {
        return value;
    }


}
