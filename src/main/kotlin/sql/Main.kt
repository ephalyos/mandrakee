
package sql

/*
    select distinct select campo1, campo2, campo1 from tabla1
    select * from tabla1
    select * from tabla1, tabla2, from tabla3
    select * from tabla1, tabla2 from tabla3
    select * from tabla1 a, tabla2 b tabla3 c, tabla 4 d
    select campo1, campo2 from tabla1
    select campo1, campo2, from tabla1
    select tabla1.campo1, tabla1.campo2 from tabla1
    select tabla1.campo1, tabla1.campo2, from tabla1
 */

fun main () {

    val scanner = Scanner()

    while (true) {
        print(">> ")
        val line = readln()
        if (line == "exit()") break
        val tokens = scanner.scan(line)
        val parser = LRParser(tokens)
        parser.parse()
    }

}
