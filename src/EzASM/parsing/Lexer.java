package EzASM.parsing;

import EzASM.Registers;
import EzASM.instructions.InstructionDispatcher;

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
        return line.startsWith("#");
    }

    public static boolean isLabel(String line) {
        int colon = line.indexOf(':');
        return (colon == line.charAt(line.length()-1) && isAlNum(line.substring(0, colon)));
    }

    public static boolean isRegister(String token) {
        return token.startsWith("$") && token.length() > 1 && Registers.isRegister(token.substring(1)) ;
    }

    public static boolean isDereference(String token) {
        int first = token.indexOf('(');
        int last = token.indexOf(')');
        return (first != -1) && (last != -1) && (first == token.lastIndexOf('(')) && (last == token.lastIndexOf(')'))
                && (isAlNum(token.substring(0, first))) && isRegister(token.substring(first + 1, last));
    }

    public static boolean isImmediate(String token) {
        if(!isNumeric(token)) return false;
        try {
            Long.parseLong(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInstruction(String token) {
        System.out.println(InstructionDispatcher.getInstructions().keySet());
        return InstructionDispatcher.getInstructions().containsKey(token);
    }


}
