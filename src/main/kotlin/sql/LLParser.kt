
package sql

class LLParser (
    private val tokens: List<Token>
) {

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

    private fun lookup (nonTerminal: String, terminal: String): List<String> {
        return table[nonTerminal]?.get(terminal)?.reversed() ?: throw Error("Error at symbol: $terminal")
    }

    private var stack = mutableListOf("$", "Q")

    private var i = 0

    private fun getNextSymbol(): String {
        val nextToken = tokens[i++]
        return if (nextToken.type == TokenType.IDENTIFIER) "id" else nextToken.lexeme
    }

    fun parse () {

        var currentSymbol = getNextSymbol()

        while (stack.isNotEmpty()) {

            println("current stack: $stack current symbol: $currentSymbol")

            val top = stack.removeAt(stack.size - 1)

            if (top == currentSymbol) {
                if (top == "$") break
                currentSymbol = getNextSymbol()
            } else {
                try {
                    val derivation = lookup(top, currentSymbol)
                    stack.addAll(derivation)
                } catch (e: Error) {
                    println("Invalid Query")
                    return
                }
            }

        }

        println("Valid query")

    }

}