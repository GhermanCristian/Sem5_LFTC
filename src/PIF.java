import java.util.ArrayList;
import java.util.List;

public class PIF {
    private final List<Pair<String, Pair<Integer, Integer>>> tokenPositionPairs;
    private final List<Integer> type;

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
            int tokenType = this.type.get(i);
            if (tokenType == 0) {
                stringBuilder.append("identifier");
            }
            else if (tokenType == 1) {
                stringBuilder.append("constant");
            }
            else {
                stringBuilder.append(this.tokenPositionPairs.get(i).getFirst());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString().stripTrailing();
    }
}
