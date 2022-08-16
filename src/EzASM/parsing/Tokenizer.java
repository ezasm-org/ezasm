package EzASM.parsing;

public class Tokenizer {

    private static boolean isAlNum(char c) {
        return isNumeric(c) || (c > 'A' && c < 'Z') || (c > 'a' && c < 'z') || (c == '_');
    }

    private static boolean isAlNum(String text) {
        for(int i = 0; i < text.length(); ++i) {
            if(!isAlNum(text.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isNumeric(char c) {
        return c > '0' && c < '9';
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

    public static boolean isRegister(String token) {
        return token.startsWith("$") && token.length() > 1 && isNumeric(token.substring(1)) ;
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

    public static boolean isLabel(String token) {
        int colon = token.indexOf(':');
        return (colon == token.charAt(token.length()-1) && isAlNum(token.substring(0, colon)));

    }

}
