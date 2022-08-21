package EzASM.parsing;

import EzASM.simulation.Registers;
import EzASM.instructions.InstructionDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Static context functions regarding lexing and tokenizing Strings.
 */
public class Lexer {

    private static boolean isAlNum(char c) {
        return isNumeric(c) || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '_');
    }

    private static boolean isAlNum(String text) {
        for(int i = 0; i < text.length(); ++i) {
            if(!isAlNum(text.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isNumeric(String text) {
        if(text.startsWith("-")) {
            text = text.substring(1);
        }
        for(int i = 0; i < text.length(); ++i) {
            if(!isNumeric(text.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Determines if the line is a comment or not
     * @param line the line of text in question.
     * @return true if the line is a comment, false otherwise.
     */
    public static boolean isComment(String line) {
        if(line.length() < 1) return false;
        return line.startsWith("#");
    }

    /**
     * Determines if a line is a label or not.
     * @param line the line of text in question.
     * @return true if the line is a label, false otherwise;
     */
    public static boolean isLabel(String line) {
        if(line.length() < 1) return false;
        int colon = line.indexOf(':');
        return (colon == line.charAt(line.length()-1) && isAlNum(line.substring(0, colon)));
    }

    /**
     * Determines if the given token is a register or not.
     * @param token the token String in question.
     * @return true if the given token is a valid register, false otherwise.
     */
    public static boolean isRegister(String token) {
        if(token.length() < 1) return false;
        return token.startsWith("$") && token.length() > 1 && Registers.isRegister(token.substring(1)) ;
    }

    /**
     * Determines if the given token is a dereference expression or not.
     * @param token the token String in question.
     * @return true if the given token is a valid dereference expression, false otherwise.
     */
    public static boolean isDereference(String token) {
        if(token.length() < 5) return false;
        int first = token.indexOf('(');
        int last = token.indexOf(')');
        return (first != -1) && (last != -1) && (first == token.lastIndexOf('(')) && (last == token.lastIndexOf(')'))
                && (isAlNum(token.substring(0, first))) && isRegister(token.substring(first + 1, last));
    }

    /**
     * Determines if the given token is an immediate or not.
     * @param token the token String in question.
     * @return true if the given token is a valid immediate, false otherwise.
     */
    public static boolean isImmediate(String token) {
        if(token.length() < 1) return false;
        if(!isNumeric(token)) return false;
        try {
            Long.parseLong(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Determines if a given token is a valid instruction or not.
     * @param token the token String in question.
     * @return true if the token is a registered instruction, false otherwise.
     */
    public static boolean isInstruction(String token) {
        return InstructionDispatcher.getInstructions().containsKey(token);
    }

    /**
     * Parses the given text as a single line. Meant for use within a simulation of the programming language.
     * @param line the line of text.
     * @param labels the mapping of label text to line numbers.
     * @param number the line number of this line.
     * @return null if the line was empty, a comment, or a label; otherwise returns the line corresponding to the text.
     * @throws ParseException if the line could not be properly parsed.
     */
    public static Line parseLine(String line, Map<String, Integer> labels, int number) throws ParseException {
        line = line.replaceAll("[\s,;]+", " ").trim();
        if(Lexer.isComment(line)) return null;
        if(Lexer.isLabel(line)) {
            labels.putIfAbsent(line, number);
            return null;
        }
        String[] tokens = line.split("[ ,]");
        if(tokens.length == 0) {
            // Empty line
            return null;
        } else if(tokens.length < 2) {
            // ERROR too few tokens to be a line
            throw new ParseException(String.format(
                    "Too few tokens found on line %d: '%s' is likely an incomplete statement", number, line));
        }

        String[] args = Arrays.copyOfRange(tokens, 2, tokens.length);
        return new Line(tokens[0], tokens[1], args);
    }

    /**
     * Parses a String containing multiple lines. Meant for use within a simulation of the programming language.
     * @param lines the text containing the lines to parse.
     * @param labels the mapping of label text to line numbers.
     * @return the list of valid lines of code found.
     * @throws ParseException if any line could not be properly parsed.
     */
    public static List<Line> parseLines(String lines, Map<String, Integer> labels) throws ParseException {
        List<String> linesRead = new ArrayList<>();
        List<Line> linesLexed = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        lines = lines + "\n";

        // individually read lines treating semicolons as line breaks
        for(int i = 0; i < lines.length(); ++i) {
            char c = lines.charAt(i);
            if (c == '\n' || c == ';') {
                if(sb.length() > 0) {
                    linesRead.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else {
                sb.append(c);
            }
        }

        for(int i = 0; i < linesRead.size(); ++i) {
            Line lexed = parseLine(linesRead.get(i), labels, i);
            if(lexed != null) {
                linesLexed.add(parseLine(linesRead.get(i), labels, i));
            }
        }
        return linesLexed;
    }

}
