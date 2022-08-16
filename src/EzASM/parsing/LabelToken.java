package EzASM.parsing;

public class LabelToken extends Token {

    private final String label;
    private final long lineNumber;

    public LabelToken(String text, long lineNumber) {
        super(text);
        this.label = text.substring(0, text.length()-1);
        this.lineNumber = lineNumber;
    }

    public String getLabel() {
        return label;
    }

    public long getLineNumber() {
        return lineNumber;
    }
}
