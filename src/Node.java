public class Node {
    private final String info;
    private Node leftChild;
    private Node rightSibling;

    public Node(String info, Node leftChild, Node rightSibling) {
        this.info = info;
        this.leftChild = leftChild;
        this.rightSibling = rightSibling;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightSibling(Node rightSibling) {
        this.rightSibling = rightSibling;
    }

    @Override
    public String toString() {
        return this.info;
    }
}
