import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final FirstSet firstSets;
    private final FollowSet followSets;
    private final Grammar grammar;
    private final Map<String, Map<String, Pair<List<String>, Integer>>> parseTable; // row -> column -> (productionRHS, number)

    public Parser(Grammar grammar) {
        this.firstSets = new FirstSet(grammar);
        this.followSets = new FollowSet(grammar, this.firstSets);
        this.grammar = grammar;
        this.parseTable = this.generateParseTable();
    }
    
    private Map<String, Map<String, Pair<List<String>, Integer>>> generateParseTable() {
        Map<String, Map<String, Pair<List<String>, Integer>>> table = new HashMap<>();

        this.grammar.getProductions().forEach((LHSAsList, productionSet) ->
            productionSet.forEach(productionRHS -> {
                String LHS = LHSAsList.get(0); // it's a CFG
                int productionCode = this.grammar.findCodeForProduction(new Pair<>(LHS, productionRHS));
                assert productionCode != -1;
                table.putIfAbsent(LHS, new HashMap<>());

                if (productionRHS.size() == 1 && productionRHS.get(0).equals(Constants.EPSILON)) {
                    this.followSets.getFollowSets().get(LHS)
                            .forEach(element -> table.get(LHS).put(element, new Pair<>(List.of(Constants.EPSILON), productionCode)));
                }
                else {
                    this.firstSets.computeFIRSTConcatenationRHS(productionRHS).forEach(element -> {
                        productionRHS.remove(Constants.EPSILON);
                        table.get(LHS).put(element, new Pair<>(productionRHS, productionCode));
                    });
                }
            }));

        this.grammar.getTerminals().forEach(terminal -> {
            table.putIfAbsent(terminal, new HashMap<>());
            table.get(terminal).put(terminal, new Pair<>(List.of(Constants.POP), Constants.NO_CODE));
        });

        table.putIfAbsent(Constants.END_OF_INPUT, Map.of(Constants.END_OF_INPUT, new Pair<>(List.of(Constants.ACCEPTED), Constants.NO_CODE)));

        return table;
    }

    public void parse() {
        this.firstSets.getFirstSets().forEach((a, b) -> System.out.println(a + "---" + b));
        this.followSets.getFollowSets().forEach((a, b) -> System.out.println(a + "###" + b));
        this.parseTable.forEach((a, b) -> b.forEach((c, d) -> System.out.print("[" + a + "][" + c + "] = " + d + "\n")));
    }
}
