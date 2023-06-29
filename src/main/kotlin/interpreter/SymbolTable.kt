package interpreter

class SymbolTable {
    companion object {

        private val table = mutableMapOf<String, Any>()
        fun retrieve ( key: String ): Any? {
            if (table.contains(key))
                return table[key]
            return null
        }

        fun update ( key: String, value: Any ) {
            if (table.contains(key))
                table[key] = value
            else
                throw Error("Identifier $key is undefined")
        }

        fun add ( key: String, value: Any ) {
            if (table.contains(key))
                throw Error("Identifier $key is already defined")
            else
                table[key] = value
        }

    }
}