import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private final Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    private List<Pair<String, Set<List<String>>>> findProductionsWhichContainNonterminal(String nonterminal) {
        List<Pair<String, Set<List<String>>>> productionsWhichContainNonterminal = new ArrayList<>();
        this.grammar.getProductions().forEach((productionLHS, productionRHS) -> {
            Set<List<String>> productionRHSWhichStartFromCurrentNonterminal = productionRHS
                    .stream()
                    .filter(possibleProductionRHS -> possibleProductionRHS.contains(nonterminal))
                    .collect(Collectors.toSet());
            if (! productionRHSWhichStartFromCurrentNonterminal.isEmpty()) {
                productionsWhichContainNonterminal.add(new Pair<>(productionLHS.get(0), productionRHSWhichStartFromCurrentNonterminal));
            }
        });
        return productionsWhichContainNonterminal;
    }

    public void parse() {
        Map<String, Set<String>> firstSets = new FirstSet(this.grammar).getFirstSets();
        firstSets.forEach((a, b) -> System.out.println(a + "---" + b));
        this.grammar.getNonterminals().forEach(nonterminal -> {
            System.out.println("The following contain " + nonterminal);
            this.findProductionsWhichContainNonterminal(nonterminal).forEach(pair -> {
                System.out.print(pair.getFirst() + " -> ");
                pair.getSecond().forEach(System.out::print);
                System.out.println();
            });
        });
    }
}
