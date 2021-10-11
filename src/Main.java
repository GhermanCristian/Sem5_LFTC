public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(5);

        symbolTable.add("1");
        System.out.println(symbolTable.containsTerm("1")); // true
        Pair position = symbolTable.findPositionOfTerm("1");
        System.out.println(position);
        System.out.println(symbolTable.findByPosition(position)); // 1

        symbolTable.add("6");
        System.out.println(symbolTable.findPositionOfTerm("6")); // should be on the same collision list as "1", because of size=5 (and 1+5=6)
        System.out.println(symbolTable.containsTerm("6")); // true

        symbolTable.add("5");
        System.out.println(symbolTable.findPositionOfTerm("5"));
        System.out.println(symbolTable.containsTerm("5"));
    }
}
