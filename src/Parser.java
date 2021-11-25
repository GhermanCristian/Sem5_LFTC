import java.util.*;

public class Parser {
    private final Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    public void parse() {
        Map<String, Set<String>> firstSets = new FirstSet(this.grammar).getFirstSets();
        firstSets.forEach((a, b) -> System.out.println(a + "---" + b));
    }
}
