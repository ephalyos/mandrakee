
package interpreter
import java.io.File

fun main (args: Array<String>) {

    if (args.isEmpty()) {

        while ( true ) {

            // Line Reading
            print(">> ")
            val line = readln()
            if ( line == "exit()") break

            // Infix Tokens
            val tokens = Scanner(listOf(line)).scan()

            // Parser Check
            Parser(tokens).parse()

            // Postfix Tokens
            val postfix = Postfix(tokens).toPostfix()

            // AST Creation
            val root = AST(postfix).toAST()

            // Execution
//             root.printChildren()
            root.run()

        }

    } else {

        // File Reading
        val input: List<String> = File(args[0]).readLines()

        // Infix Tokens
        val tokens = Scanner(input).scan()

        // Parser Check
        Parser(tokens).parse()

        // Postfix Tokens
        val postfix = Postfix(tokens).toPostfix()

        // AST Creation
        val root = AST(postfix).toAST()

        // Execution
//        root.printChildren()
        root.run()

    }

}
