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
                } else if ( left is String && right is Int ) {
                    "$left$right"
                } else if ( left is String && right is Double ) {
                    "$left$right"
                } else if ( left is Int && right is String ) {
                    "$left$right"
                } else if ( left is Double && right is String ) {
                    "$left$right"
                } else if ( left is Int && right is Int ) {
                    left + right
                } else if ( left is Int && right is Double ) {
                    left + right
                } else if ( left is Double && right is Int ) {
                    left + right
                } else if ( left is Double && right is Double ) {
                    left + right
                } else {
                    throw Error("Unsupported Type Operation")
                }
            }
            TokenType.SUBSTRACT -> {
                if ( left is Double && right is Double ) {
                    left - right
                } else if ( left is Int && right is Int ) {
                    left - right
                } else {
                    throw Error("Unsupported Type Operation")
                }
            }
            TokenType.MULTIPLICATION -> {
                if ( left is Double && right is Double ) {
                    left * right
                } else if ( left is Int && right is Int ) {
                    left * right
                } else if ( left is Int && right is Double ) {
                    left * right
                } else if ( left is Double && right is Int ) {
                    left * right
                } else {
                    throw Error("Unsupported Type Operation")
                }
            }
            TokenType.DIVISION -> {
                if ( left is Double && right is Double ) {
                    left / right
                } else if ( left is Int && right is Int ) {
                    left / right
                } else {
                    throw Error("Unsupported Type Operation")
                }
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
                if ( left is Double && right is Double ) {
                    left > right
                } else if ( left is Int && right is Int ) {
                    left > right
                } else if ( left is Int && right is Double ) {
                    left > right
                } else if ( left is Double && right is Int ) {
                    left > right
                } else {
                    throw Error("Unsupported Type Operation")
                }
            }
            TokenType.LESSER_THAN -> {
                if ( left is Double && right is Double ) {
                    left < right
                } else if ( left is Int && right is Int ) {
                    left < right
                } else {
                    throw Error("Unsupported Type Operation")
                }
            }
            TokenType.GREATER_EQUAL_THAN -> {
                if ( left is Double && right is Double ) {
                    left >= right
                } else if ( left is Int && right is Int ) {
                    left >= right
                } else {
                    throw Error("Unsupported Type Operation")
                }
            }
            TokenType.LESSER_EQUAL_THAN -> {
                if ( left is Double && right is Double ) {
                    left <= right
                } else if ( left is Int && right is Int ) {
                    left <= right
                } else {
                    throw Error("Unsupported Type Operation")
                }
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
        val value = if ( node.children.size > 1 ) node.children[0].solve() else 0
        val identifier = if ( node.children.size > 1 ) node.children[1] else node.children[0]
        if ( identifier.value.type != TokenType.IDENTIFIER )
            throw Error("Left Side Must be an Identifier")
        else
            SymbolTable.add(key = identifier.value.lexeme, value = value)
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
            SymbolTable.update(key = left.lexeme, value = right)
    }
}

class SolverIf (
    private val node: Node
) {
    fun solve () {

        val condition = node.children[0].solve()
        val elseBody = if ( node.children.last().value.type == TokenType.ELSE ) node.children.last() else null

        if ( condition !is Boolean )
            throw Error("Condition Expression Must Be Boolean")

        if ( condition ) {
            for ( i in 1..(node.children.size - 2))
                node.children[i].solve()
        }

        if ( elseBody != null  && condition == false) {
            for ( child in elseBody.children )
                child.solve()
        } else if ( elseBody == null && condition == true ){
            node.children.last().solve()
        }

    }
}

class SolverWhile (
    private val node: Node
) {
    fun solve () {

        val condition = node.children[0]

        if ( condition.solve() !is Boolean )
            throw Error("Condition Expression Must Be Boolean")

        while ( condition.solve() as Boolean ) {
            for ( i in 1 until node.children.size)
                node.children[i].solve()
        }

    }
}

class SolverFor (
    private val node: Node
) {
    fun solve () {

        val initialize = node.children[0]
        val condition = node.children[1]
        val step = node.children[2]

        initialize.solve()

        if ( condition.solve() !is Boolean )
            throw Error("Condition Expression Must Be Boolean")

        while ( condition.solve() as Boolean ) {
            for ( i in 3 until node.children.size )
                node.children[i].solve()
            step.solve()
        }

    }
}
