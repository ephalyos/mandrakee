
package interpreter
import java.io.File

fun main (args: Array<String>) {

    val scanner = Scanner()

    if (args.isEmpty()) {
        while (true) {
            print(">> ")
            val line = readln()
            if (line == "exit()") break
            val tokens = scanner.scan(listOf(line))
            val parser = Parser(tokens)
            parser.parse()
        }
    } else {
        val input: List<String> = File(args[0]).readLines()
        val tokens = scanner.scan(input)
        for (token in tokens) {
            println(token.toString())
        }
        val parser = Parser(tokens)
        parser.parse()
    }

}