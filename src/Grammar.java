import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private final String ELEMENT_SEPARATOR = ";;";
    private final String TRANSITION_OR_SEPARATOR = "\\|";
    private final String TRANSITION_CONCATENATION = " ";
    private final String SEPARATOR_LEFT_RIGHT_HAND_SIDE = "->";

    // LL1
    private Set<String> nonterminals;
    private Set<String> terminals;
    private Map<List<String>, Set<List<String>>> productions;
    private String startingSymbol;
    private boolean isCFG;

    private void processProduction(String production) {
        String[] leftAndRightHandSide = production.split(this.SEPARATOR_LEFT_RIGHT_HAND_SIDE);
        List<String> splitLHS = List.of(leftAndRightHandSide[0].split(this.TRANSITION_CONCATENATION));
        String[] splitRHS = leftAndRightHandSide[1].split(this.TRANSITION_OR_SEPARATOR);

        this.productions.putIfAbsent(splitLHS, new HashSet<>());
        Arrays.stream(splitRHS).forEach(splitRH -> this.productions.get(splitLHS).add(Arrays.stream(splitRH.split(this.TRANSITION_CONCATENATION)).collect(Collectors.toList())));
    }

    private void loadFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            this.nonterminals = new HashSet<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.terminals = new HashSet<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.startingSymbol = scanner.nextLine();

            this.productions = new HashMap<>();
            while (scanner.hasNextLine()) {
                this.processProduction(scanner.nextLine());
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

        for (List<String> leftHandSide : this.productions.keySet()) {
            // left-hand side has to contain only 1 element: a nonterminal
            if (leftHandSide.size() != 1 || ! this.nonterminals.contains(leftHandSide.get(0))) {
                return false;
            }

            for (List<String> possibleNextMoves : this.productions.get(leftHandSide)) {
                for (String possibleNextMove : possibleNextMoves) {
                    if (! (this.nonterminals.contains(possibleNextMove) ||
                            this.terminals.contains(possibleNextMove) ||
                            possibleNextMove.equals(Constants.EPSILON))) {
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

    public Set<String> getNonterminals() {
        return this.nonterminals;
    }

    public Set<String> getTerminals() {
        return this.terminals;
    }

    public Map<List<String>, Set<List<String>>> getProductions() {
        return this.productions;
    }

    public Set<List<String>> getProductionsForNonterminal(String nonterminal) {
        // assume CFG
        return this.productions.get(List.of(nonterminal));
    }

    public String getStartingSymbol() {
        return this.startingSymbol;
    }

    public boolean isCFG() {
        return this.isCFG;
    }
}
