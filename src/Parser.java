import java.util.List;

public class Parser {
    private final FirstSet firstSets;
    private final FollowSet followSets;
    private final ParseTable parseTable; // row -> column -> (productionRHS, number)
    private final SequenceEvaluator sequenceEvaluator;
    private final Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.firstSets = new FirstSet(grammar);
        this.followSets = new FollowSet(grammar, this.firstSets);
        this.parseTable = new ParseTable(this.firstSets, this.followSets, grammar);
        this.sequenceEvaluator = new SequenceEvaluator(grammar.getStartingSymbol(), this.parseTable);
    }

    public void parse(List<String> sequence) {
        this.firstSets.getFirstSets().forEach((a, b) -> System.out.println(a + "---" + b));
        this.followSets.getFollowSets().forEach((a, b) -> System.out.println(a + "###" + b));
        this.parseTable.getParseTable().forEach((a, b) -> b.forEach((c, d) -> System.out.print("[" + a + "][" + c + "] = " + d + "\n")));
        List<Integer> sequenceProductionCodes = this.sequenceEvaluator.evaluateSequence(sequence);
        System.out.println(sequenceProductionCodes);
        new OutputTree(this.grammar, sequenceProductionCodes).printTree();
    }
}
