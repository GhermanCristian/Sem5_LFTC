import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private final Grammar grammar;
    private final FirstSet firstSets;
    private Map<String, Set<String>> followSets;

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

    private void processPossibleProductionRHS(Map<String, Set<String>> temporaryFollowSets, String nonterminal, List<String> possibleProductionRHS, String LHSNonterminal) {
        List<String> elementsAfterNonterminalFirstOccurrence = possibleProductionRHS.subList(possibleProductionRHS.indexOf(nonterminal) + 1, possibleProductionRHS.size());
        if (elementsAfterNonterminalFirstOccurrence.size() == 0) {
            elementsAfterNonterminalFirstOccurrence.add(Constants.EPSILON);
        }

        Set<String> FIRST = this.firstSets.computeFIRSTConcatenationRHS(elementsAfterNonterminalFirstOccurrence);
        FIRST.forEach(element -> {
            if (element.equals(Constants.EPSILON)) {
                temporaryFollowSets.get(nonterminal).addAll(this.followSets.get(LHSNonterminal));
            }
            else {
                temporaryFollowSets.get(nonterminal).addAll(this.firstSets.getPreviousFirst(element));
            }
        });
    }

    private void processNonterminal(Map<String, Set<String>> temporaryFollowSets, String nonterminal) {
        temporaryFollowSets.put(nonterminal, new HashSet<>(this.followSets.get(nonterminal)));

        List<Pair<String, Set<List<String>>>> productionsWhichContainNonterminal = this.findProductionsWhichContainNonterminal(nonterminal);
        productionsWhichContainNonterminal.forEach(pair -> {
            String LHSNonterminal = pair.getFirst();
            Set<List<String>> productionRHS = pair.getSecond();
            productionRHS.forEach(possibleProductionRHS -> this.processPossibleProductionRHS(temporaryFollowSets, nonterminal, possibleProductionRHS, LHSNonterminal));
        });
    }

    private void computeFollowSets() {
        this.grammar.getNonterminals().forEach(nonterminal -> {
            Set<String> newSet = new HashSet<>();
            if (nonterminal.equals(this.grammar.getStartingSymbol())) {
                newSet.add(Constants.END_OF_INPUT);
            }
            this.followSets.put(nonterminal, newSet);
        });

        boolean sameSets = false;
        while (! sameSets) {
            sameSets = true;
            Map<String, Set<String>> temporaryFollowSets = new HashMap<>();
            for (String nonterminal : this.grammar.getNonterminals()) {
                processNonterminal(temporaryFollowSets, nonterminal);
                if (! temporaryFollowSets.get(nonterminal).equals(this.followSets.get(nonterminal))) {
                    sameSets = false;
                }
            }
            this.followSets = temporaryFollowSets;
        }
    }

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.firstSets = new FirstSet(this.grammar);
        this.followSets = new HashMap<>();
        this.computeFollowSets();
    }

    public void parse() {
        this.firstSets.getFirstSets().forEach((a, b) -> System.out.println(a + "---" + b));
        this.followSets.forEach((a, b) -> System.out.println(a + "###" + b));
    }
}
