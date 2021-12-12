import java.util.ArrayList;

public class SymbolTable {
    private final Integer size;
    private final ArrayList<ArrayList<String>> table;

    private Integer hashFunction(String key) {
        int ASCIISum = 0;
        for (int i = 0; i < key.length(); i++) {
            ASCIISum = (ASCIISum + key.charAt(i)) % size;
        }
        return ASCIISum % size;
    }

    public boolean containsTerm(String term) {
        return this.findPositionOfTerm(term) != null;
    }

    public Pair<Integer, Integer> findPositionOfTerm(String term) {
        int key = this.hashFunction(term);
        if (! this.table.get(key).isEmpty()) {
            ArrayList<String> collisionList = this.table.get(key);
            for (int i = 0; i < collisionList.size(); i++) {
                if (collisionList.get(i).equals(term)) {
                    return new Pair<>(key, i);
                }
            }
        }
        return null;
    }

    public boolean add(String term) {
        int key = this.hashFunction(term);
        if (this.containsTerm(term)) {
            return false; // element is already in the symbol table
        }

        this.table.get(key).add(term);
        return true;
    }

    public SymbolTable(Integer initialSize) {
        this.size = initialSize;
        this.table = new ArrayList<>();
        for (int i = 0; i < this.size; i++) {
            this.table.add(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        for (int i = 0; i < this.table.size(); i++) {
            if (this.table.get(i).size() > 0) {
                representation.append(i).append(" - ").append(this.table.get(i)).append("\n");
            }
        }
        return representation.toString();
    }
}
