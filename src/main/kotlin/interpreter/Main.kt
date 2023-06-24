
package interpreter
import java.io.File

fun main (args: Array<String>) {

    if (args.isEmpty()) {
        while (true) {
            // Prompting
            print(">> ")
            val line = readln()
            if (line == "exit()") break
            // Scanner creation
            val scanner = Scanner(listOf(line))
            val tokens = scanner.scan()
            for (token in tokens) println(token.toString())
            // Parser creation
            val parser = Parser(tokens)
            parser.parse()
        }
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
