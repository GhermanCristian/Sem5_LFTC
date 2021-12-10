import java.util.List;

public class OutputTree {
    private final Node root;
    private final Grammar grammar;
    private final List<Integer> sequenceProductionCodes;
    private int nodeIndexInTree;

    public OutputTree(Grammar grammar, List<Integer> sequenceProductionCodes) {
        this.grammar = grammar;
        this.sequenceProductionCodes = sequenceProductionCodes;
        this.nodeIndexInTree = 0;

        Pair<String, List<String>> initialProduction = grammar.getProductionCodes().get(sequenceProductionCodes.get(0));
        this.root = new Node(initialProduction.getFirst(), null, null);
        this.root.setLeftChild(this.buildTreeRecursively(initialProduction.getSecond()));
    }

    private Node buildTreeRecursively(List<String> nextSymbols) {
        if (this.nodeIndexInTree >= this.sequenceProductionCodes.size() || nextSymbols.isEmpty()) {
            return null;
        }

        String currentSymbol = nextSymbols.get(0);
        nextSymbols.remove(0);
        Node currentNode = new Node(currentSymbol, null, null);
        currentNode.setRightSibling(this.buildTreeRecursively(nextSymbols));

        if (this.grammar.getNonterminals().contains(currentSymbol)) {
            int currentProductionCode = this.sequenceProductionCodes.get(this.nodeIndexInTree);
            this.nodeIndexInTree++;
            List<String> productionRHS = this.grammar.getProductionCodes().get(currentProductionCode).getSecond();
            currentNode.setLeftChild(this.buildTreeRecursively(productionRHS));
        }

        return currentNode;
    }
}
