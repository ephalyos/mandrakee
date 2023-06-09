
package interpreter

class Node (
    val value: Token,
) {

    val children = mutableListOf<Node>()

    fun addChild ( node: Node ) {
        children.add(node)
    }

    fun addChildren (children: List<Node>) {
        for ( child in children ) {
            addChild(child)
        }
    }

    fun printChildren ( depth: Int = 0) {
        for (child in children) {
            val spaces = "--".repeat(depth)
            println("$spaces${child.value}")
            child.printChildren(depth + 1)
        }
    }

    fun solve (): Any {
        return when ( value.type ) {
            TokenType.IDENTIFIER -> {
                SymbolTable.retrieve(value.lexeme)
            }
            TokenType.STRING -> {
                value.lexeme
            }
            TokenType.TRUE -> {
                true
            }
            TokenType.FALSE -> {
                false
            }
            TokenType.INTEGER, TokenType.DOUBLE -> {
                value.value
            }
            TokenType.ADDITION, TokenType.SUBSTRACT, TokenType.MULTIPLICATION, TokenType.DIVISION -> {
                SolverArithmetic(node = this).solve()
            }
            TokenType.PRINT -> {
                SolverPrint(node = this).solve()
            }
            TokenType.EQUALS, TokenType.NOT_EQUALS,
            TokenType.GREATER_THAN, TokenType.LESSER_THAN,
            TokenType.GREATER_EQUAL_THAN, TokenType.LESSER_EQUAL_THAN -> {
                SolverComparison(node = this).solve()
            }
            TokenType.VAR -> {
                SolverVar(node = this).solve()
            }
            TokenType.ASSIGN -> {
                SolverAssignment(node = this).solve()
            }
            TokenType.AND, TokenType.OR -> {
                SolverBool(node = this).solve()
            }
            TokenType.IF -> {
                SolverIf(node = this).solve()
            }
            TokenType.WHILE -> {
                SolverWhile(node = this).solve()
            }
            TokenType.FOR -> {
                SolverFor(node = this).solve()
            }
            else -> value
        }!!
    }

    fun run () {
        for ( node in children ) {
            node.solve()
        }
    }

    override fun toString(): String = "$value"

}
