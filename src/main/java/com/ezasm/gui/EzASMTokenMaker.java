package com.ezasm.gui;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.Registers;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.text.Segment;

/**
 * Custom EzASM language syntax highlighter support.
 */
public class EzASMTokenMaker extends AbstractTokenMaker {

    private final int REGISTER_VARIABLE = Token.VARIABLE;

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
        // 'startOffset + (currentToken.getOffset()-offset)', but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentToken.getOffset()'.
        int newStartOffset = startOffset - offset;


        currentToken = new TokenImpl();
        currentToken.setOffset(offset);
        currentToken.setType(initialTokenType);
        boolean expectIntegerTypeCharacter = false;
        boolean expectHexadecimal = false;
        boolean expectBinary = false;

        for (int i = offset; i < end; ++i) {

            char c = array[i];

            switch (initialTokenType) {
                case Token.NULL -> {
                    currentToken.setOffset(i);   // Starting a new token here.
                    switch (c) {
                        case ' ', '\t' -> initialTokenType = Token.WHITESPACE;
                        case '"' -> initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                        case '\'' -> initialTokenType = Token.LITERAL_CHAR;
                        case '#' -> initialTokenType = Token.COMMENT_EOL;
                        case '$' -> initialTokenType = REGISTER_VARIABLE;
                        default -> {
                            if (RSyntaxUtilities.isDigit(c)) {
                                initialTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                            } else if (Lexer.isAlNum(c)) {
                                initialTokenType = Token.IDENTIFIER;
                            } else {
                                // Anything not currently handled - mark as an identifier
                                initialTokenType = Token.IDENTIFIER;
                            }
                        }
                    } // End of switch (c).
                }
                case Token.WHITESPACE -> {
                    switch (c) {

                        case ' ':
                        case '\t':
                            break;   // Still whitespace.

                        case '"':
                            addToken(text, currentToken.getOffset(), i - 1, Token.WHITESPACE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case '\'':
                            addToken(text, currentToken.getOffset(), i - 1, Token.WHITESPACE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_CHAR;
                            break;

                        case '#':
                            addToken(text, currentToken.getOffset(), i - 1, Token.WHITESPACE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.COMMENT_EOL;
                            break;

                        case '$':
                            addToken(text, currentToken.getOffset(), i - 1, Token.WHITESPACE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = REGISTER_VARIABLE;
                            break;

                        default:   // Add the whitespace token and start anew.

                            addToken(text, currentToken.getOffset(), i - 1, Token.WHITESPACE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);

                            if (RSyntaxUtilities.isDigit(c)) {
                                initialTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            } else if (Lexer.isAlNum(c)) {
                                initialTokenType = Token.IDENTIFIER;
                                break;
                            } else {
                                // Anything not currently handled - mark as identifier
                                initialTokenType = Token.IDENTIFIER;
                            }

                    } // End of switch (c).
                }
                case Token.IDENTIFIER -> {
                    switch (c) {
                        case ' ', '\t' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.IDENTIFIER, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.WHITESPACE;
                        }
                        case '"' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.IDENTIFIER, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                        }
                        case '\'' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.IDENTIFIER, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_CHAR;
                        }
                        case '$' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.IDENTIFIER, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = REGISTER_VARIABLE;
                        }
                        // Otherwise, we're still an identifier (?).

                    } // End of switch (c).
                }
                case Token.LITERAL_NUMBER_DECIMAL_INT -> {
                    switch (c) {
                        case ' ', '\t' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.WHITESPACE;
                        }
                        case '"' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                        }
                        case '\'' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_CHAR;
                        }
                        case '$' -> {
                            addToken(text, currentToken.getOffset(), i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = REGISTER_VARIABLE;
                        }
                        default -> {
                            if (!(RSyntaxUtilities.isHexCharacter(c) || c == 'x' || c == 'b')) {
                                // Not a literal number; remember this was a number and start over.
                                addToken(text, currentToken.getOffset(), i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentToken.getOffset());
                                i--;
                                initialTokenType = Token.NULL;
                            }
                        }
                    } // End of switch (c).
                }
                case Token.COMMENT_EOL -> {
                    i = end - 1;
                    addToken(text, currentToken.getOffset(), i, initialTokenType, newStartOffset + currentToken.getOffset());
                    // We need to set token type to null so at the bottom we don't add one more token.
                    initialTokenType = Token.NULL;
                }
                case Token.LITERAL_STRING_DOUBLE_QUOTE -> {
                    if (c == '"') {
                        addToken(text, currentToken.getOffset(), i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset + currentToken.getOffset());
                        initialTokenType = Token.NULL;
                    }
                }
                case Token.LITERAL_CHAR -> {
                    if (c == '\'') {
                        addToken(text, currentToken.getOffset(), i, Token.LITERAL_CHAR, newStartOffset + currentToken.getOffset());
                        initialTokenType = Token.NULL;
                    }
                }
                case REGISTER_VARIABLE -> {
                    switch (c) {
                        case ' ', '\t' -> {
                            addToken(text, currentToken.getOffset(), i - 1, REGISTER_VARIABLE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.WHITESPACE;
                        }
                        case '"' -> {
                            addToken(text, currentToken.getOffset(), i - 1, REGISTER_VARIABLE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                        }
                        case '\'' -> {
                            addToken(text, currentToken.getOffset(), i - 1, REGISTER_VARIABLE, newStartOffset + currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_CHAR;
                        }
                        default -> {

                        }
                    }
                }
            } // End of switch (initialTokenType).

        }

        if (initialTokenType != Token.NULL) {
            addToken(text, currentToken.getOffset(),end-1, initialTokenType, newStartOffset+currentToken.getOffset());
        }
        addNullToken();

        // Return the first token in our linked list.
        return firstToken;

    }
}
