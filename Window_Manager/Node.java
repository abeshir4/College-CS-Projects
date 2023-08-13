//TODO: Add JavaDocs ONLY. No other Editing.

/**
 *  A Node class.
 * @author Katherine Russell
 * @param <T> generic of type T
 */
class Node<T> {
    /**
     * The data inside the node.
     */
    public T data;
    /**
     * The reference to the next node.
     */
    public Node<T> next;
    /**
     * The reference to the previous node.
     */
    public Node<T> prev;

    /**
     * constructor referring to the Node.
     */

    public Node() {

    }

    /**
     * constructor referring to the Node.
     * @param data The data value to be initialized in the Node
     */

    public Node(T data) {

        this.data = data;
    }
}
