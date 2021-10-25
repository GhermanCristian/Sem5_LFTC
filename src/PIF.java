import java.util.ArrayList;
import java.util.List;

public class PIF {
    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPairs;
    private List<Integer> type;

    public PIF() {
        this.tokenPositionPairs = new ArrayList<>();
        this.type = new ArrayList<>();
    }

    public void add(Pair<String, Pair<Integer, Integer>> pair, Integer type) {
        this.tokenPositionPairs.add(pair);
        this.type.add(type);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.tokenPositionPairs.size(); i++) {
            stringBuilder.append(this.tokenPositionPairs.get(i).getFirst())
                        .append(" - ")
                        .append(this.tokenPositionPairs.get(i).getSecond())
                        .append(" -> ")
                        .append(this.type.get(i))
                        .append("\n");
        }
        return stringBuilder.toString();
    }
}
