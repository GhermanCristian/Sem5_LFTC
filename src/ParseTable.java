import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseTable {
    private final FirstSet firstSets;
    private final FollowSet followSets;
    private final Grammar grammar;
    private final Map<String, Map<String, Pair<List<String>, Integer>>> parseTable; // row -> column -> (productionRHS, number)

    public ParseTable(FirstSet firstSets, FollowSet followSets, Grammar grammar) {
        this.firstSets = firstSets;
        this.followSets = followSets;
        this.grammar = grammar;
        this.parseTable = this.generateParseTable();
    }

    private void applyFirstRule(Map<String, Map<String, Pair<List<String>, Integer>>> table) {
        this.grammar.getProductions().forEach((LHSAsList, productionSet) ->
            productionSet.forEach(productionRHS -> {
                String LHS = LHSAsList.get(0); // it's a CFG
                int productionCode = this.grammar.findCodeForProduction(new Pair<>(LHS, productionRHS));
                assert productionCode != -1;
                table.putIfAbsent(LHS, new HashMap<>());

                if (productionRHS.size() == 1 && productionRHS.get(0).equals(Constants.EPSILON)) {
                    this.followSets.getFollowSets().get(LHS)
                            .forEach(element -> {
                                if (table.get(LHS).containsKey(element)) {
                                    throw new RuntimeException("Not LL1: " + element);
                                }
                                table.get(LHS).put(element, new Pair<>(List.of(Constants.EPSILON), productionCode));
                            });
                }
                else {
                    this.firstSets.computeFIRSTConcatenationRHS(productionRHS).forEach(element -> {
                        if (table.get(LHS).containsKey(element)) {
                            throw new RuntimeException("Not LL1: " + element);
                        }
                        productionRHS.remove(Constants.EPSILON);
                        table.get(LHS).put(element, new Pair<>(productionRHS, productionCode));
                    });
                }
            }));
    }

    private void applySecondRule(Map<String, Map<String, Pair<List<String>, Integer>>> table) {
        this.grammar.getTerminals().forEach(terminal -> {
            table.putIfAbsent(terminal, new HashMap<>());
            table.get(terminal).put(terminal, new Pair<>(List.of(Constants.POP), Constants.POP_CODE));
        });
    }

    private void applyThirdRule(Map<String, Map<String, Pair<List<String>, Integer>>> table) {
        table.putIfAbsent(Constants.END_OF_INPUT, Map.of(Constants.END_OF_INPUT, new Pair<>(List.of(Constants.ACCEPTED), Constants.ACCEPTED_CODE)));
    }

    private Map<String, Map<String, Pair<List<String>, Integer>>> generateParseTable() {
        Map<String, Map<String, Pair<List<String>, Integer>>> table = new HashMap<>();
        this.applyFirstRule(table);
        this.applySecondRule(table);
        this.applyThirdRule(table);
        return table;
    }

    public Map<String, Map<String, Pair<List<String>, Integer>>> getParseTable() {
        return this.parseTable;
    }
}
