
package sql

class Scanner {

    val reserved = hashMapOf(
        "select" to TokenType.SELECT,
        "from" to TokenType.FROM,
        "distinct" to TokenType.DISTINCT
    )

}

fun Scanner.scan (line: String): List<Token> {

    val tokens = mutableListOf<Token>()

    val digits = '0'..'9'
    val lowercase = 'a'..'z'
    val uppercase = 'A'..'Z'

    var state = 0
    var lexeme = ""
    var i = 0

    while (i < line.length) {
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
                when (ch) {
                    '*' -> {
                        tokens.add(Token(TokenType.ASTERISK, "*"))
                        state = 0
                        i++
                    }
                    '.' -> {
                        tokens.add(Token(TokenType.DOT, "."))
                        state = 0
                        i++
                    }
                    ',' -> {
                        tokens.add(Token(TokenType.COMMA, ","))
                        state = 0
                        i++
                    }
                    in lowercase, in uppercase -> {
                        state = 2
                        lexeme += ch
                        i++
                    }
                    else -> {
                        throw Error("Error: invalid character found")
                    }
                }
            }
            2 -> {
                when (ch) {
                    in lowercase, in uppercase, in digits -> {
                        lexeme += ch
                        i++
                    }
                    else -> {
                        if (lexeme in reserved) {
                            tokens.add(Token(reserved[lexeme]!!, lexeme))
                        } else {
                            tokens.add(Token(TokenType.IDENTIFIER, lexeme))
                        }
                        lexeme = ""
                        state = 0
                    }
                }
            }
        }
    }

    tokens.add(Token(TokenType.EOF, ""))

    return tokens

}
