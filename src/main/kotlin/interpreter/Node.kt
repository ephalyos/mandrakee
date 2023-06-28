
package interpreter

class Node<T> (
    val value: T,
) {

    val children = mutableListOf<Node<T>>()

    fun addChild ( node: Node<T> ) {
        children.add(node)
    }

    fun addChildren (children: List<Node<T>> ) {
        for ( child in children ) {
            addChild(child)
        }
    }

    fun printChildren ( depth: Int = 0) {
        for (child in children) {
            val spaces = "--".repeat(depth)
            println("$spaces${child.value}")
            child.printChildren(depth + 1)
        }
    }

    override fun toString(): String = "$value"

}
