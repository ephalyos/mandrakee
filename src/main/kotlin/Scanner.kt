
class Scanner () {

    val reserved = hashMapOf<String, TokenType>(
        "if" to TokenType.IF,
        "else" to TokenType.ELSE,
        "var" to TokenType.VAR,
        "val" to TokenType.VAL,
        "while" to TokenType.WHILE,
        "for" to TokenType.FOR,
        "true" to TokenType.TRUE,
        "false" to TokenType.FALSE,
        "this" to TokenType.THIS,
        "fun" to TokenType.FUN,
        "return" to TokenType.RETURN,
        "class" to TokenType.CLASS,
        "super" to TokenType.SUPER,
        "and" to TokenType.AND,
        "or" to TokenType.OR,
        "nil" to TokenType.NIL,
        "print" to TokenType.PRINT,
    )

}

fun Scanner.scan (lines: List<String>): List<Token> {

    val tokens = mutableListOf<Token>()

    val digits = '0'..'9'
    val lowercase = 'a'..'z'
    val uppercase = 'A'..'Z'

    var state = 0
    var lexeme = ""

    for ((linenum, line) in lines.withIndex()) {
        var i = 0
        while ( i < line.length ) {
            val ch = line[i]
            when (state) {
                0 -> {
                    when (ch) {
                        ' ', '\n' -> {
                            state = 0
                            i += 1
                        }
                        else -> {
                            state = 1
                        }
                    }
                }
                1 -> {
                    state = when (ch) {
                        '<' -> 3
                        '>' -> 4
                        '+' -> 5
                        '-' -> 6
                        '*' -> 7
                        '/' -> 8
                        '%' -> 9
                        '=' -> 10
                        '!' -> 11
                        '"' -> 12
                        in digits -> 16
                        in lowercase, in uppercase -> 22
                        else -> 2
                    }
                    lexeme += ch
                    if (state != 2)
                        i += 1
                }
                2 -> {
                    when (ch) {
                        '(' -> tokens.add(Token(null, linenum, lexeme, TokenType.LEFT_PARENTHESES))
                        ')' -> tokens.add(Token(null, linenum, lexeme, TokenType.RIGHT_PARENTHESES))
                        '{' -> tokens.add(Token(null, linenum, lexeme, TokenType.LEFT_BRACE))
                        '}' -> tokens.add(Token(null, linenum, lexeme, TokenType.RIGHT_BRACE))
                        '[' -> tokens.add(Token(null, linenum, lexeme, TokenType.LEFT_BRACKET))
                        ']' -> tokens.add(Token(null, linenum, lexeme, TokenType.RIGHT_BRACKET))
                        ',' -> tokens.add(Token(null, linenum, lexeme, TokenType.COMMA))
                        '.' -> tokens.add(Token(null, linenum, lexeme, TokenType.DOT))
                        ';' -> tokens.add(Token(null, linenum, lexeme, TokenType.SEMI_COLON))
                        ':' -> tokens.add(Token(null, linenum, lexeme, TokenType.COLON))
                    }
                    lexeme = ""
                    i += 1
                    state = 0
                }
                3 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.LESSER_EQUAL_THAN))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.LESSER_THAN))
                    }
                    lexeme = ""
                    state = 0
                }
                4 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.GREATER_EQUAL_THAN))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.GREATER_THAN))
                    }
                    lexeme = ""
                    state = 0
                }
                5 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.ADDITION_ASSIGN))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.ADDITION))
                    }
                    lexeme = ""
                    state = 0
                }
                6 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.SUBSTRACT_ASSIGN))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.SUBSTRACT))
                    }
                    lexeme = ""
                    state = 0
                }
                7 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.MULTIPLICATION_ASSIGN))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.MULTIPLICATION))
                    }
                    lexeme = ""
                    state = 0
                }
                8 -> {
                    when (ch) {
                        '=' -> {
                            lexeme += ch
                            tokens.add(Token(null, linenum, lexeme, TokenType.DIVISION_ASSIGN))
                            state = 0
                            i += 1
                        }
                        '/' -> {
                            state = 13
                            i += 1
                        }
                        '*' -> {
                            state = 14
                            i += 1
                        }
                        else -> {
                            tokens.add(Token(null, linenum, lexeme, TokenType.DIVISION))
                            state = 0
                        }
                    }
                    lexeme = ""
                }
                9 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.MODULUS_ASSIGN))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.MODULUS))
                    }
                    lexeme = ""
                    state = 0
                }
                10 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.EQUALS))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.ASSIGN))
                    }
                    lexeme = ""
                    state = 0
                }
                11 -> {
                    if (ch == '=') {
                        lexeme += ch
                        tokens.add(Token(null, linenum, lexeme, TokenType.NOT_EQUALS))
                        i += 1
                    } else {
                        tokens.add(Token(null, linenum, lexeme, TokenType.NOT))
                    }
                    lexeme = ""
                    state = 0
                }
                12 -> {
                    state = if (ch == '"') {
                        tokens.add(Token(lexeme, linenum, lexeme, TokenType.STRING))
                        0
                    } else if ( (i+1) >= line.length ) {
                        throw Error("Not matching quotes for String at $linenum")
                    } else {
                        12
                    }
                    lexeme += ch
                    i += 1
                }
                13 -> {
                    state = if (ch == '\n') {
                        0
                    } else {
                        13
                    }
                    i += 1
                }
                14 -> {
                    state = if (ch == '*') {
                        15
                    } else {
                        14
                    }
                    i += 1
                }
                15 -> {
                    state = if (ch == '/') {
                        0
                    } else {
                        14
                    }
                    i += 1
                }
                16 -> {
                    when (ch) {
                        in digits -> {
                            lexeme += ch
                            state = 16
                            i += 1
                        }
                        '.' -> {
                            lexeme += ch
                            state = 17
                            i += 1
                        }
                        'e' -> {
                            lexeme += ch
                            state = 19
                            i += 1
                        }
                        else -> {
                            tokens.add(Token(lexeme.toInt(), linenum, lexeme, TokenType.INTEGER))
                            lexeme = ""
                            state = 0
                        }
                    }
                }
                17 -> {
                    if (ch in digits) {
                        lexeme += ch
                        state = 18
                        i += 1
                    } else {
                        throw Error("Bad Number definition at $linenum")
                    }
                }
                18 -> {
                    when (ch) {
                        in digits -> {
                            lexeme += ch
                            state = 18
                            i += 1
                        }
                        'e' -> {
                            lexeme += ch
                            state = 19
                            i += 1
                        }
                        else -> {
                            tokens.add(Token(lexeme.toDouble(), linenum, lexeme, TokenType.DOUBLE))
                            lexeme = ""
                            state = 0
                        }
                    }
                }
                19 -> {
                    when (ch) {
                        in digits -> {
                            lexeme += ch
                            state = 20
                            i += 1
                        }
                        '+', '-' -> {
                            lexeme += ch
                            state = 21
                            i += 1
                        }
                        else -> throw Error("Bad Number definition at $linenum")
                    }
                }
                20 -> {
                    if (ch in digits) {
                        lexeme += ch
                        state = 20
                        i += 1
                    } else {
                        tokens.add(Token(lexeme.toDouble(), linenum, lexeme, TokenType.DOUBLE))
                        lexeme = ""
                        state = 0
                    }
                }
                21 -> {
                    if (ch in digits) {
                        lexeme += ch
                        state = 20
                        i += 1
                    } else {
                        throw Error("Bad Number definition at $linenum")
                    }
                }
                22 -> {
                    when (ch) {
                        in digits, in uppercase, in lowercase, '_' -> {
                            lexeme += ch
                            state = 22
                            i += 1
                        }
                        else -> {
                            if (lexeme in reserved) {
                                tokens.add(Token(null, linenum, lexeme, reserved[lexeme]!!))
                            } else {
                                tokens.add(Token(null, linenum, lexeme, TokenType.IDENTIFIER))
                            }
                            lexeme = ""
                            state = 0
                        }
                    }
                }
            }
        }
    }

    tokens.add(Token(null, 0, "", TokenType.EOF))

    return tokens

}
