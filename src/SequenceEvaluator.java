import java.util.ArrayList;
import java.util.List;

public class SequenceEvaluator {
    private final String startingSymbol;
    private final ParseTable parseTable;

    public SequenceEvaluator(String startingSymbol, ParseTable parseTable) {
        this.startingSymbol = startingSymbol;
        this.parseTable = parseTable;
    }

    public List<Integer> evaluateSequence(List<String> sequence) {
        List<Integer> output = new ArrayList<>();

        List<String> inputStack = new ArrayList<>(sequence);
        inputStack.add(Constants.END_OF_INPUT);

        List<String> workingStack = new ArrayList<>();
        workingStack.add(this.startingSymbol);
        workingStack.add(Constants.END_OF_INPUT);

        boolean finished = false;
        while (! finished) {
            String inputTop = inputStack.get(0);
            String workingStackTop = workingStack.remove(0);

            if (this.parseTable.getParseTable().containsKey(workingStackTop)) {
                if (this.parseTable.getParseTable().get(workingStackTop).containsKey(inputTop)) {
                    Pair<List<String>, Integer> tableEntry = this.parseTable.getParseTable().get(workingStackTop).get(inputTop);

                    if (tableEntry.getSecond() == Constants.POP_CODE) { // pop
                        inputStack.remove(0);
                    }
                    else if (tableEntry.getSecond() == Constants.ACCEPTED_CODE && inputTop.equals(Constants.END_OF_INPUT) && workingStackTop.equals(Constants.END_OF_INPUT)) { // accept
                        finished = true;
                    }
                    else { // push
                        for (int i = tableEntry.getFirst().size() - 1; i >= 0; i--) {
                            String symbol = tableEntry.getFirst().get(i);
                            if (! symbol.equals(Constants.EPSILON)) {
                                workingStack.add(0, symbol);
                            }
                        }
                        output.add(tableEntry.getSecond());
                    }
                }
                else {
                    throw new RuntimeException(inputTop + " is not a valid terminal");
                }
            }
            else {
                throw new RuntimeException(workingStackTop + " is not a valid terminal / nonterminal");
            }
        }
        return output;
    }
}
