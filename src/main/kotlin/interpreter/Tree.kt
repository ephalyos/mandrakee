
package interpreter

class Node (
    val token: Token
) {
    val children = mutableListOf<Node>()
    fun addChild ( child: Node ) {
        children.add(child)
    }
}

class Tree (
    val node: Node
) {

}
