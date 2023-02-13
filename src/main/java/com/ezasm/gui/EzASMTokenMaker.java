package com.ezasm.gui;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.parsing.Lexer;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.text.Segment;

/**
 * Custom EzASM language syntax highlighter support.
 */
public class EzASMTokenMaker extends AbstractTokenMaker {

    private final int REGISTER_VARIABLE = Token.VARIABLE;
    private int currentTokenStart;
    private int currentTokenType;
    private boolean expectIntegerTypeCharacter = false;
    private boolean expectHexadecimal = false;
    private boolean expectBinary = false;
    private boolean hasDecimalPoint = false;

    public EzASMTokenMaker() {
        super();
    }

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();

        for (String instruction : InstructionDispatcher.getInstructions().keySet()) {
            tokenMap.put(instruction, Token.RESERVED_WORD);
        }

        return tokenMap;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        // This assumes all keywords, etc. were parsed as "identifiers."
        if (tokenType == Token.IDENTIFIER) {
            int value = wordsToHighlight.get(segment, start, end);
            if (value != -1) {
                tokenType = value;
            }
        }
        super.addToken(segment, start, end, tokenType, startOffset);
    }

    /**
     * Returns whether tokens of the specified type should have "mark occurrences" enabled for the current programming
     * language.
     *
     * @param type The token type.
     * @return Whether tokens of this type should have "mark occurrences" enabled.
     */
    @Override
    public boolean getMarkOccurrencesOfTokenType(int type) {
        return type == Token.IDENTIFIER;
    }

    @Override
    public String[] getLineCommentStartAndEnd(int languageIndex) {
        return new String[] { "#", null };
    }

    @Override
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        // Token starting offsets are always of the form:
        // 'startOffset + (currentTokenStart-offset)', but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart'.
        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType = initialTokenType;
        expectIntegerTypeCharacter = false;
        expectHexadecimal = false;
        expectBinary = false;
        hasDecimalPoint = false;

        for (int i = offset; i < end; ++i) {

            char c = array[i];

            switch (currentTokenType) {
            case Token.NULL -> {
                currentTokenStart = i; // Starting a new token here.
                switch (c) {
                case ' ', '\t', ';', ',' -> currentTokenType = Token.WHITESPACE;
                case '"' -> currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                case '\'' -> currentTokenType = Token.LITERAL_CHAR;
                case '#' -> currentTokenType = Token.COMMENT_EOL;
                case '$' -> currentTokenType = REGISTER_VARIABLE;
                case '(', ')' -> {
                    addToken(text, currentTokenStart, i, Token.SEPARATOR, newStartOffset + currentTokenStart);
                }
                default -> {
                    if (RSyntaxUtilities.isDigit(c)) {
                        expectIntegerTypeCharacter = c == '0';
                        currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                    } else if (Lexer.isAlNum(c)) {
                        currentTokenType = Token.IDENTIFIER;
                    } else {
                        // Anything not currently handled - mark as an identifier
                        currentTokenType = Token.ERROR_IDENTIFIER;
                    }
                }
                } // End of switch (c).
            }
            case Token.WHITESPACE -> {
                switch (c) {
                case ' ', '\t', ';', ',' -> {
                    // Still whitespace.
                }
                case '"' -> {
                    addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                }
                case '\'' -> {
                    addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.LITERAL_CHAR;
                }
                case '#' -> {
                    addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.COMMENT_EOL;
                }
                case '$' -> {
                    addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = REGISTER_VARIABLE;
                }
                case '(', ')' -> {
                    addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                    addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                    currentTokenType = Token.NULL;
                }
                default -> {
                    // Add the whitespace token and start anew.
                    addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                    currentTokenStart = i;

                    // Handle integer inputs

                    if (!handleOtherInteger(c)) {
                        if (RSyntaxUtilities.isDigit(c)) {
                            expectIntegerTypeCharacter = c == '0';
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                        } else if (Lexer.isAlNum(c)) {
                            currentTokenType = Token.IDENTIFIER;
                        } else {
                            // Anything not currently handled - mark as an identifier
                            currentTokenType = Token.ERROR_IDENTIFIER;
                        }
                    }
                }
                } // End of switch (c).
            }
            case Token.IDENTIFIER -> {
                switch (c) {
                case ' ', '\t', ';', ',' -> {
                    addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.WHITESPACE;
                }
                case '(', ')' -> {
                    addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                    addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                    currentTokenType = Token.NULL;
                }
                case '#' -> {
                    addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.COMMENT_EOL;
                }
                default -> {
                    if (!(Lexer.isAlNum(c) || c == ':')) {
                        // Not an identifier; remember this was an identifier error and start over.
                        addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
                                newStartOffset + currentTokenStart);
                        i--;
                        currentTokenType = Token.NULL;
                    }
                }
                } // End of switch (c).
            }
            case Token.LITERAL_NUMBER_DECIMAL_INT -> {
                switch (c) {
                case ' ', '\t', ';', ',' -> {
                    expectIntegerTypeCharacter = expectHexadecimal = expectBinary = hasDecimalPoint = false;
                    addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.WHITESPACE;
                }
                case '(', ')' -> {
                    expectIntegerTypeCharacter = expectHexadecimal = expectBinary = hasDecimalPoint = false;
                    addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                            newStartOffset + currentTokenStart);
                    addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                    currentTokenType = Token.NULL;
                }
                case '#' -> {
                    expectIntegerTypeCharacter = expectHexadecimal = expectBinary = hasDecimalPoint = false;
                    addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.COMMENT_EOL;
                }
                default -> {
                    if (!handleOtherInteger(c)) {
                        if (RSyntaxUtilities.isDigit(c)) {
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                        } else if (array[i - 1] == '.' && !hasDecimalPoint) {
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                            hasDecimalPoint = true;
                        } else {
                            // Anything not currently handled - mark as an identifier
                            currentTokenType = Token.ERROR_NUMBER_FORMAT;
                        }
                    }

                    if (currentTokenType != Token.LITERAL_NUMBER_DECIMAL_INT) {
                        // Not a literal number; remember this was a number error and start over.
                        addToken(text, currentTokenStart, i - 1, Token.ERROR_NUMBER_FORMAT,
                                newStartOffset + currentTokenStart);
                        i--;
                        currentTokenType = Token.NULL;
                    }
                }
                } // End of switch (c).
            }
            case Token.COMMENT_EOL -> {
                i = end - 1;
                addToken(text, currentTokenStart, i, Token.COMMENT_EOL, newStartOffset + currentTokenStart);
                // We need to set token type to null so at the bottom we don't add one more token.
                currentTokenType = Token.NULL;
            }
            case Token.SEPARATOR -> {
                addToken(text, currentTokenStart, i - 1, Token.SEPARATOR, newStartOffset + currentTokenStart);
                currentTokenType = Token.NULL;
            }
            case Token.LITERAL_STRING_DOUBLE_QUOTE -> {
                if (c == '"') {
                    addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE,
                            newStartOffset + currentTokenStart);
                    currentTokenType = Token.NULL;
                }
            }
            case Token.LITERAL_CHAR -> {
                if (c == '\'') {
                    addToken(text, currentTokenStart, i, Token.LITERAL_CHAR, newStartOffset + currentTokenStart);
                    currentTokenType = Token.NULL;
                }
            }
            case REGISTER_VARIABLE -> {
                switch (c) {
                case ' ', '\t', ';', ',' -> {
                    addToken(text, currentTokenStart, i - 1, REGISTER_VARIABLE, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.WHITESPACE;
                }
                case '(', ')' -> {
                    addToken(text, currentTokenStart, i - 1, REGISTER_VARIABLE, newStartOffset + currentTokenStart);
                    addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                    currentTokenType = Token.NULL;
                }
                case '#' -> {
                    addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.COMMENT_EOL;
                }
                default -> {
                    if (!Lexer.isAlNum(c)) {
                        // Not a valid register; remember this was an error and start over.
                        addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
                                newStartOffset + currentTokenStart);
                        i--;
                        currentTokenType = Token.NULL;
                    }
                }
                }
            }
            case Token.ERROR_IDENTIFIER -> {
                switch (c) {
                case ' ', '\t', ';', ',' -> {
                    addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.WHITESPACE;
                }
                case '(', ')' -> {
                    addToken(text, currentTokenStart, i - 1, REGISTER_VARIABLE, newStartOffset + currentTokenStart);
                    addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                    currentTokenType = Token.NULL;
                }
                case '#' -> {
                    addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
                            newStartOffset + currentTokenStart);
                    currentTokenStart = i;
                    currentTokenType = Token.COMMENT_EOL;
                }
                default -> {
                    if (RSyntaxUtilities.isDigit(c)) {
                        if (array[i - 1] == '-') {
                            expectIntegerTypeCharacter = c == '0';
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                        } else if (array[i - 1] == '.') {
                            hasDecimalPoint = true;
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                        }
                    }
                }
                }
            }
            } // End of switch (currentTokenType).
        }

        if (currentTokenType != Token.NULL) {
            addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
        }
        addNullToken();

        // Return the first token in our linked list.
        return firstToken;

    }

    private boolean handleOtherInteger(char c) {
        if (c == '.') {
            if (!hasDecimalPoint) {
                hasDecimalPoint = true;
            } else {
                currentTokenType = Token.ERROR_NUMBER_FORMAT;
            }
            return true;
        }
        if (expectIntegerTypeCharacter) {
            expectIntegerTypeCharacter = false;
            if (c == 'x' || c == 'X') {
                expectHexadecimal = true;
                return true;
            } else if (c == 'b' || c == 'B') {
                expectBinary = true;
                return true;
            }
        } else if (expectHexadecimal) {
            if (RSyntaxUtilities.isHexCharacter(c)) {
                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
            } else {
                currentTokenType = Token.ERROR_NUMBER_FORMAT;
                expectHexadecimal = hasDecimalPoint = false;
            }
            return true;
        } else if (expectBinary) {
            if (c == '0' || c == '1') {
                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
            } else {
                currentTokenType = Token.ERROR_NUMBER_FORMAT;
                expectBinary = hasDecimalPoint = false;
            }
            return true;
        }
        return false;
    }
}
