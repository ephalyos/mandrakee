
package sql

class LRParser (
    private val tokens: List<Token>
) {

    private val productions = hashMapOf(
        1 to Pair("Q", listOf("select", "D", "from", "T")),
        2 to Pair("D", listOf("distinct", "P")),
        3 to Pair("D", listOf("P")),
        4 to Pair("P", listOf("*")),
        5 to Pair("P", listOf("A")),
        6 to Pair("A", listOf("A2", "A1")),
        7 to Pair("A1", listOf(",", "A")),
        8 to Pair("A1", listOf()),
        9 to Pair("A2", listOf("id", "A3")),
        10 to Pair("A3", listOf(".", "id")),
        11 to Pair("A3", listOf()),
        12 to Pair("T", listOf("T2", "T1")),
        13 to Pair("T1", listOf(",", "T")),
        14 to Pair("T1", listOf()),
        15 to Pair("T2", listOf("id", "T3")),
        16 to Pair("T3", listOf("id")),
        17 to Pair("T3", listOf())
    )

    enum class ACTION {
        SHIFT,
        REDUCE,
        ACCEPT
    }
    private val s = ACTION.SHIFT
    private val r = ACTION.REDUCE
    private val acc = ACTION.ACCEPT

    private val actionTable = hashMapOf(
        0 to hashMapOf(
            "select" to Pair(s, 2),
        ),
        1 to hashMapOf(
            "$" to Pair(acc, 0),
        ),
        2 to hashMapOf(
            "distinct" to Pair(s, 4),
            "*" to Pair(s, 6),
            "id" to Pair(s, 9),
        ),
        3 to hashMapOf(
            "from" to Pair(s, 10),
        ),
        4 to hashMapOf(
            "*" to Pair(s, 6),
            "id" to Pair(s, 9),
        ),
        5 to hashMapOf(
            "from" to Pair(r, 3),
        ),
        6 to hashMapOf(
            "from" to Pair(r, 4),
        ),
        7 to hashMapOf(
            "from" to Pair(r, 5),
        ),
        8 to hashMapOf(
            "from" to Pair(r, 8),
            "," to Pair(s, 13),
        ),
        9 to hashMapOf(
            "from" to Pair(r, 11),
            "," to Pair(r, 11),
            "." to Pair(s, 15),
        ),
        10 to hashMapOf(
            "id" to Pair(s, 18),
        ),
        11 to hashMapOf(
            "from" to Pair(r, 2),
        ),
        12 to hashMapOf(
            "from" to Pair(r, 6),
        ),
        13 to hashMapOf(
            "id" to Pair(s, 9),
        ),
        14 to hashMapOf(
            "from" to Pair(r, 9),
            "," to Pair(r, 9),
        ),
        15 to hashMapOf(
            "id" to Pair(s, 20),
        ),
        16 to hashMapOf(
            "$" to Pair(r, 1),
        ),
        17 to hashMapOf(
            "," to Pair(s, 22),
            "$" to Pair(r, 14),
        ),
        18 to hashMapOf(
            "," to Pair(r, 17),
            "id" to Pair(s, 24),
            "$" to Pair(r, 17),
        ),
        19 to hashMapOf(
            "from" to Pair(r, 7),
        ),
        20 to hashMapOf(
            "from" to Pair(r, 10),
            "," to Pair(r, 10),
        ),
        21 to hashMapOf(
            "$" to Pair(r, 12),
        ),
        22 to hashMapOf(
            "id" to Pair(s, 18),
        ),
        23 to hashMapOf(
            "," to Pair(r, 15),
            "$" to Pair(r, 15),
        ),
        24 to hashMapOf(
            "," to Pair(r, 16),
            "$" to Pair(r, 16),
        ),
        25 to hashMapOf(
            "$" to Pair(r, 13),
        ),
    )

    private val gotoTable = hashMapOf(
        0 to hashMapOf(
            "Q" to 1
        ),
        2 to hashMapOf(
            "D" to 3,
            "P" to 5,
            "A" to 7,
            "A2" to 8
        ),
        4 to hashMapOf(
            "P" to 11,
            "A" to 7,
            "A2" to 8
        ),
        8 to hashMapOf(
            "A1" to 12
        ),
        9 to hashMapOf(
            "A3" to 14
        ),
        10 to hashMapOf(
            "T" to 16,
            "T2" to 17
        ),
        13 to hashMapOf(
            "A" to 19,
            "A2" to 8
        ),
        17 to hashMapOf(
            "T1" to 21
        ),
        18 to hashMapOf(
            "T3" to 23
        ),
        22 to hashMapOf(
            "T" to 25,
            "T2" to 17
        ),
    )

    private fun actionLookup (state: Int, terminal: String): Pair<ACTION, Int> {
        return actionTable[state]?.get(terminal) ?: throw Error()
    }

    private fun gotoLookup (state: Int, nonTerminal: String): Int {
        return gotoTable[state]?.get(nonTerminal) ?: throw Error()
    }

    private var i = 0
    private fun getNextSymbol (): String {
        return try {
            val nextToken = tokens[i++]
            if (nextToken.type == TokenType.IDENTIFIER) "id" else nextToken.lexeme
        } catch (e: IndexOutOfBoundsException) {
            "Error Occurred"
        }
    }

    private var stateStack = mutableListOf(0)
    private var symbolStack = mutableListOf<String>()

    fun parse () {

        var currentSymbol = getNextSymbol()
        var currentState = stateStack[stateStack.size - 1]

        while (true) {

            var action: Pair<ACTION, Int>
            try {
                action = actionLookup(currentState, currentSymbol)
            } catch (e: Error) {
                println("Invalid query")
                return
            }
            println("states: $stateStack")
            println("symbols: $symbolStack")

            if (action.first == ACTION.SHIFT) {
                stateStack.add(action.second)
                symbolStack.add(currentSymbol)
                currentSymbol = getNextSymbol()
                currentState = stateStack[stateStack.size - 1]

            } else if (action.first == ACTION.REDUCE) {
                val head = productions[action.second]?.first!!
                val body = productions[action.second]?.second!!
                var temp = 0
                while (temp < body.size) {
                    stateStack.removeAt(stateStack.size - 1)
                    symbolStack.removeAt(symbolStack.size - 1)
                    temp++
                }
                currentState = stateStack[stateStack.size - 1]
                var nextState: Int
                try {
                    nextState = gotoLookup(currentState, head)
                } catch (e: Error) {
                    println("Invalid query")
                    return
                }
                stateStack.add(nextState)
                symbolStack.add(head)
                currentState = nextState

            } else {
                break
            }

        }
        println("Valid query")
    }

}