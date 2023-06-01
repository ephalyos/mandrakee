import kotlin.properties.Delegates

class Parser (
    private val tokens: List<Token>
) {

    val eolT = Token(lexeme = "", type = TokenType.EOF)
    val classT = Token(lexeme = "class", type = TokenType.CLASS)
    val identifierT = Token(lexeme = "", type = TokenType.IDENTIFIER)
    val funT = Token(lexeme = "fun", type = TokenType.FUN)
    val varT = Token(lexeme = "var", type = TokenType.VAR)
    val assignT = Token(lexeme = "=", type = TokenType.ASSIGN)
    val forT = Token(lexeme = "for", type = TokenType.FOR)
    val leftParenthesesT = Token(lexeme = "(", type = TokenType.LEFT_PARENTHESES)
    val rightParenthesesT = Token(lexeme = ")", type = TokenType.RIGHT_PARENTHESES)
    val semicolonT = Token(lexeme = ";", type = TokenType.SEMI_COLON)
    val ifT = Token(lexeme = "if", type = TokenType.IF)
    val elseT = Token(lexeme = "else", type = TokenType.ELSE)
    val printT = Token(lexeme = "print", type = TokenType.PRINT)
    val returnT = Token(lexeme = "return", type = TokenType.RETURN)
    val whileT = Token(lexeme = "while", type = TokenType.WHILE)
    val leftBraceT = Token(lexeme = "{", type = TokenType.LEFT_BRACE)
    val rightBraceT = Token(lexeme = "}", type = TokenType.RIGHT_BRACE)
    val orT = Token(lexeme = "or", type = TokenType.OR)
    val andT = Token(lexeme = "and", type = TokenType.AND)
    val notEqualsT = Token(lexeme = "!=", type = TokenType.NOT_EQUALS)
    val equalsT = Token(lexeme = "and", type = TokenType.EQUALS)
    val moreThanT = Token(lexeme = ">", type = TokenType.GREATER_THAN)
    val moreEqualThanT = Token(lexeme = ">=", type = TokenType.GREATER_EQUAL_THAN)
    val lessThanT = Token(lexeme = "<", type = TokenType.LESSER_THAN)
    val lessEqualThanT = Token(lexeme = "<=", type = TokenType.LESSER_EQUAL_THAN)
    val minusT = Token(lexeme = "-", type = TokenType.SUBSTRACT)
    val plusT = Token(lexeme = "+", type = TokenType.ADDITION)
    val divisionT = Token(lexeme = "/", type = TokenType.DIVISION)
    val multiplicationT = Token(lexeme = "*", type = TokenType.MULTIPLICATION)
    val notT = Token(lexeme = "!", type = TokenType.NOT)
    val dotT = Token(lexeme = ".", type = TokenType.DOT)
    val trueT = Token(lexeme = "true", type = TokenType.TRUE)
    val falseT = Token(lexeme = "false", type = TokenType.FALSE)
    val nullT = Token(lexeme = "null", type = TokenType.NIL)
    val thisT = Token(lexeme = "this", type = TokenType.THIS)
    val integerT = Token(lexeme = "", type = TokenType.INTEGER)
    val doubleT = Token(lexeme = "", type = TokenType.DOUBLE)
    val stringT = Token(lexeme = "", type = TokenType.STRING)
    val superT = Token(lexeme = "super", type = TokenType.SUPER)
    val commaT = Token(lexeme = ",", type = TokenType.COMMA)

    var i = 0
    var errorFound = false
    var current = Token(null, 0, "", TokenType.EOF)

    fun parse () {
        current = tokens[i]
        program()
    }

    fun match (t: Token) {
        if (errorFound) return
        if (current.type == t.type){
            i++
            current = tokens[i]
        } else {
            errorFound = true
            throw Error("Error at ${current.line} expected a ${t.type.name}")
        }
    }

    fun program () {
        if (current.equals(classT) || current.equals(funT) || current.equals(varT)) {
            declaration()
        }
    }

    fun declaration () {

    }

}