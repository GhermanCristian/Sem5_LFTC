import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private final String EPSILON = "EPS";

    private Map<String, Set<String>> firstSets;
    private final Grammar grammar;

    private Set<String> concatenationOfLength1(Set<String> l1, Set<String> l2) {
        Set<String> concatenationResult = new HashSet<>();
        l1.forEach(element -> {
            if (element.equals(this.EPSILON)) {
                concatenationResult.addAll(l2);
            }
            else {
                concatenationResult.add(element);
            }
        });
        return concatenationResult;
    }

    private Set<String> getFirstTerminalsForNonterminal(String nonterminal) {
        return this.grammar.getProductionsForNonterminal(nonterminal)
                .stream()
                .filter(rhs -> this.grammar.getTerminals().contains(rhs.get(0)))
                .map(rhs -> rhs.get(0))
                .collect(Collectors.toSet());
    }

    private Set<String> getPreviousFirst(String element) {
        if (this.grammar.getTerminals().contains(element) || this.EPSILON.equals(element)) {
            return Set.of(element);
        }
        return this.firstSets.get(element);
    }

    private Set<String> computeConcatenationMultipleElementsInRHS(List<String> productionRHS) {
        if (productionRHS.size() == 0) {
            throw new RuntimeException("Empty production RHS");
        }
        if (productionRHS.size() == 1) {
            return this.getPreviousFirst(productionRHS.get(0));
        }

        Set<String> union = this.concatenationOfLength1(this.getPreviousFirst(productionRHS.get(0)), this.getPreviousFirst(productionRHS.get(1)));
        for (int i = 2; i < productionRHS.size(); i++) {
            union = this.concatenationOfLength1(union, this.getPreviousFirst(productionRHS.get(i)));
        }
        return union;
    }

    private void computeFirstSets() {
        this.grammar.getNonterminals().forEach(nonterminal -> this.firstSets.put(nonterminal, this.getFirstTerminalsForNonterminal(nonterminal)));

        boolean sameSets = false;
        while (! sameSets) {
            sameSets = true;
            Map<String, Set<String>> temporaryFirstSets = new HashMap<>();
            for (String nonterminal : this.grammar.getNonterminals()) {
                temporaryFirstSets.put(nonterminal, new HashSet<>());
                this.grammar.getProductionsForNonterminal(nonterminal)
                        .forEach(productionRHS -> temporaryFirstSets.get(nonterminal).addAll(this.computeConcatenationMultipleElementsInRHS(productionRHS)));
                if (! temporaryFirstSets.get(nonterminal).equals(this.firstSets.get(nonterminal))) {
                    sameSets = false;
                }
            }
            this.firstSets = temporaryFirstSets;
        }
    }

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.firstSets = new HashMap<>();
    }

    public void parse() {
        this.computeFirstSets();
        this.firstSets.forEach((a, b) -> System.out.println(a + "---" + b));
    }
}
