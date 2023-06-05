
package sql

class Token (
    val type: TokenType,
    val lexeme: String,
    private val line: Int = 0
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
        result = 31 * result + line
        return result
    }

    override fun toString(): String = "Token: ${type.name} with lexeme: $lexeme found at: $line"

}

enum class TokenType {

    IDENTIFIER,

    SELECT,
    FROM,
    DISTINCT,
    COMMA,
    DOT,
    ASTERISK,

    EOF

}
