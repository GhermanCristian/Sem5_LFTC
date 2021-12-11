import java.util.ArrayList;
import java.util.List;

public class OutputTree {
    private final Node root;
    private final Grammar grammar;
    private final List<Integer> sequenceProductionCodes;
    private int currentProductionCodeIndex;

    public OutputTree(Grammar grammar, List<Integer> sequenceProductionCodes) {
        this.grammar = grammar;
        this.sequenceProductionCodes = sequenceProductionCodes;
        this.currentProductionCodeIndex = 1; // because production 0 is already used when setting the left child of the root

        Pair<String, List<String>> initialProduction = grammar.getProductionCodes().get(sequenceProductionCodes.get(0));
        this.root = new Node(initialProduction.getFirst(), null, null);
        this.root.setLeftChild(this.buildTreeRecursively(initialProduction.getSecond()));
    }

    private Node buildTreeRecursively(List<String> nextSymbolsOriginal) {
        if (this.currentProductionCodeIndex == this.sequenceProductionCodes.size() && nextSymbolsOriginal.size() == 1 && nextSymbolsOriginal.get(0).equals(Constants.EPSILON)) {
            return new Node(Constants.EPSILON, null, null);
        }
        else if (nextSymbolsOriginal.isEmpty() || this.currentProductionCodeIndex >= this.sequenceProductionCodes.size()) {
            return null;
        }

        List<String> nextSymbols = new ArrayList<>(nextSymbolsOriginal);
        int currentProductionCodeIndexCopy = this.currentProductionCodeIndex; // because the field gets modified by the other recursive calls
        String currentSymbol = nextSymbols.remove(0);
        Node currentNode = new Node(currentSymbol, null, null);

        if (this.grammar.getNonterminals().contains(currentSymbol)) {
            this.currentProductionCodeIndex++;
            int currentProductionCode = this.sequenceProductionCodes.get(currentProductionCodeIndexCopy);
            List<String> productionRHS = this.grammar.getProductionCodes().get(currentProductionCode).getSecond();
            currentNode.setLeftChild(this.buildTreeRecursively(productionRHS));
        }
        currentNode.setRightSibling(this.buildTreeRecursively(nextSymbols));

        return currentNode;
    }

    public void printTree() {
        List<Node> stack = new ArrayList<>();
        stack.add(this.root);

        while (!stack.isEmpty()) {
            Node currentNode = stack.remove(stack.size() - 1);
            System.out.printf("node: %s; left: %s; right: %s\n", currentNode, currentNode.getLeftChild(), currentNode.getRightSibling());
            if (currentNode.getLeftChild() != null) {
                stack.add(currentNode.getLeftChild());
            }
            if (currentNode.getRightSibling() != null) {
                stack.add(currentNode.getRightSibling());
            }
        }
    }
}
