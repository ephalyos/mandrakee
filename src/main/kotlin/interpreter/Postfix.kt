
package interpreter

class Postfix (
    val infix: List<Token>
) {
    val postfix = mutableListOf<Token>()
    val stack = mutableListOf<Token>()
    val controlStack = mutableListOf<Token>()

    fun toPostfix () {
        var inControl = false
        for (token in infix) {
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

            else if (token.type == TokenType.LEFT_PARENTHESES){
               stack.add(token)
            }

            else if (token.isOperator()){
               while (stack.size > 0 && stack[stack.size-1].precedence() >= token.precedence()){
                   postfix.add(stack.removeAt(stack.size-1))
               }
               stack.add(token)
            }

        }
    }

}
