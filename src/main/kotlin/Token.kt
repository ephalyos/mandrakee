
class Token (
    private val value: Any?,
    private val line: Int = 0,
    private val lexeme: String,
    private val type: TokenType,
) {

    override fun equals(other: Any?): Boolean {
        if (other !is Token){
            return false
        }
        return false
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + lexeme.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + line
        return result
    }

    override fun toString(): String = "Token: $type with lexeme: $lexeme found at: $line with value: $value"

}

enum class TokenType {

    IDENTIFIER,

    INTEGER,
    DOUBLE,
    STRING,

    ASSIGN,
    EQUALS,
    NOT_EQUALS,

    NOT,

    COMMA,
    DOT,
    COLON,
    SEMI_COLON,

    LESSER_THAN,
    LESSER_EQUAL_THAN,
    GREATER_THAN,
    GREATER_EQUAL_THAN,

    ADDITION,
    ADDITION_ASSIGN,
    SUBSTRACT,
    SUBSTRACT_ASSIGN,
    MULTIPLICATION,
    MULTIPLICATION_ASSIGN,
    DIVISION,
    DIVISION_ASSIGN,
    MODULUS,
    MODULUS_ASSIGN,

    LEFT_PARENTHESES,
    RIGHT_PARENTHESES,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,

    EOF,

    IF,
    ELSE,
    VAR,
    VAL,
    WHILE,
    FOR,
    TRUE,
    FALSE,
    THIS,
    FUN,
    RETURN,
    CLASS,
    SUPER,
    AND,
    OR,
    NIL,
    PRINT,

}