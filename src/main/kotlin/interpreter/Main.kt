
package interpreter
import java.io.File

fun main (args: Array<String>) {

    if (args.isEmpty()) {

        /*
            "if (movie.isNational == true or (movie.isInternational == true and movie.rating > 67)) ;"
            "if (a == 5) { \n b = 6; c = 7; \n } else { \n b = 5; \n }"
            "fun main () { print 2+2; } "
            "var mensaje = \"holis bolis\""
            "(3 + 4) * (12 % 4) / 7 ;"
        */

        val input = " if (a == 5) { b = 6; } else { c = 8; } var b = 7; "
        val tokens = Scanner(listOf(input)).scan()
        val postfix = Postfix(tokens).toPostfix()

        println(" --- POSTFIX --- ")
        for (token in postfix) println(token.toString())


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
