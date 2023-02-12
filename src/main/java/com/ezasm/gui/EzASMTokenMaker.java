package com.ezasm.gui;

import com.ezasm.instructions.InstructionDispatcher;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.text.Segment;

public class EzASMTokenMaker extends AbstractTokenMaker {

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

        for (int i = offset; i < end; ++i) {

            char c = array[i];

            switch (initialTokenType) {

                case Token.NULL:

                    currentToken.setOffset(i);   // Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            initialTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case '#':
                            initialTokenType = Token.COMMENT_EOL;
                            break;

                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                initialTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            }
                            else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
                                initialTokenType = Token.IDENTIFIER;
                                break;
                            }

                            // Anything not currently handled - mark as an identifier
                            initialTokenType = Token.IDENTIFIER;
                            break;

                    } // End of switch (c).

                    break;

                case Token.WHITESPACE:

                    switch (c) {

                        case ' ':
                        case '\t':
                            break;   // Still whitespace.

                        case '"':
                            addToken(text, currentToken.getOffset(),i-1, Token.WHITESPACE, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case '#':
                            addToken(text, currentToken.getOffset(),i-1, Token.WHITESPACE, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.COMMENT_EOL;
                            break;

                        default:   // Add the whitespace token and start anew.

                            addToken(text, currentToken.getOffset(),i-1, Token.WHITESPACE, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);

                            if (RSyntaxUtilities.isDigit(c)) {
                                initialTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            }
                            else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
                                initialTokenType = Token.IDENTIFIER;
                                break;
                            }

                            // Anything not currently handled - mark as identifier
                            initialTokenType = Token.IDENTIFIER;

                    } // End of switch (c).

                    break;

                default: // Should never happen
                case Token.IDENTIFIER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentToken.getOffset(),i-1, Token.IDENTIFIER, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            addToken(text, currentToken.getOffset(),i-1, Token.IDENTIFIER, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        default:
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c=='/' || c=='_') {
                                break;   // Still an identifier of some type.
                            }
                            // Otherwise, we're still an identifier (?).

                    } // End of switch (c).

                    break;

                case Token.LITERAL_NUMBER_DECIMAL_INT:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentToken.getOffset(),i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            addToken(text, currentToken.getOffset(),i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentToken.getOffset());
                            currentToken.setOffset(i);
                            initialTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        default:

                            if (RSyntaxUtilities.isDigit(c)) {
                                break;   // Still a literal number.
                            }

                            // Otherwise, remember this was a number and start over.
                            addToken(text, currentToken.getOffset(),i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentToken.getOffset());
                            i--;
                            initialTokenType = Token.NULL;

                    } // End of switch (c).

                    break;

                case Token.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentToken.getOffset(),i, initialTokenType, newStartOffset+currentToken.getOffset());
                    // We need to set token type to null so at the bottom we don't add one more token.
                    initialTokenType = Token.NULL;
                    break;

                case Token.LITERAL_STRING_DOUBLE_QUOTE:
                    if (c=='"') {
                        addToken(text, currentToken.getOffset(),i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+currentToken.getOffset());
                        initialTokenType = Token.NULL;
                    }
                    break;

            } // End of switch (initialTokenType).

        } // End of for (int i=offset; i<end; i++).

        switch (initialTokenType) {

            // Remember what token type to begin the next line with.
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentToken.getOffset(),end-1, initialTokenType, newStartOffset+currentToken.getOffset());
                break;

            // Do nothing if everything was okay.
            case Token.NULL:
                addNullToken();
                break;

            // All other token types don't continue to the next line...
            default:
                addToken(text, currentToken.getOffset(),end-1, initialTokenType, newStartOffset+currentToken.getOffset());
                addNullToken();

        }

        // Return the first token in our linked list.
        return firstToken;

    }
}
