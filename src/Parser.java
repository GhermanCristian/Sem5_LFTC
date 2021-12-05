import java.util.List;

public class Parser {
    private final FirstSet firstSets;
    private final FollowSet followSets;
    private final ParseTable parseTable; // row -> column -> (productionRHS, number)
    private final SequenceEvaluator sequenceEvaluator;

    public Parser(Grammar grammar) {
        this.firstSets = new FirstSet(grammar);
        this.followSets = new FollowSet(grammar, this.firstSets);
        this.parseTable = new ParseTable(this.firstSets, this.followSets, grammar);
        this.sequenceEvaluator = new SequenceEvaluator(grammar.getStartingSymbol(), this.parseTable);
    }

    public void parse() {
        this.firstSets.getFirstSets().forEach((a, b) -> System.out.println(a + "---" + b));
        this.followSets.getFollowSets().forEach((a, b) -> System.out.println(a + "###" + b));
        this.parseTable.getParseTable().forEach((a, b) -> b.forEach((c, d) -> System.out.print("[" + a + "][" + c + "] = " + d + "\n")));
        System.out.println(this.sequenceEvaluator.evaluateSequence(List.of("(", "int", ")", "+", "int")));
    }
}
