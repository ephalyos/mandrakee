
package interpreter

class AST (
    private val postfix: List<Token>
) {

    fun makeAST (): Node<Token> {

        val stack = mutableListOf<Node<Token>>()
        val fatherStack = mutableListOf<Node<Token>>()
        val root = Node(Token(type = TokenType.EOF, lexeme = ""))

        var father = root
        fatherStack.add(root)

        for (token in postfix) {
//            root.getChild()
//            println("Stack: $stack")
//            println("Father: $fatherStack")
            if (token.isReserved()) {
                val node = Node(token)
                fatherStack.last().addChild(node)
                fatherStack.add(node)
                father = node
            }
            else if (token.isOperand()) {
                stack.add(Node(token))
            }
            else if (token.isOperator()) {
                val operator = Node(token)
                for (i in 1..token.arity()) {
                    operator.addChild(stack.removeLast())
                }
                stack.add(operator)
            }
            else if (token.type == TokenType.SEMI_COLON) {
                if (stack.isEmpty()) {
                    fatherStack.removeLast()
                    father = fatherStack.last()
                }
                else {
                    val temp = stack.removeLast()
                    when (father.value.type) {
                        TokenType.VAR -> {
                            father.addChildren(temp.children)
                            fatherStack.removeLast()
                            father = fatherStack.last()
                        }
                        TokenType.PRINT -> {
                            father.addChild(temp)
                            fatherStack.removeLast()
                            father = fatherStack.last()
                        }
                        else -> {
                            father.addChild(temp)
                        }
                    }
                }
            }
        }

        return root

    }

}
