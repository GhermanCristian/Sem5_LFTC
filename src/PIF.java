import java.util.ArrayList;
import java.util.List;

public class PIF {
    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPairs;

    public PIF() {
        this.tokenPositionPairs = new ArrayList<>();
    }

    public void add(Pair<String, Pair<Integer, Integer>> pair) {
        this.tokenPositionPairs.add(pair);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.tokenPositionPairs.forEach(pair -> stringBuilder.append(pair.getFirst()).append(" - ").append(pair.getSecond()).append("\n"));
        return stringBuilder.toString();
    }
}
