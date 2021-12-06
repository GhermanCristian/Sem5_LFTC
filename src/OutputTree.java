import java.util.List;

public class OutputTree {
    private Node root;
    private final Grammar grammar;
    private final List<Integer> sequenceProductionCodes;

    public OutputTree(Grammar grammar, List<Integer> sequenceProductionCodes) {
        this.grammar = grammar;
        this.sequenceProductionCodes = sequenceProductionCodes;
        Pair<String, List<String>> initialProduction = grammar.getProductionCodes().get(sequenceProductionCodes.get(0));
        this.root = new Node(initialProduction.getFirst(), null, null);
        this.root.setLeftChild(this.buildTreeRecursively(initialProduction.getSecond()));
    }

    private Node buildTreeRecursively(List<String> nextSymbols) {
        String currentSymbol = nextSymbols.get(0);
        nextSymbols.remove(0);
        Node currentNode = new Node(currentSymbol, null, null);
        currentNode.setRightSibling(this.buildTreeRecursively(nextSymbols));

        if (this.grammar.getNonterminals().contains(currentSymbol)) {

        }

        return currentNode;
    }
}
