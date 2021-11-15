public class Node {
    public final int index;
    public final String info;
    public final int parent;
    public final int leftSibling;

    public Node(int index, String info, int parent, int leftSibling) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.leftSibling = leftSibling;
    }

    @Override
    public String toString() {
        return "Node{" +
                "index=" + index +
                ", info='" + info + '\'' +
                ", parent=" + parent +
                ", leftSibling=" + leftSibling +
                '}';
    }
}
