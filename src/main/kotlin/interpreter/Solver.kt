package interpreter

class SolverArithmetic (
    private val node: Node
) {
    fun solve (): Any {
        val left = node.children[1].solve()
        val right = node.children[0].solve()
        return when ( node.value.type ) {
            TokenType.ADDITION -> {
                if ( left is String && right is String ) {
                    "$left$right"
                } else {
                    (left as Int) + (right as Int)
                }
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

class SolverPrint (
    private val node: Node
) {
    fun solve () {
        val child = node.children[0].solve()
        println(child)
    }
}

class SolverComparison (
    private val node: Node
) {
    fun solve (): Boolean {
        val left = node.children[1].solve()
        val right = node.children[0].solve()
        return when ( node.value.type ) {
            TokenType.EQUALS -> {
                left == right
            }
            TokenType.NOT_EQUALS -> {
                left != right
            }
            TokenType.GREATER_THAN -> {
                if ( left is Int && right is Int )
                    left > right
                else
                    throw Error("Unsupported Type Operation")
            }
            TokenType.LESSER_THAN -> {
                if ( left is Int && right is Int )
                    left < right
                else
                    throw Error("Unsupported Type Operation")
            }
            TokenType.GREATER_EQUAL_THAN -> {
                if ( left is Int && right is Int )
                    left >= right
                else
                    throw Error("Unsupported Type Operation")
            }
            TokenType.LESSER_EQUAL_THAN -> {
                if ( left is Int && right is Int )
                    left <= right
                else
                    throw Error("Unsupported Type Operation")
            }
            else -> throw Error("Unsupported Operation")
        }
    }
}

class SolverVar (
    private val node: Node
) {
    fun solve () {
        val left = node.children[1].value        // will be an undefined identifier
        val right = node.children[0].solve()
        if ( left.type != TokenType.IDENTIFIER )
            throw Error("Left Side Must be an Identifier")
        else
            SymbolTable.add(key = left.lexeme, value = right!!)
    }
}
