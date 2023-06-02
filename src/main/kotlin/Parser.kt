
class Parser (
    private val tokens: List<Token>
) {

    private val eolT = Token(lexeme = "", type = TokenType.EOF)
    private val classT = Token(lexeme = "class", type = TokenType.CLASS)
    private val identifierT = Token(lexeme = "", type = TokenType.IDENTIFIER)
    private val funT = Token(lexeme = "fun", type = TokenType.FUN)
    private val varT = Token(lexeme = "var", type = TokenType.VAR)
    private val assignT = Token(lexeme = "=", type = TokenType.ASSIGN)
    private val forT = Token(lexeme = "for", type = TokenType.FOR)
    private val leftParenthesesT = Token(lexeme = "(", type = TokenType.LEFT_PARENTHESES)
    private val rightParenthesesT = Token(lexeme = ")", type = TokenType.RIGHT_PARENTHESES)
    private val semicolonT = Token(lexeme = ";", type = TokenType.SEMI_COLON)
    private val ifT = Token(lexeme = "if", type = TokenType.IF)
    private val elseT = Token(lexeme = "else", type = TokenType.ELSE)
    private val printT = Token(lexeme = "print", type = TokenType.PRINT)
    private val returnT = Token(lexeme = "return", type = TokenType.RETURN)
    private val whileT = Token(lexeme = "while", type = TokenType.WHILE)
    private val leftBraceT = Token(lexeme = "{", type = TokenType.LEFT_BRACE)
    private val rightBraceT = Token(lexeme = "}", type = TokenType.RIGHT_BRACE)
    private val orT = Token(lexeme = "or", type = TokenType.OR)
    private val andT = Token(lexeme = "and", type = TokenType.AND)
    private val notEqualsT = Token(lexeme = "!=", type = TokenType.NOT_EQUALS)
    private val equalsT = Token(lexeme = "==", type = TokenType.EQUALS)
    private val moreThanT = Token(lexeme = ">", type = TokenType.GREATER_THAN)
    private val moreEqualThanT = Token(lexeme = ">=", type = TokenType.GREATER_EQUAL_THAN)
    private val lessThanT = Token(lexeme = "<", type = TokenType.LESSER_THAN)
    private val lessEqualThanT = Token(lexeme = "<=", type = TokenType.LESSER_EQUAL_THAN)
    private val minusT = Token(lexeme = "-", type = TokenType.SUBSTRACT)
    private val plusT = Token(lexeme = "+", type = TokenType.ADDITION)
    private val divisionT = Token(lexeme = "/", type = TokenType.DIVISION)
    private val multiplicationT = Token(lexeme = "*", type = TokenType.MULTIPLICATION)
    private val notT = Token(lexeme = "!", type = TokenType.NOT)
    private val dotT = Token(lexeme = ".", type = TokenType.DOT)
    private val trueT = Token(lexeme = "true", type = TokenType.TRUE)
    private val falseT = Token(lexeme = "false", type = TokenType.FALSE)
    private val nullT = Token(lexeme = "null", type = TokenType.NIL)
    private val thisT = Token(lexeme = "this", type = TokenType.THIS)
    private val integerT = Token(lexeme = "", type = TokenType.INTEGER)
    private val doubleT = Token(lexeme = "", type = TokenType.DOUBLE)
    private val stringT = Token(lexeme = "", type = TokenType.STRING)
    private val superT = Token(lexeme = "super", type = TokenType.SUPER)
    private val commaT = Token(lexeme = ",", type = TokenType.COMMA)

    private var i = 0
    private var errorFound = false
    private var current = Token(null, 0, "", TokenType.EOF)

    fun parse () {
        current = tokens[i]
        program()
    }

    private fun match (t: Token) {
        if (errorFound) return
        if (current.type == t.type){
            i++
            current = tokens[i]
        } else {
            errorFound = true
            throw Error("Error at ${current.line} expected a ${t.type.name}")
        }
    }

    private fun program () {
        declaration()
    }

    // DECLARATIONS - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun declaration () {
        when (current) {
            classT -> {
                classDecl()
                declaration()
            }
            funT -> {
                funDecl()
                declaration()
            }
            varT -> {
                varDecl()
                declaration()
            }
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT, stringT, identifierT, leftParenthesesT, superT,
            forT, ifT, printT, returnT, whileT, leftBraceT-> {
                statement()
                declaration()
            }
        }
    }

    private fun classDecl () {
        if (current == classT) {
            match(classT)
            match(identifierT)
            classInher()
            match(leftParenthesesT)
            functions()
            match(rightParenthesesT)
        } else {
            throw Error("Error at ${current.line} expected CLASS")
        }
    }

    private fun classInher () {
        if (current == lessThanT){
            match(lessThanT)
            match(identifierT)
        } else {
            throw Error("Error at ${current.line} expected IDENTIFIER")
        }
    }

    private fun funDecl () {
        if (current == funT) {
            match(funT)
            function()
        }
    }

    private fun varDecl () {
        if (current == varT) {
            match(varT)
            match(identifierT)
            varInit()
            match(semicolonT)
        } else {
            throw Error("Error at ${current.line} expected VAR")
        }
    }

    private fun varInit () {
        if (current == assignT) {
            match(assignT)
            expression()
        }
    }

    // STATEMENTS - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun statement () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT -> {
                expressionStatement()
            }
            forT -> {
                forStatement()
            }
            ifT -> {
               ifStatement()
            }
            printT -> {
                printStatement()
            }
            returnT -> {
                returnStatement()
            }
            whileT -> {
                whileStatement()
            }
            leftBraceT -> {
                block()
            }
            else -> {
                throw Error("Error at ${current.line}")
            }
        }
    }

    private fun expressionStatement () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT -> {
                expression()
                match(semicolonT)
            }
            else -> {
                throw Error("Error at ${current.line}")
            }
        }
    }

    private fun forStatement () {
        if (current == forT) {
            match(forT)
            match(leftParenthesesT)
            forStatement1()
            forStatement2()
            forStatement3()
            match(rightParenthesesT)
            statement()
        } else {
            throw Error("Error at ${current.line} expected FOR")
        }
    }

    private fun forStatement1 () {
        when (current) {
            varT -> {
                varDecl()
            }
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                expressionStatement()
            }
            semicolonT -> {
                match(semicolonT)
            }
            else -> {
                throw Error("Error at ${current.line} expected VARIABLE | EXPRESSION")
            }
        }
    }

    private fun forStatement2 () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                expression()
                match(semicolonT)
            }
            semicolonT -> {
                match(semicolonT)
            }
            else -> {
                throw Error("Error at ${current.line} expected ;")
            }
        }
    }

    private fun forStatement3 () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT -> {
                expression()
            }
        }
    }

    private fun ifStatement () {
        if (current == ifT) {
            match(ifT)
            match(leftParenthesesT)
            expression()
            match(rightParenthesesT)
            statement()
            elseStatement()
        } else {
            throw Error("Error at ${current.line} expected IF EXPRESSION")
        }
    }

    private fun elseStatement () {
        if ( current == elseT) {
            match(elseT)
            statement()
        }
    }

    private fun printStatement () {
        if (current == printT) {
            match(printT)
            expression()
            match(semicolonT)
        } else {
            throw Error("Error at ${current.line} expected PRINT EXPRESSION")
        }
    }

    private fun returnStatement () {
        if (current == returnT) {
            match(returnT)
            returnExpressionOptional()
            match(semicolonT)
        } else {
            throw Error("Error at ${current.line} expected RETURN EXPRESSION")
        }
    }

    private fun returnExpressionOptional () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                expression()
            }
        }
    }

    private fun whileStatement () {
        if (current == whileT) {
            match(whileT)
            match(leftParenthesesT)
            expression()
            match(rightParenthesesT)
            statement()
        } else {
            throw Error("Error at ${current.line} expected WHILE EXPRESSION")
        }
    }

    private fun block () {
        if (current == leftBraceT) {
            match(leftBraceT)
            blockDecl()
            match(rightBraceT)
        } else {
            throw Error("Error at ${current.line} expected {")
        }
    }

    private fun blockDecl () {
        declaration()
        blockDecl()
    }

    // EXPRESSIONS  - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun expression () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                assignment()
            }
            else -> {
                throw Error("Error at ${current.line} expected EXPRESSION")
            }
        }
    }

    private fun assignment () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                logicOr()
                assignmentOptional()
            }
            else -> {
                throw Error("Error at ${current.line} expected EXPRESSION")
            }
        }
    }

    private fun logicOr () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                logicAnd()
                logicOr2()
            }
            else -> {
                throw Error("Error at ${current.line} expected EXPRESSION")
            }
        }
    }

    private fun assignmentOptional () {
        if (current == assignT) {
            match(assignT)
            expression()
        }
    }

    private fun logicOr2 () {
        if (current == orT) {
            match(orT)
            logicAnd()
            logicOr2()
        }
    }

    private fun logicAnd () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                equality()
                logicAnd2()
            }
            else -> {
                throw Error("Error at ${current.line} expected EXPRESSION")
            }
        }
    }

    private fun logicAnd2 () {
        if (current == andT) {
            match(andT)
            equality()
            logicAnd2()
        }
    }

    private fun equality () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                comparison()
                equality2()
            }
            else -> {
                throw Error("Error at ${current.line} expected EXPRESSION")
            }
        }
    }

}