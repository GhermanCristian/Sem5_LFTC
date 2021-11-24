import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private final String EPSILON = "EPS";

    private Map<String, Set<String>> firstSet;
    private final Grammar grammar;

    private Set<String> concatenationOfLength1(Set<String> l1, Set<String> l2) {
        Set<String> concatenationResult = new HashSet<>();
        l1.forEach(element -> {
            if (element.equals(this.EPSILON)) {
                l2.forEach(e -> concatenationResult.add(e.substring(0, 1)));
            }
            else {
                concatenationResult.add(element.substring(0, 1));
            }
        });
        return concatenationResult;
    }

    private List<String> getFirstTerminalsForNonterminal(String nonterminal) {
        return this.grammar.getProductionsForNonterminal(nonterminal)
                .stream()
                .filter(rhs -> this.grammar.getTerminals().contains(rhs.get(0)))
                .map(rhs -> rhs.get(0))
                .collect(Collectors.toList());
    }

    private void computeFirstSets() {
        // initialize

        boolean sameSets = false;
        while (! sameSets) {
            sameSets = true;
        }
    }

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.firstSet = new HashMap<>();
    }
}
