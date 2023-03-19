package com.ezasm.parsing;

import com.ezasm.instructions.exception.IllegalArgumentException;
import com.ezasm.simulation.Registers;
import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.util.RawData;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static context functions regarding lexing and tokenizing Strings.
 */
public class Lexer {

    /**
     * Determines if a character is alphanumeric.
     *
     * @param c the character to inspect.
     * @return true if the character is alphanumeric or an underscore.
     */
    public static boolean isAlphaNumeric(char c) {
        return Character.isDigit(c) || Character.isAlphabetic(c) || (c == '_');
    }

    /**
     * Determines if a string is alphanumeric but not starting with a number.
     *
     * @param text the string to inspect.
     * @return true if the string is alphanumeric but not starting with a number, false otherwise.
     */
    private static boolean isAlphaNumeric(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (!isAlphaNumeric(text.charAt(i)) || (i == 0 && Character.isDigit(text.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given string is a valid number.
     *
     * @param text the string to inspect.
     * @return true if the given string is a valid number, false otherwise.
     */
    public static boolean isNumeric(String text) {
        try {
            textToBytes(text);
            return true;
        } catch (ParseException ignored) {
        }
        return false;
    }

    /**
     * Converts the given text to the applicable raw data.
     *
     * @param text the string containing a number to be converted to raw data.
     * @return the raw data representing the result of the conversion.
     * @throws ParseException if there is an error converting the given string to a number.
     */
    public static RawData textToBytes(String text) throws ParseException {
        int base = 10;
        if (isHexadecimal(text)) {
            base = 16;
            text = text.replace("0x", "");
        } else if (isBinary(text)) {
            base = 2;
            text = text.replace("0b", "");
        }

        if (text.indexOf('.') != text.lastIndexOf('.')) {
            throw new ParseException("More than one decimal point in number");
        }

        try { // Try conversion to long
            return new RawData(Long.parseLong(text, base));
        } catch (NumberFormatException ignored) {
        }

        try { // Try conversion to double
            return new RawData(stringToDouble(text, base));
        } catch (NumberFormatException ignored) {
        }

        throw new ParseException(String.format("Unable to parse immediate %s in base %d", text, base));
    }

    /**
     * Determines if the given string is of a hexadecimal format: starting with "0x" or "-0x" for positive and negative
     * numbers respectively.
     *
     * @param text the string to inspect.
     * @return true if the given string is of a hexadecimal format: starting with "0x" or "-0x", false otherwise.
     */
    private static boolean isHexadecimal(String text) {
        return text.startsWith("0x") || text.startsWith("-0x");
    }

    /**
     * Determines if the given string is of a binary format: starting with "0b" or "-0b" for positive and negative
     * numbers respectively.
     *
     * @param text the string to inspect.
     * @return true if the given string is of a binary format: starting with "0b" or "-0b", false otherwise.
     */
    private static boolean isBinary(String text) {
        return text.startsWith("0b") || text.startsWith("-0b");
    }

    /**
     * Converts the given string of the given base into a double.
     *
     * @param text the string representing the number to parse.
     * @param base the base of the number contained in the string.
     * @return the number within the string.
     * @throws NumberFormatException if the given string is not a valid number.
     */
    private static double stringToDouble(String text, int base) throws NumberFormatException {
        boolean isNegative = false;
        if (text.startsWith("-")) {
            text = text.substring(1);
            isNegative = true;
        }
        String[] halves = text.split("\\.");
        if (halves.length > 2) {
            throw new NumberFormatException();
        }
        double alloc = 0.0;
        for (int i = 0; i < halves[0].length(); ++i) {
            int exp = halves[0].length() - 1 - i;
            alloc += Integer.parseInt(Character.toString(halves[0].charAt(i)), base) * Math.pow(base, exp);
        }
        if (halves.length == 1) {
            if (isNegative) {
                alloc = -alloc;
            }
            return alloc;
        }
        for (int i = 0; i < halves[1].length(); ++i) {
            alloc += Integer.parseInt(Character.toString(halves[1].charAt(i)), base) * Math.pow(base, -(i + 1));
        }
        if (isNegative) {
            alloc = -alloc;
        }
        return alloc;
    }

    /**
     * Determines if a token is a label or not.
     *
     * @param token the token of text in question.
     * @return true if the token is a label, false otherwise.
     */
    public static boolean isLabel(String token) {
        if (token.length() < 1)
            return false;
        int colon = token.indexOf(':');
        return (colon == token.length() - 1) && isAlphaNumeric(token.substring(0, colon));
    }

    /**
     * Determines if a token is possibly a label reference or not.
     *
     * @param token the token of text in question.
     * @return true if the token is a label reference, false otherwise.
     */
    public static boolean isLabelReference(String token) {
        return isAlphaNumeric(token);
    }

    /**
     * Determines if the given token is a register or not.
     *
     * @param token the token String in question.
     * @return true if the given token is a valid register, false otherwise.
     */
    public static boolean isRegister(String token) {
        if (token.length() < 1)
            return false;
        return token.startsWith("$") && token.length() > 1 && Registers.isRegister(token.substring(1));
    }

    /**
     * Determines if the given token is a dereference expression or not.
     *
     * @param token the token String in question.
     * @return true if the given token is a valid dereference expression, false otherwise.
     */
    public static boolean isDereference(String token) {
        return token.matches("^(-?\\d+)?\\(\\$.+\\)$");
    }

    /**
     * Determines if the given token is an immediate or not.
     *
     * @param token the token String in question.
     * @return true if the given token is a valid immediate, false otherwise.
     */
    public static boolean isImmediate(String token) {
        if (token.isEmpty()) {
            return false;
        }
        return isNumeric(token);
    }

    /**
     * Determines if the given token is a character immediate or not.
     *
     * @param token the token String in question.
     * @return true if the given token is a valid character immediate, false otherwise.
     */
    public static boolean isCharacterImmediate(String token) {
        return token.length() > 1 && token.startsWith("'") && token.endsWith("'");
    }

    /**
     * Gets the character represented by the given character immediate. This also accepts the Java escape sequences.
     *
     * @param token the character immediate string.
     * @return the character read.
     * @throws ParseException if the given token is not a valid character immediate.
     */
    public static char getCharacterImmediate(String token) throws ParseException {
        if (token.length() == 3) {
            return token.charAt(1);
        } else if (token.length() < 3) {
            throw new ParseException(String.format("Improperly formatted character token %s", token));
        }
        String parsed = "";
        try {
            parsed = token.substring(1, token.length() - 1).translateEscapes();
        } catch (IllegalArgumentException e) {
            throw new ParseException(String.format("Unable to parse character token %s", token));
        }
        if (parsed.length() != 1) {
            throw new ParseException(String.format("Unable to parse character token %s", token));
        }
        return parsed.charAt(0);
    }

    /**
     * Determines if a given prototype is a valid instruction or not.
     *
     * @param token the token String in question.
     * @return true if the token is a registered instruction, false otherwise.
     */
    public static boolean isInstruction(String token) {
        return InstructionDispatcher.getInstructions().containsKey(token);
    }

    /**
     * Parses the given text as a single line. Meant for use within a simulation of the programming language.
     *
     * @param line       the line of text.
     * @param lineNumber the line number of this line.
     * @return null if the line was empty, a comment, or a label; otherwise returns the line corresponding to the text.
     * @throws ParseException if the line could not be properly parsed.
     */
    public static Line parseLine(String line, int lineNumber) throws ParseException {
        line = StringUtils.substringBefore(line, '#');

        if (line.length() == 0)
            return null;
        String[] tokens = tokenizeLine(line);
        if (tokens.length == 0) {
            return null;
        }
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return new Line(tokens[0], args);
        } catch (ParseException e) {
            throw new ParseException(String.format("Line %d: %s", lineNumber + 1, e.getMessage()));
        }
    }

    /**
     * Parses a String containing multiple lines.
     *
     * @param lines the text containing the lines to parse.
     * @return the list of valid lines of code found.
     * @throws ParseException if any line could not be properly parsed.
     */
    public static List<Line> parseLines(String lines) throws ParseException {
        List<String> linesRead = new ArrayList<>();
        List<Line> linesLexed = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        // individually read lines treating semicolons as line breaks
        for (int i = 0; i < lines.length(); ++i) {
            char c = lines.charAt(i);
            if (c == '\n') {
                linesRead.add(sb.toString());
                sb.delete(0, sb.length());
            } else {
                sb.append(c);
            }
        }

        // put any remaining characters into their own line
        if (!sb.isEmpty()) {
            linesRead.add(sb.toString());
        }

        for (int i = 0; i < linesRead.size(); ++i) {
            Line lexed = parseLine(linesRead.get(i), i);
            if (lexed != null) {
                linesLexed.add(lexed);
            }
        }
        return linesLexed;
    }

    /**
     * Checks if a given string is a valid line of EzASM code.
     *
     * @param line a string with exactly 1 \n at the end.
     * @return true if a valid line of EzASM code, false otherwise.
     */
    public static boolean validProgramLine(String line) {
        String[] tokens = tokenizeLine(line);
        return tokens.length > 0;
    }

    /**
     * Turns a line of EzASM into an array of strings of its respective tokens.
     *
     * @param line the line to tokenize.
     * @return the tokens found.
     */
    public static String[] tokenizeLine(String line) {
        line = StringUtils.substringBefore(line, '#');
        line = line.trim();

        if (line.length() == 0) {
            return new String[0];
        }

        List<String> tokens = new ArrayList<>();

        StringBuilder currentToken = new StringBuilder();
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\'' && !inDoubleQuotes) {
                inSingleQuotes = !inSingleQuotes;
                currentToken.append(c);
            } else if (c == '\"' && !inSingleQuotes) {
                inDoubleQuotes = !inDoubleQuotes;
                currentToken.append(c);
            } else if (inSingleQuotes || inDoubleQuotes || !(Character.isWhitespace(c) || c == ',')) {
                currentToken.append(c);
            } else if (currentToken.length() > 0) {
                tokens.add(currentToken.toString());
                currentToken = new StringBuilder();
            }
        }

        // Add remaining characters to the array
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens.toArray(new String[0]);
    }

}
