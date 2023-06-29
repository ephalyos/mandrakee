
package interpreter

class Token (
    val line: Int = 0,
    val type: TokenType,
    val lexeme: String,
    val value: Any? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (other !is Token){
            return false
        }
        return this.type == other.type
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + lexeme.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + line
        return result
    }

//    override fun toString(): String = "Token: ${type.name} with lexeme: $lexeme found at: $line with value: $value"
    override fun toString(): String = lexeme

    fun precedence (): Int {
        return when(type) {
            TokenType.ASSIGN -> {
                1
            }
            TokenType.AND, TokenType.OR -> {
                2
            }
            TokenType.EQUALS, TokenType.NOT_EQUALS -> {
                3
            }
            TokenType.GREATER_THAN, TokenType.GREATER_EQUAL_THAN, TokenType.LESSER_THAN, TokenType.LESSER_EQUAL_THAN -> {
                4
            }
            TokenType.ADDITION, TokenType.SUBSTRACT -> {
                5
            }
            TokenType.MULTIPLICATION, TokenType.DIVISION -> {
                6
            }
            TokenType.NOT, TokenType.DOT -> {
                7
            }
            else -> 0
        }
    }

    fun arity (): Int {
        return when (type) {
            TokenType.ASSIGN, TokenType.AND, TokenType.OR,
            TokenType.EQUALS, TokenType.NOT_EQUALS,
            TokenType.GREATER_THAN, TokenType.GREATER_EQUAL_THAN,
            TokenType.LESSER_THAN, TokenType.LESSER_EQUAL_THAN,
            TokenType.ADDITION, TokenType.SUBSTRACT,
            TokenType.MULTIPLICATION, TokenType.DIVISION -> {
                2
            }
            TokenType.NOT, TokenType.DOT -> {
                1
            }
            else -> 0
        }
    }

}

enum class TokenType {

    IDENTIFIER, INTEGER, DOUBLE, STRING,
    TRUE, FALSE, NIL,

    IF, ELSE, FOR, WHILE,
    VAR, VAL, FUN, CLASS,
    THIS, SUPER,
    PRINT, RETURN,
    EOF,

    COMMA, COLON, SEMI_COLON,

    LEFT_PARENTHESES, RIGHT_PARENTHESES,
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET,

    ASSIGN,
    ADDITION_ASSIGN, SUBSTRACT_ASSIGN,
    MULTIPLICATION_ASSIGN, DIVISION_ASSIGN, MODULUS_ASSIGN, // 1
    AND, OR, // 2
    EQUALS, NOT_EQUALS, // 3
    LESSER_THAN, LESSER_EQUAL_THAN,
    GREATER_THAN, GREATER_EQUAL_THAN, // 4
    ADDITION, SUBSTRACT, // 5
    MULTIPLICATION, DIVISION, MODULUS, // 6
    NOT, DOT, // 7

}
