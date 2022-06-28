package com.tsipl.king;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tsipl.king.King;

import static com.tsipl.king.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    // initializes our scanner with the source file/code
    Scanner(String source) {
        this.source = source;
    }

    // scans all of the "tokens" 1 by 1
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, lineNumber));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            // single characters
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case '[':
                addToken(LEFT_BRACK);
                break;
            case ']':
                addToken(RIGHT_BRACK);
                break;
            case ',':
                addToken(COMMA);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '.':
                addToken(DOT);
                break;
            case '+':
                addToken(PLUS);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '*':
                addToken(STAR);
                break;

            // ignore white space
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            // double characters
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : EQUAL);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;

            // literals
            case '"':
                string();
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else {
                    King.error(line, "Unexpected character.");
                }
                break;
        }
    }

    // consume the current character, advance if double token
    private boolean match(char expected) {
        if (isAtEnd || source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    // return character add current index, lookahead?
    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }

        return source.charAt(current);
    }

    // get next character
    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }

        return source.charAt(current + 1);
    }

    // returns if we've finished scanning/parsing the file
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // input: gets next character in source file
    private char advance() {
        return source.charAt(current++);
    }

    // output: add and parse tokens
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // get text of current lexeme and create token for it
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, currrent);
        tokens.add(new Token(type, text, literal, line));
    }

    // LITERALS
    // strings
    private void string() {
        // middle of string
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        // string didn't finish, at end of file
        if (isAtEnd()) {
            King.error(line, "Unable to parse string.");
            return;
        }

        // find end of string, add it as a value
        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    // numbers
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) {
                advance();
            }
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }
}