package com.ezasm.parsing;

import com.ezasm.Conversion;
import com.ezasm.simulation.Registers;
import com.ezasm.instructions.InstructionDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static context functions regarding lexing and tokenizing Strings.
 */
public class Lexer {

    private static boolean isAlNum(char c) {
        return isNumeric(c) || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '_') || (c == '-');
    }

    private static boolean isAlNum(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (!isAlNum(text.charAt(i)) || (i == 0 && isNumeric(text.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isNumeric(String text) {
        try {
            textToBytes(text);
            return true;
        } catch (ParseException ignored) {
        }
        return false;
    }

    public static byte[] textToBytes(String text) throws ParseException {
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
            return Conversion.longToBytes(Long.parseLong(text, base));
        } catch (NumberFormatException ignored) {
        }

        try { // Try conversion to double
            return Conversion.doubleToBytes(stringToDouble(text, base));
        } catch (NumberFormatException ignored) {
        }

        throw new ParseException(String.format("Unable to parse immediate %s in base %d", text, base));
    }

    private static boolean isHexadecimal(String text) {
        return text.startsWith("0x") || text.startsWith("-0x");
    }

    private static boolean isBinary(String text) {
        return text.startsWith("0b") || text.startsWith("-0b");
    }

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
     * Determines if the line is a comment or not
     *
     * @param line the line of text in question.
     * @return true if the line is a comment, false otherwise.
     */
    public static boolean isComment(String line) {
        if (line.length() < 1)
            return false;
        return line.startsWith("#");
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
        return (colon == token.length() - 1) && isAlNum(token.substring(0, colon));
    }

    /**
     * Determines if a token is possibly a label reference or not.
     *
     * @param token the token of text in question.
     * @return true if the token is a label reference, false otherwise.
     */
    public static boolean isLabelReference(String token) {
        return isAlNum(token);
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

    public static boolean isCharacterImmediate(String token) {
        return token.startsWith("'") && token.endsWith("'");
    }

    public static char getCharacterImmediate(String token) throws ParseException {
        if (token.length() == 3) {
            return token.charAt(1);
        } else if (token.length() < 3) {
            throw new ParseException(String.format("Improperly formatted character token %s", token));
        }
        String parsed = token.substring(1, token.length() - 1).translateEscapes();
        if (parsed.length() != 1) {
            throw new ParseException(String.format("Unable to parse character token %s", token));
        }
        return parsed.charAt(0);
    }

    /**
     * Determines if a given token is a valid instruction or not.
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
        line = line.replaceAll("[\s\t,;]+", " ").trim();
        if (line.length() == 0)
            return null;
        if (Lexer.isComment(line))
            return null;
        String[] tokens = line.split("[ ,]");
        if (tokens.length == 0) {
            // Empty line
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
     * Parses the given text as a single line. Meant for use within a simulation of the programming language.
     *
     * @param line       the line of text.
     * @return null if the line was empty, a comment, or a label; otherwise returns the line corresponding to the text.
     * @throws ParseException if the line could not be properly parsed.
     */
    public static boolean isValidProgramLine(String line) {
        line = line.replaceAll("[\s\t,;]+", " ").trim();
        if (line.length() == 0)
            return false;
        if (Lexer.isComment(line))
            return false;
        String[] tokens = line.split("[ ,]");
        if (tokens.length == 0) {
            // Empty line
            return false;
        }
        return true;
    }

    /**
     * Parses a String containing multiple lines.
     *
     * @param lines the text containing the lines to parse.
     * @return the list of valid lines of code found.
     * @throws ParseException if any line could not be properly parsed.
     */
    public static List<Line> parseLines(String lines, int startingLine) throws ParseException {
        List<String> linesRead = new ArrayList<>();
        List<Line> linesLexed = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        lines = lines + "\n";

        // individually read lines treating semicolons as line breaks
        for (int i = 0; i < lines.length(); ++i) {
            char c = lines.charAt(i);
            if (c == '\n' || c == ';') {
                if (sb.length() > 0) {
                    linesRead.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else {
                sb.append(c);
            }
        }

        for (String s : linesRead) {
            Line lexed = parseLine(s, linesLexed.size() + startingLine);
            if (lexed != null) {
                linesLexed.add(lexed);
            }
        }
        return linesLexed;
    }

    /**
     * Checks if a given string is a valid line of ezasm code
     *
     * @param line a string with exactly 1 \n at the end
     * @return true if a valid line of ezasm code, false if not
     */
    public static boolean validProgramLine(String line) {
        line = line.replaceAll("[\s,;]+", " ").trim();
        if (line.length() == 0)
            return false;
        if (Lexer.isComment(line))
            return false;
        if (Lexer.isLabel(line))
            return false;
        String[] tokens = line.split("[ ,]");

        return tokens.length >= 2;// Not enough tokens
    }

}
