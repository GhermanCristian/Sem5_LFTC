import java.util.*;
import java.util.stream.Collectors;

public class FollowSet {
    private Map<String, Set<String>> followSets;
    private final Grammar grammar;
    private final FirstSet firstSets;

    public Map<String, Set<List<String>>> findProductionsWhichContainNonterminal(String nonterminal) {
        Map<String, Set<List<String>>> productionsWhichContainNonterminal = new HashMap<>();
        this.grammar.getProductions().forEach((productionLHS, productionRHS) -> {
            Set<List<String>> productionRHSWhichStartFromCurrentNonterminal = productionRHS
                    .stream()
                    .filter(possibleProductionRHS -> possibleProductionRHS.contains(nonterminal))
                    .collect(Collectors.toSet());
            if (! productionRHSWhichStartFromCurrentNonterminal.isEmpty()) {
                productionsWhichContainNonterminal.put(productionLHS.get(0), productionRHSWhichStartFromCurrentNonterminal);
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
                Set<String> previousFIRST = this.firstSets.getPreviousFirst(element);
                previousFIRST.remove(Constants.EPSILON);
                temporaryFollowSets.get(nonterminal).addAll(previousFIRST);
            }
        });
    }

    private void processNonterminal(Map<String, Set<String>> temporaryFollowSets, String nonterminal) {
        temporaryFollowSets.put(nonterminal, new HashSet<>(this.followSets.get(nonterminal)));

        Map<String, Set<List<String>>> productionsWhichContainNonterminal = this.findProductionsWhichContainNonterminal(nonterminal);
        productionsWhichContainNonterminal
                .forEach((LHSNonterminal, productionRHS) -> productionRHS
                        .forEach(possibleProductionRHS -> this.processPossibleProductionRHS(temporaryFollowSets, nonterminal, possibleProductionRHS, LHSNonterminal)));
    }

    private void initializeFollowSets() {
        this.grammar.getNonterminals().forEach(nonterminal -> {
            Set<String> newSet = new HashSet<>();
            if (nonterminal.equals(this.grammar.getStartingSymbol())) {
                newSet.add(Constants.END_OF_INPUT);
            }
            this.followSets.put(nonterminal, newSet);
        });
    }

    private void computeFollowSets() {
        this.followSets = new HashMap<>();
        this.initializeFollowSets();

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

    public FollowSet(Grammar grammar, FirstSet firstSets) {
        this.grammar = grammar;
        this.firstSets = firstSets;
        this.computeFollowSets();
    }

    public Map<String, Set<String>> getFollowSets() {
        return this.followSets;
    }
}
