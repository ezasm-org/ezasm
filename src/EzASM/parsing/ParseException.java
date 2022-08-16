package EzASM.parsing;

public class ParseException extends Exception{

    public ParseException() {
        super("Error in parsing the given line");
    }

    public ParseException(String message) {
        super(message);
    }

}
