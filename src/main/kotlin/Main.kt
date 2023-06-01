
fun main (args: Array<String>) {

    println("args size: ${args.size}")
    println(args.joinToString(" "))

    val lines = listOf(
        "var( value( = 0 // line-comment, should not see any of this\n",
        "this val /* should not see any of this */ val new += 1\n",
        "13 13e14 13e+14 13e-14 13.14 13.14e15 13.14e+15 13.14e-15\n"
    )

    val scanner = Scanner()

    val tokens = scanner.scan(lines)

    for (token in tokens) {
        println(token.toString())
    }

}