public class Parser {
    private final FirstSet firstSets;
    private final FollowSet followSets;
    private final ParseTable parseTable; // row -> column -> (productionRHS, number)

    public Parser(Grammar grammar) {
        this.firstSets = new FirstSet(grammar);
        this.followSets = new FollowSet(grammar, this.firstSets);
        this.parseTable = new ParseTable(this.firstSets, this.followSets, grammar);
    }

    public void parse() {
        this.firstSets.getFirstSets().forEach((a, b) -> System.out.println(a + "---" + b));
        this.followSets.getFollowSets().forEach((a, b) -> System.out.println(a + "###" + b));
        this.parseTable.getParseTable().forEach((a, b) -> b.forEach((c, d) -> System.out.print("[" + a + "][" + c + "] = " + d + "\n")));
    }
}
