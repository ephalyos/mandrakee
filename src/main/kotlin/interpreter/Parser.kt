
package interpreter

class Parser (
    private val tokens: List<Token>
) {

    private val eofT = Token(lexeme = "", type = TokenType.EOF)
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
    private val greaterThanT = Token(lexeme = ">", type = TokenType.GREATER_THAN)
    private val greaterEqualThanT = Token(lexeme = ">=", type = TokenType.GREATER_EQUAL_THAN)
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
    private var current = Token(lexeme = "", type = TokenType.EOF)

    fun parse () {
        current = tokens[i]
        program()
        if (current != eofT)
            println("Invalid program")
    }

    private fun match (t: Token) {
        if (current.type == t.type){
            i++
            current = tokens[i]
        } else {
            throw Error("Error at ${current.line} expected a ${t.type.name}")
        }
    }

    private fun program () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT, stringT, identifierT, leftParenthesesT, superT,
            forT, ifT, printT, returnT, whileT, leftBraceT, classT, varT, funT -> {
                declaration()
            }
             else -> {
                 throw Error("Error at ${current.line} expected DECLARATION")
             }
        }
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
            match(leftBraceT)
            functions()
            match(rightBraceT)
        } else {
            throw Error("Error at ${current.line} expected CLASS")
        }
    }

    private fun classInher () {
        if (current == lessThanT){
            match(lessThanT)
            match(identifierT)
        }
    }

    private fun funDecl () {
        if (current == funT) {
            match(funT)
            function()
        } else {
            throw Error("Error at ${current.line} expected FUNCTION")
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
                throw Error("Error at ${current.line} expected STATEMENT")
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
                throw Error("Error at ${current.line} expected EXPRESSION")
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
                throw Error("Error at ${current.line} expected FOR STATEMENT 1")
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
                throw Error("Error at ${current.line} expected FOR STATEMENT 2")
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
            throw Error("Error at ${current.line} expected IF")
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
            throw Error("Error at ${current.line} expected PRINT")
        }
    }

    private fun returnStatement () {
        if (current == returnT) {
            match(returnT)
            returnExpressionOptional()
            match(semicolonT)
        } else {
            throw Error("Error at ${current.line} expected RETURN")
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
            throw Error("Error at ${current.line} expected WHILE")
        }
    }

    private fun block () {
        if (current == leftBraceT) {
            match(leftBraceT)
            blockDecl()
            match(rightBraceT)
        } else {
            throw Error("Error at ${current.line} expected BLOCK")
        }
    }

    private fun blockDecl () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT, stringT, identifierT, leftParenthesesT, superT,
            forT, ifT, printT, returnT, whileT, leftBraceT, classT, varT, funT -> {
                declaration()
                blockDecl()
            }
        }
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
                throw Error("Error at ${current.line} expected ASSIGNMENT")
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
                throw Error("Error at ${current.line} expected LOGIC_OR")
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
                throw Error("Error at ${current.line} expected LOGIC_AND")
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
                throw Error("Error at ${current.line} expected EQUALITY")
            }
        }
    }

    private fun equality2 () {
        when (current) {
            notEqualsT -> {
                match(notEqualsT)
                comparison()
                equality2()
            }
            equalsT -> {
                match(equalsT)
                comparison()
                equality2()
            }
        }
    }

    private fun comparison () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                term()
                comparison2()
            }
            else -> {
                throw Error("Error at ${current.line} expected COMPARISON")
            }
        }
    }

    private fun comparison2 () {
        when (current) {
            greaterThanT -> {
                match(greaterThanT)
                term()
                comparison2()
            }
            greaterEqualThanT -> {
                match(greaterEqualThanT)
                term()
                comparison2()
            }
            lessThanT -> {
                match(lessThanT)
                term()
                comparison2()
            }
            lessEqualThanT -> {
                match(lessEqualThanT)
                term()
                comparison2()
            }
        }
    }

    private fun term () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                factor()
                term2()
            }
            else -> {
                throw Error("Error at ${current.line} expected TERM")
            }
        }
    }

    private fun term2 () {
        when (current) {
            minusT -> {
                match(minusT)
                factor()
                term2()
            }
            plusT -> {
                match(plusT)
                factor()
                term2()
            }
        }
    }

    private fun factor () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                unary()
                factor2()
            }
            else -> {
                throw Error("Error at ${current.line} expected FACTOR")
            }
        }
    }

    private fun factor2 () {
        when (current) {
            divisionT -> {
                match(divisionT)
                unary()
                factor2()
            }
            multiplicationT -> {
                match(multiplicationT)
                unary()
                factor2()
            }
        }
    }

    private fun unary () {
        when (current) {
            notT -> {
                match(notT)
                unary()
            }
            minusT -> {
                match(minusT)
                unary()
            }
            trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                call()
            }
            else -> {
                throw Error("Error at ${current.line} expected UNARY")
            }
        }
    }

    private fun call () {
        when (current) {
            trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                primary()
                call2()
            }
            else -> {
                throw Error("Error at ${current.line} expected CALL")
            }
        }
    }

    private fun call2 () {
        when (current) {
            leftParenthesesT -> {
                match(leftParenthesesT)
                argumentsOptional()
                match(rightParenthesesT)
                call2()
            }
            dotT -> {
                match(dotT)
                match(identifierT)
                call2()
            }
        }
    }

//    private fun callOptional () {
//        when (current) {
//            trueT, falseT, nullT, thisT, integerT, doubleT,
//            stringT, identifierT, leftParenthesesT, superT-> {
//                call()
//                match(dotT)
//            }
//        }
//    }

    private fun primary () {
        when (current) {
            trueT -> match(trueT)
            falseT -> match(falseT)
            nullT -> match(nullT)
            thisT -> match(thisT)
            integerT -> match(integerT)
            doubleT -> match(doubleT)
            stringT -> match(stringT)
            identifierT -> match(identifierT)
            leftParenthesesT -> {
                match(leftParenthesesT)
                expression()
                match(rightParenthesesT)
            }
            superT -> {
                match(superT)
                match(dotT)
                match(identifierT)
            }
            else -> {
                throw Error("Error at ${current.line} expected PRIMARY")
            }
        }
    }

    // OTHERS  - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun function () {
        if (current == identifierT) {
            match(identifierT)
            match(leftParenthesesT)
            parametersOptional()
            match(rightParenthesesT)
            block()
        } else {
            throw Error("Error at ${current.line} expected FUNCTION")
        }
    }

    private fun functions () {
        if (current == identifierT) {
            function()
            functions()
        }
    }

    private fun parametersOptional () {
        if (current == identifierT) {
            parameters()
        }
    }

    private fun parameters () {
        if (current == identifierT) {
            match(identifierT)
            parameters2()
        } else {
            throw Error("Error at ${current.line} expected PARAMETERS")
        }
    }

    private fun parameters2 () {
        if (current == commaT) {
            match(commaT)
            match(identifierT)
            parameters2()
        }
    }

    private fun argumentsOptional () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                arguments()
            }
        }
    }

    private fun arguments () {
        when (current) {
            notT, minusT, trueT, falseT, nullT, thisT, integerT, doubleT,
            stringT, identifierT, leftParenthesesT, superT-> {
                expression()
                arguments2()
            }
            else -> {
                throw Error("Error at ${current.line} expected ARGUMENTS")
            }
        }
    }

    private fun arguments2 () {
        if (current == commaT) {
            match(commaT)
            expression()
            arguments2()
        }
    }

}