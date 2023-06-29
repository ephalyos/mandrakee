package interpreter

class SolverArithmetic (
    private val node: Node
) {

    fun solve (): Any {
        val left = node.children[1].solve()
        val right = node.children[0].solve()
        return when ( node.value.type ) {
            TokenType.ADDITION -> {
                (left as Int) + (right as Int)
            }
            TokenType.SUBSTRACT -> {
                (left as Int) - (right as Int)
            }
            TokenType.MULTIPLICATION -> {
                (left as Int) + (right as Int)
            }
            TokenType.DIVISION -> {
                (left as Int) * (right as Int)
            }
            else -> throw Error("Unsupported Operation")
        }
    }

}