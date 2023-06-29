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
                (left as Int) * (right as Int)
            }
            TokenType.DIVISION -> {
                (left as Int) / (right as Int)
            }
            else -> throw Error("Unsupported Operation")
        }
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

class SolverBool (
    private val node: Node
) {
    fun solve (): Boolean {
        val left = node.children[0].solve()
        val right = node.children[1].solve()
        if ( left !is Boolean || right !is Boolean )
            throw Error("Unsupported Type Operation")
        return when ( node.value.type ) {
            TokenType.AND -> {
                left and right
            }
            TokenType.OR -> {
                left or right
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

class SolverAssignment (
    private val node: Node
) {
    fun solve () {
        val left = node.children[1].value   // must be a defined identifier
        val right = node.children[0].solve()
        if ( left.type != TokenType.IDENTIFIER )
            throw Error("Left Side Must be an Identifier")
        else
            SymbolTable.update(key = left.lexeme, value = right!!)
    }
}

class SolverIf (
    private val node: Node
) {
    fun solve () {
        val condition = node.children[0].solve()
        if ( condition !is Boolean )
            throw Error("Condition Expression Must Be Boolean")
        if ( condition ) {
            for ( i in 1..(node.children.size - 2))
                node.children[i].solve()
        }
        if ( condition && node.children.last().value.type != TokenType.ELSE ){
            node.children.last().solve()
        } else {
            val elseBody = node.children.last()
            for ( child in elseBody.children )
                child.solve()
        }
    }
}

