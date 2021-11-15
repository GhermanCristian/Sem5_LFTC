import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    private final String ELEMENT_SEPARATOR = ";";
    private final String TRANSITION_SEPARATOR = "`";

    // LL1
    private List<String> nonterminals;
    private List<String> terminals;
    private List<Node> transitions;
    private String startingSymbol;

    private int getFirstPositionOfNode(String state) {
        for (Node node: this.transitions) {
            if (node.info.equals(state)) {
                return node.index;
            }
        }
        return -1;
    }

    private void loadFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            this.nonterminals = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.terminals = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.startingSymbol = scanner.nextLine();

            this.transitions = new ArrayList<>();
            this.transitions.add(new Node(0, this.startingSymbol, 0, 0)); // root
            //TODO - load transitions
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Grammar(String filePath) {
        this.loadFromFile(filePath);
    }

    public List<String> getNonterminals() {
        return this.nonterminals;
    }

    public List<String> getTerminals() {
        return this.terminals;
    }

    public List<Node> getTransitions() {
        return this.transitions;
    }

    public String getStartingSymbol() {
        return this.startingSymbol;
    }
}
