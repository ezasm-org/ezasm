package EzASM.parsing;

import EzASM.Registers;
import EzASM.instructions.InstructionDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public static boolean isComment(String line) {
        if(line.length() < 1) return false;
        return line.startsWith("#");
    }

    public static boolean isLabel(String line) {
        if(line.length() < 1) return false;
        int colon = line.indexOf(':');
        return (colon == line.charAt(line.length()-1) && isAlNum(line.substring(0, colon)));
    }

    public static boolean isRegister(String token) {
        if(token.length() < 1) return false;
        return token.startsWith("$") && token.length() > 1 && Registers.isRegister(token.substring(1)) ;
    }

    public static boolean isDereference(String token) {
        if(token.length() < 5) return false;
        int first = token.indexOf('(');
        int last = token.indexOf(')');
        return (first != -1) && (last != -1) && (first == token.lastIndexOf('(')) && (last == token.lastIndexOf(')'))
                && (isAlNum(token.substring(0, first))) && isRegister(token.substring(first + 1, last));
    }

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
     * Determines if a given token is a valid instruction.
     * @param token the token to analyze.
     * @return true if the token is a registered instruction, false otherwise.
     */
    public static boolean isInstruction(String token) {
        return InstructionDispatcher.getInstructions().containsKey(token);
    }

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
            throw new ParseException(String.format("Too few tokens found: '%s' is likely an incomplete statement", line));
        }

        String[] args = Arrays.copyOfRange(tokens, 2, tokens.length);
        return new Line(tokens[0], tokens[1], args);
    }

    public static List<Line> parseLines(String lines, Map<String, Integer> labels) throws ParseException {
        List<String> linesRead = new ArrayList<>();
        List<Line> linesLexed = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        lines = lines + "\n";

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
            linesLexed.add(parseLine(linesRead.get(i), labels, i));
        }
        return linesLexed;
    }

}
