
package interpreter

class Token (
    val line: Int = 0,
    val type: TokenType,
    private val lexeme: String,
    private val value: Any? = null,
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
    override fun toString(): String = "Token: $lexeme"

    fun isOperand (): Boolean {
        return when (type) {
            TokenType.IDENTIFIER, TokenType.STRING, TokenType.INTEGER, TokenType.DOUBLE,
            TokenType.TRUE, TokenType.FALSE, TokenType.NIL -> {
                true
            }
            else -> false
        }
    }

    fun isOperator (): Boolean {
        return when (type) {
            TokenType.ASSIGN, TokenType.ADDITION_ASSIGN, TokenType.SUBSTRACT_ASSIGN,
            TokenType.MULTIPLICATION_ASSIGN, TokenType.DIVISION_ASSIGN, TokenType.MODULUS_ASSIGN,
            TokenType.AND, TokenType.OR,
            TokenType.EQUALS, TokenType.NOT_EQUALS,
            TokenType.LESSER_THAN, TokenType.LESSER_EQUAL_THAN,
            TokenType.GREATER_THAN, TokenType.GREATER_EQUAL_THAN,
            TokenType.ADDITION, TokenType.SUBSTRACT,
            TokenType.MULTIPLICATION, TokenType.DIVISION, TokenType.MODULUS,
            TokenType.NOT, TokenType.DOT-> {
                true
            }
            else -> false
        }
    }

    fun isReserved (): Boolean {
        return when (type) {
            TokenType.IF, TokenType.ELSE, TokenType.WHILE, TokenType.FOR,
            TokenType.VAR, TokenType.VAL, TokenType.FUN, TokenType.CLASS,
            TokenType.RETURN, TokenType.PRINT, TokenType.THIS, TokenType.SUPER -> {
                true
            }
            else -> false
        }
    }

    fun isControl (): Boolean {
        return when(type) {
            TokenType.IF, TokenType.ELSE, TokenType.WHILE, TokenType.FOR -> {
                true
            }
            else -> false
        }
    }

    fun precedence (): Int {
        return when(type) {
            TokenType.ASSIGN, TokenType.ADDITION_ASSIGN, TokenType.SUBSTRACT_ASSIGN,
            TokenType.MULTIPLICATION_ASSIGN, TokenType.DIVISION_ASSIGN, TokenType.MODULUS_ASSIGN -> {
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
            TokenType.MULTIPLICATION, TokenType.DIVISION, TokenType.MODULUS -> {
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
            TokenType.NOT, TokenType.DOT -> {
                1
            }
            TokenType.ADDITION, TokenType.SUBSTRACT,
            TokenType.MULTIPLICATION, TokenType.DIVISION, TokenType.MODULUS,
            TokenType.EQUALS, TokenType.NOT_EQUALS,
            TokenType.LESSER_THAN, TokenType.LESSER_EQUAL_THAN,
            TokenType.GREATER_THAN, TokenType.GREATER_EQUAL_THAN,
            TokenType.AND, TokenType.OR-> {
                2
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
