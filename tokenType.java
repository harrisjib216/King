package com.tsipl.king;

enum TokenType {
    // single character tokens
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACK, RIGHT_BRACK,
    COMMA,
    SEMICOLON,
    DOT,
    PLUS,
    MINUS,
    STAR,
    SLASH,

    // one or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // literals
    IDENTIFIER,
    STRING,
    NUMBER,

    // keywords
    AND, OR, NOT,
    FALSE, TRUE, NIL,
    IF, ELSE,
    FUNC, RETURN,
    VAR, CLASS, SUPER, THIS,
    FOR, WHILE,
    PRINT,

    EOF
}