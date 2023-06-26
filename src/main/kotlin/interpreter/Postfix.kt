
package interpreter

class Postfix (
    private val infix: List<Token>
) {
    private val postfix = mutableListOf<Token>()
    private val stack = mutableListOf<Token>()
    private val controlStack = mutableListOf<Token>()

    fun toPostfix (): List<Token> {

        var inControl = false
        var index = 0

        for (token in infix) {
            index++
            if (token.type == TokenType.EOF) {
               break
            }
            else if (token.isReserved()) {
                postfix.add(token)
                if (token.isControl()) {
                    inControl = true
                    controlStack.add(token)
                }
            }
            else if (token.isOperand()) {
               postfix.add(token)
            }
            else if (token.isOperator()) {
               while (stack.isNotEmpty() && stack.last().precedence() >= token.precedence()) {
                   postfix.add(stack.removeLast())
               }
               stack.add(token)
            }
            else if (token.type == TokenType.LEFT_PARENTHESES) {
                stack.add(token)
            }
            else if (token.type == TokenType.RIGHT_PARENTHESES) {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PARENTHESES) {
                    postfix.add(stack.removeLast())
                }
                if (stack.last().type == TokenType.LEFT_PARENTHESES)
                    stack.removeLast()
                if (inControl) {
                    postfix.add(Token(type = TokenType.SEMI_COLON, lexeme = ";"))
                }
            }
            else if (token.type == TokenType.LEFT_BRACE) {
                stack.add(token)
            }
            else if (token.type == TokenType.RIGHT_BRACE && inControl) {
                if (infix[index].type == TokenType.ELSE) {
                    stack.removeLast()
                }
                else {
                    stack.removeLast()
                    postfix.add(Token(type = TokenType.SEMI_COLON, lexeme = ";"))
                    controlStack.removeLast()
                    if (controlStack.isEmpty())
                        inControl = false
                }
            }
            else if (token.type == TokenType.SEMI_COLON) {
                while (stack.isNotEmpty() && stack.last().type != TokenType.LEFT_BRACE) {
                    postfix.add(stack.removeLast())
                }
                postfix.add(token)
            }
        }

        while (stack.isNotEmpty())
            postfix.add(stack.removeLast())

        while (controlStack.isNotEmpty()) {
            controlStack.removeLast()
            postfix.add(Token(type = TokenType.SEMI_COLON, lexeme = ";"))
        }

        return postfix

    }

}
