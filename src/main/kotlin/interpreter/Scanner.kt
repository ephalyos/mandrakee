
package interpreter

class Scanner (
    private var lines: List<String>
) {

    private val reserved = hashMapOf(
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

    private val digits = ('0'..'9').joinToString("")
    private val lowercase = ('a'..'z').joinToString("")
    private val uppercase = ('A'..'Z').joinToString("")

    private val save = Action.SAVE_CHAR
    private val grab = Action.GRAB_NEXT_CHAR
    private val clear = Action.CLEAR_LEXEME
    private val add = Action.ADD_TOKEN
    private val error = Action.ERROR

    private val table = hashMapOf(
        0 to listOf(
            Transition(accept = "\n ", state = 0, actions = listOf(grab)),
            Transition(accept = null, state = 1, actions = listOf()),
        ),
        1 to listOf(
            Transition(accept = "<", state = 3, actions = listOf(save, grab)),
            Transition(accept = ">", state = 4, actions = listOf(save, grab)),
            Transition(accept = "+", state = 5, actions = listOf(save, grab)),
            Transition(accept = "-", state = 6, actions = listOf(save, grab)),
            Transition(accept = "*", state = 7, actions = listOf(save, grab)),
            Transition(accept = "/", state = 8, actions = listOf(save, grab)),
            Transition(accept = "%", state = 9, actions = listOf(save, grab)),
            Transition(accept = "=", state = 10, actions = listOf(save, grab)),
            Transition(accept = "!", state = 11, actions = listOf(save, grab)),
            Transition(accept = "\"", state = 12, actions = listOf(grab)),
            Transition(accept = digits, state = 16, actions = listOf(save, grab)),
            Transition(accept = "$lowercase$uppercase", state = 22, actions = listOf(save, grab)),
            Transition(accept = null, state = 2, actions = listOf()),
        ),
        2 to listOf(
            Transition(accept = "(", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.LEFT_PARENTHESES),
            Transition(accept = ")", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.RIGHT_PARENTHESES),
            Transition(accept = "{", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.LEFT_BRACE),
            Transition(accept = "}", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.RIGHT_BRACE),
            Transition(accept = "[", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.LEFT_BRACKET),
            Transition(accept = "]", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.RIGHT_BRACKET),
            Transition(accept = ",", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.COMMA),
            Transition(accept = ".", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.DOT),
            Transition(accept = ";", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.SEMI_COLON),
            Transition(accept = ":", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.COLON),
        ),
        3 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.LESSER_EQUAL_THAN),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.LESSER_THAN),
        ),
        4 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.GREATER_EQUAL_THAN),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.GREATER_THAN),
        ),
        5 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.ADDITION_ASSIGN),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.ADDITION),
        ),
        6 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.SUBSTRACT_ASSIGN),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.SUBSTRACT),
        ),
        7 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.MULTIPLICATION_ASSIGN),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.MULTIPLICATION),
        ),
        8 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.DIVISION_ASSIGN),
            Transition(accept = "/", state = 13, actions = listOf(clear, grab)),
            Transition(accept = "*", state = 14, actions = listOf(clear, grab)),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.DIVISION),
        ),
        9 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.MODULUS_ASSIGN),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.MODULUS),
        ),
        10 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.EQUALS),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.ASSIGN),
        ),
        11 to listOf(
            Transition(accept = "=", state = 0, actions = listOf(save, add, clear, grab), type = TokenType.NOT_EQUALS),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.NOT),
        ),
        12 to listOf(
            Transition(accept = "\"", state = 0, actions = listOf(add, clear, grab), type = TokenType.STRING),
            Transition(accept = "\n", state = 12, actions = listOf(error)),
            Transition(accept = null, state = 12, actions = listOf(save, grab)),
        ),
        13 to listOf(
            Transition(accept = "\n", state = 0, actions = listOf(grab)),
            Transition(accept = null, state = 13, actions = listOf(grab)),
        ),
        14 to listOf(
            Transition(accept = "*", state = 15, actions = listOf(grab)),
            Transition(accept = null, state = 14, actions = listOf(grab)),
        ),
        15 to listOf(
            Transition(accept = "/", state = 0, actions = listOf(grab)),
            Transition(accept = null, state = 14, actions = listOf(grab)),
        ),
        16 to listOf(
            Transition(accept = digits, state = 16, actions = listOf(save, grab)),
            Transition(accept = ".", state = 17, actions = listOf(save, grab)),
            Transition(accept = "e", state = 19, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.INTEGER),
        ),
        17 to listOf(
            Transition(accept = digits, state = 18, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(error)),
        ),
        18 to listOf(
            Transition(accept = digits, state = 18, actions = listOf(save, grab)),
            Transition(accept = "e", state = 19, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.DOUBLE),
        ),
        19 to listOf(
            Transition(accept = digits, state = 20, actions = listOf(save, grab)),
            Transition(accept = "+-", state = 21, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(error)),
        ),
        20 to listOf(
            Transition(accept = digits, state = 20, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.DOUBLE),
        ),
        21 to listOf(
            Transition(accept = digits, state = 20, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(error)),
        ),
        22 to listOf(
            Transition(accept = "_$digits$lowercase$uppercase", state = 22, actions = listOf(save, grab)),
            Transition(accept = null, state = 0, actions = listOf(add, clear), type = TokenType.IDENTIFIER),
        ),
    )

    private var lineIndex = 0
    private var charIndex = 0

    private fun nextChar (): Char? {
        if (lines.isEmpty())
            return null
        if (lineIndex >= lines.size)
            return null
        val currentLine = lines[lineIndex]
        if ((currentLine.isEmpty()) || (charIndex >= currentLine.length)) {
            charIndex = 0
            lineIndex++
            return '\n'
        }
        return currentLine[charIndex++]
    }

    fun scan (): List<Token> {
        var ch = nextChar()
        var state = 0
        var lexeme = ""
        val tokens = mutableListOf<Token>()
        while (ch != null) {
//            println("ch: $ch, lexeme: $lexeme, state: $state, position: ${lineIndex+1}:${charIndex+1}")
            val transitions = table[state]!!
            for (transition in transitions) {
                if ((transition.accept == null) || (ch!! in transition.accept)) {
                    state = transition.state
                    for (action in transition.actions) {
                        when (action) {
                            Action.SAVE_CHAR -> {
                                lexeme += ch
                            }
                            Action.CLEAR_LEXEME -> {
                                lexeme = ""
                            }
                            Action.GRAB_NEXT_CHAR -> {
                                ch = nextChar()
                            }
                            Action.ADD_TOKEN -> {
                                when (transition.type) {
                                    TokenType.IDENTIFIER -> {
                                        if (lexeme in reserved) {
                                            tokens.add(Token(value = null, line = lineIndex, lexeme = lexeme, type = reserved[lexeme]!!))
                                        } else {
                                            tokens.add(Token(value = null, line = lineIndex, lexeme = lexeme, type = TokenType.IDENTIFIER))
                                        }
                                    }
                                    TokenType.INTEGER -> {
                                        tokens.add(Token(value = lexeme.toInt(), line = lineIndex, lexeme = lexeme, type = TokenType.INTEGER))
                                    }
                                    TokenType.DOUBLE -> {
                                        tokens.add(Token(value = lexeme.toDouble(), line = lineIndex, lexeme = lexeme, type = TokenType.DOUBLE))
                                    }
                                    else -> {
                                        tokens.add(Token(value = null, line = lineIndex, lexeme = lexeme, type = transition.type!!))
                                    }
                                }
                            }
                            Action.ERROR -> {
                                if (state == 12) {
                                    throw Error("Failed to Scan String at line $lineIndex:$charIndex")
                                } else {
                                    throw Error("Failed to Scan Number at line $lineIndex:$charIndex")
                                }
                            }
                        }
                    }
                    break
                }
            }
        }

        // flag token

        tokens.add(Token(value = null, line = 0, lexeme = "", type = TokenType.EOF))

        return tokens

    }

}

enum class Action {
    SAVE_CHAR,
    GRAB_NEXT_CHAR,
    CLEAR_LEXEME,
    ADD_TOKEN,
    ERROR
}

class Transition(
    val accept: String?,
    val state: Int,
    val actions: List<Action>,
    val type: TokenType? = null
)
