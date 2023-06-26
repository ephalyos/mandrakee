
package interpreter

class Node<T> (
    val value: T,
    var depth: Int = 0,
) {

    val children = mutableListOf<Node<T>>()

    fun addChild ( node: Node<T> ) {
        node.depth = depth + 1
        children.add(node)
    }

    fun getChild () {
        for (child in children) {
            val spaces = "--".repeat(child.depth)
            println("$spaces${child.value}")
            child.getChild()
        }
    }

}
