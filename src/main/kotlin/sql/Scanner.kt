
package sql

import TokenType

class Scanner {

    val reserved = hashMapOf(
        "select" to TokenType.SELECT,
        "from" to TokenType.FROM,
        "distinct" to TokenType.DISTINCT,

    )

}
