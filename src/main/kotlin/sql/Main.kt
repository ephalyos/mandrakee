
package sql

fun main () {

    val scanner = Scanner()

    while (true) {
        print(">> ")
        val line = readln()
        if (line == "exit()") break
        val tokens = scanner.scan(line)
        for (token in tokens) {
            println(token.toString())
        }
    }

}
