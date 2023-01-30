package com.ezasm.parsing;

/**
 * An exception relating to parsing or lexing.
 */
public class ParseException extends Exception {

    /**
     * Default constructor; states that there was an error parsing the line as the message.
     */
    public ParseException() {
        super("Error in parsing the given line");
    }

    /**
     * Gives the custom message to the parent constructor.
     *
     * @param message
     *            the custom message.
     */
    public ParseException(String message) {
        super(message);
    }

}
