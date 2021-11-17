import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private final String ELEMENT_SEPARATOR = ";";
    private final String TRANSITION_SEPARATOR = "`";
    private final String INSIDE_TRANSITION_SEPARATOR = " ";
    private final String EPSILON = "EPS";

    // LL1
    private List<String> nonterminals;
    private List<String> terminals;
    private Map<String, Set<List<String>>> productions;
    private String startingSymbol;
    private boolean isCFG;

    private void loadFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            this.nonterminals = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.terminals = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.startingSymbol = scanner.nextLine();

            this.productions = new HashMap<>();
            while (scanner.hasNextLine()) {
                String[] splitProductions = scanner.nextLine().split(this.TRANSITION_SEPARATOR);
                String leftHandSide = splitProductions[0];
                this.productions.putIfAbsent(leftHandSide, new HashSet<>());
                for (int i = 1; i < splitProductions.length; i++) {
                    this.productions.get(leftHandSide).add(Arrays.stream(splitProductions[i].split(this.INSIDE_TRANSITION_SEPARATOR)).collect(Collectors.toList()));
                }
            }

            this.isCFG = this.checkIfCFG();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfCFG() {
        if (! this.nonterminals.contains(this.startingSymbol)) {
            return false;
        }

        for (String leftHandSide : this.productions.keySet()) {
            if (! this.nonterminals.contains(leftHandSide)) {
                return false;
            }
            for (List<String> possibleNextMoves : this.productions.get(leftHandSide)) {
                for (String possibleNextMove : possibleNextMoves) {
                    if (! (this.nonterminals.contains(possibleNextMove) ||
                            this.terminals.contains(possibleNextMove) ||
                            possibleNextMove.equals(this.EPSILON))) {
                        return false;
                    }
                }
            }
        }

        return true;
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

    public Map<String, Set<List<String>>> getProductions() {
        return this.productions;
    }

    public String getStartingSymbol() {
        return this.startingSymbol;
    }

    public boolean isCFG() {
        return this.isCFG;
    }
}
