
package interpreter

import java.awt.SystemColor.control

class Postfix (
    private val infix: List<Token>
) {

    private val postfix = mutableListOf<Token>()

    fun toPostfix (): List<Token> {

        val stack = mutableListOf<Token>()
        val control = mutableListOf<Token>()

        for (token in infix ) {

            when ( token.type ) {

                TokenType.EOF -> break

                TokenType.INTEGER, TokenType.DOUBLE, TokenType.STRING, TokenType.IDENTIFIER -> {
                    postfix.add(token)
                }

                TokenType.VAR, TokenType.VAL, TokenType.FUN, TokenType.CLASS -> {
                    postfix.add(token)
                }

                TokenType.IF -> {
                    postfix.add(token)
                    control.add(token)
                }

                TokenType.ELSE -> {
                    postfix.removeLast()    // remove the ';' added by closing '}' of 'if'
                    postfix.add(token)
                    control.add(token)
                }

                TokenType.LEFT_PARENTHESES -> {
                    stack.add(token)
                }

                TokenType.RIGHT_PARENTHESES -> {
                    while ( stack.isNotEmpty() && stack.last().type != TokenType.LEFT_PARENTHESES )
                        postfix.add(stack.removeLast())
                    stack.removeLast()
                }

                TokenType.LEFT_BRACE -> {
                    if ( control.last().type == TokenType.IF )
                        postfix.add(Token(type = TokenType.SEMI_COLON, lexeme = ";" ))
                    stack.add(token)
                }

                TokenType.RIGHT_BRACE -> {
                    stack.removeLast()
                    control.removeLast()
                    postfix.add(Token(type = TokenType.SEMI_COLON, lexeme = ";" ))
                }

                TokenType.SEMI_COLON -> {
                    while ( stack.isNotEmpty() && stack.last().type != TokenType.LEFT_BRACE )
                        postfix.add(stack.removeLast())
                    postfix.add(token)
                }

                TokenType.ASSIGN, TokenType.AND, TokenType.OR, TokenType.EQUALS, TokenType.NOT_EQUALS,
                TokenType.LESSER_THAN, TokenType.LESSER_EQUAL_THAN, TokenType.GREATER_THAN, TokenType.GREATER_EQUAL_THAN,
                TokenType.ADDITION, TokenType.SUBSTRACT, TokenType.MULTIPLICATION, TokenType.DIVISION -> {
                    while ( stack.isNotEmpty() && stack.last().precedence() >= token.precedence() )
                        postfix.add(stack.removeLast())
                    stack.add(token)
                }

                else -> break

            }
        }

        return postfix
    }

}
