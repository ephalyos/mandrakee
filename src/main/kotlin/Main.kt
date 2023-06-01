
import java.io.File

fun main (args: Array<String>) {

    val scanner = Scanner()

    if (args.isEmpty()) {
        while (true) {
            print(">> ")
            val line = readln()
            if (line == "exit()") break
            val tokens = scanner.scan(listOf(line))
            for (token in tokens) {
                println(token.toString())
            }
        }
    } else {
        val input: List<String> = File(args[0]).readLines()
        val tokens = scanner.scan(input)
        for (token in tokens) {
            println(token.toString())
        }
    }

}