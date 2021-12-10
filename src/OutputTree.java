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
        this.currentProductionCodeIndex = 0;

        Pair<String, List<String>> initialProduction = grammar.getProductionCodes().get(sequenceProductionCodes.get(0));
        this.root = new Node(initialProduction.getFirst(), null, null);
        this.root.setLeftChild(this.buildTreeRecursively(initialProduction.getSecond()));
    }

    private Node buildTreeRecursively(List<String> nextSymbolsOriginal) {
        if (nextSymbolsOriginal.size() == 1 && nextSymbolsOriginal.get(0).equals(Constants.EPSILON)) {
        }
        else if (nextSymbolsOriginal.isEmpty() || this.currentProductionCodeIndex >= this.sequenceProductionCodes.size() - 1) {
            return null;
        }

        List<String> nextSymbols = new ArrayList<>(nextSymbolsOriginal);
        int currentProductionCodeIndexCopy = this.currentProductionCodeIndex; // because the field gets modified by the other recursive calls
        String currentSymbol = nextSymbols.get(0);
        nextSymbols.remove(0);
        Node currentNode = new Node(currentSymbol, null, null);
        currentNode.setRightSibling(this.buildTreeRecursively(nextSymbols));

        if (this.grammar.getNonterminals().contains(currentSymbol)) {
            int currentProductionCode = this.sequenceProductionCodes.get(currentProductionCodeIndexCopy);
            this.currentProductionCodeIndex++;
            List<String> productionRHS = this.grammar.getProductionCodes().get(currentProductionCode).getSecond();
            currentNode.setLeftChild(this.buildTreeRecursively(productionRHS));
        }

        return currentNode;
    }

    public void printTree() {
        List<Node> queue = new ArrayList<>();
        queue.add(this.root);

        while (!queue.isEmpty()) {
            Node currentNode = queue.remove(0);
            System.out.print(currentNode + " -- ");
            if (currentNode.getRightSibling() != null) {
                queue.add(currentNode.getRightSibling());
            }
            if (currentNode.getLeftChild() != null) {
                queue.add(currentNode.getLeftChild());
            }
        }
    }
}
