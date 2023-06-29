
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

    fun solve (): Any? {
        return when ( value.type ) {
            TokenType.INTEGER, TokenType.DOUBLE -> {
                value.value
            }
            TokenType.STRING -> {
                value.lexeme
            }
            TokenType.ADDITION, TokenType.SUBSTRACT, TokenType.MULTIPLICATION, TokenType.DIVISION -> {
                SolverArithmetic(node = this).solve()
            }
            TokenType.PRINT -> {
                SolverPrint(node = this).solve()
            }
//            TokenType.EQUALS, TokenType.NOT_EQUALS, TokenType.GREATER_THAN, TokenType.LESSER_THAN,
//            TokenType.GREATER_EQUAL_THAN, TokenType.LESSER_EQUAL_THAN -> {
//
//            }
//            TokenType.IF, TokenType.ELSE -> {
//                1
//            }
            else -> null
        }
    }

    fun run () {
        for ( node in children ) {
            node.solve()
        }
    }

    override fun toString(): String = "$value"

}
