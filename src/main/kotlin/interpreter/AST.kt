
package interpreter

class AST (
    private val postfix: List<Token>
) {

    fun toAST (): Node<Token> {

        val stack = mutableListOf<Node<Token>>()
        val fathers = mutableListOf<Node<Token>>()

        val root  = Node(Token(type = TokenType.EOF, lexeme = ""))
        fathers.add(root)

        for ( token in postfix ) {

            when ( token.type ) {

                TokenType.VAR, TokenType.PRINT, TokenType.IF, TokenType.WHILE, TokenType.FOR -> {
                    val node = Node(token)
                    fathers.last().addChild(node)
                    fathers.add(node)
                }

                TokenType.ELSE -> {
                    val node = Node(token)
                    fathers.last().addChild(node)
                    fathers.removeLast()
                    fathers.add(node)
                }

                TokenType.INTEGER, TokenType.DOUBLE, TokenType.STRING, TokenType.IDENTIFIER,
                TokenType.TRUE, TokenType.FALSE -> {
                    stack.add(Node(token))
                }

                TokenType.ASSIGN, TokenType.AND, TokenType.OR, TokenType.EQUALS, TokenType.NOT_EQUALS,
                TokenType.LESSER_THAN, TokenType.LESSER_EQUAL_THAN, TokenType.GREATER_THAN, TokenType.GREATER_EQUAL_THAN,
                TokenType.ADDITION, TokenType.SUBSTRACT, TokenType.MULTIPLICATION, TokenType.DIVISION -> {
                    val node = Node(token)
                    for ( i in 1..token.arity() )
                        node.addChild(stack.removeLast())
                    stack.add(node)
                }

                TokenType.SEMI_COLON -> {
                    if ( stack.isEmpty() )
                        fathers.removeLast()
                    else {
                        when ( fathers.last().value.type ) {
                            TokenType.VAR -> {
                                if ( stack.last().value.type == TokenType.ASSIGN ){
                                    fathers.last().addChildren(stack.last().children)
                                    stack.removeLast()
                                }
                                else
                                    fathers.last().addChild(stack.removeLast())
                                fathers.removeLast()
                            }
                            TokenType.PRINT -> {
                                fathers.last().addChild(stack.removeLast())
                                fathers.removeLast()
                            }
                            else -> {
                                fathers.last().addChild(stack.removeLast())
                            }
                        }
                    }
                }

                else -> break

            }

        }

        return root
    }

}
