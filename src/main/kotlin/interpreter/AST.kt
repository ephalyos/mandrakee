
package interpreter

class AST (
    private val postfix: List<Token>
) {

    private val stack = mutableListOf<Node<Token>>()

    fun makeAST () {

        for (token in postfix) {
            if (token.type == TokenType.EOF) {
                break
            }
            else if (token.isReserved()) {

            }
        }

    }

}