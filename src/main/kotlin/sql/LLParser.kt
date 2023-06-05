
package sql

class LLParser (
    private val tokens: List<Token>
) {

    private val terminals = hashMapOf(
        "id" to TokenType.IDENTIFIER,
        "select" to TokenType.SELECT,
        "from" to TokenType.FROM,
        "distinct" to TokenType.DISTINCT,
        "," to TokenType.COMMA,
        "." to TokenType.DOT,
        "*" to TokenType.ASTERISK,
        "$" to TokenType.EOF
    )

    private val table = hashMapOf(
        "Q" to hashMapOf(
            "select" to listOf("select", "D", "from", "T")
        ),
        "D" to hashMapOf(
            "distinct" to listOf("distinct", "P"),
            "*" to listOf("P"),
            "id" to listOf("P")
        ),
        "P" to hashMapOf(
            "*" to listOf("*"),
            "id" to listOf("A")
        ),
        "A" to hashMapOf(
            "id" to listOf("A2", "A1")
        ),
        "A1" to hashMapOf(
            "," to listOf(",", "A"),
            "from" to listOf()
        ),
        "A2" to hashMapOf(
            "id" to listOf("id", "A3")
        ),
        "A3" to hashMapOf(
            "." to listOf(".", "id"),
            "from" to listOf(),
            "," to listOf()
        ),
        "T" to hashMapOf(
            "id" to listOf("T2", "T1")
        ),
        "T1" to hashMapOf(
            "," to listOf(",", "T"),
            "$" to listOf()
        ),
        "T2" to hashMapOf(
            "id" to listOf("id", "T3")
        ),
        "T3" to hashMapOf(
            "id" to listOf("id"),
            "," to listOf(),
            "$" to listOf()
        )
    )

    fun lookup (nonTerminal: String, terminal: String): List<String> {
        return table[nonTerminal]?.get(terminal)?.reversed()!!
    }

    private var stack = mutableListOf("$", "Q")

    private var i = 0

    private fun getNextToken (): Token {
        return tokens[++i]
    }

    fun parse () {
        while (true) {
            val stackTop = stack.removeAt(stack.size - 1)
            val inputToken = getNextToken()
            val terminal = if (inputToken.type == TokenType.IDENTIFIER) "id" else inputToken.lexeme
            val derivation = lookup(stackTop, terminal)
            break
        }
    }

}