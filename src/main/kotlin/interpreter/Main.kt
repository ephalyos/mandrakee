
package interpreter
import java.io.File

fun main (args: Array<String>) {

    if (args.isEmpty()) {

        // " if (a == 5) { b = 6; } else { c = 8; } var b = 7; var c; "
        // " if (true) { a = 5; } "
        // " var nombre = \"Carlos\"; var apellido = \"Herrera\"; print nombre + \" \" + apellido; "
        // " print \"el área es: \"; \n print area; "
        // " if (area > 100 and radius < 50) { print \"valid\"; } else { print \"invalid\"; } "
        // " while ( init <= lim ) { print fib; aux = aux + fib; } print \"finished\"; "
        // " for ( init = 1 ; init <= lim ; init = init + 1 ) { print fib; aux = aux + fib; } print \"finished\"; "

        val input = " while ( init <= lim ) { print fib; aux = aux + fib; } print \"finished\"; "

        val tokens = Scanner(listOf(input)).scan()
        val postfix = Postfix(tokens).toPostfix()

        val root = AST(postfix).toAST()
        root.printChildren()

    } else {

        // File Reading
        val input: List<String> = File(args[0]).readLines()

        // Scanner creation
        val scanner = Scanner(input)
        val tokens = scanner.scan()
        for (token in tokens) println(token.toString())

        // Parser creation
        val parser = Parser(tokens)
        parser.parse()

    }

}
