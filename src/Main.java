import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    private static void printToFile(String filePath, Object object) {
        try(PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(String filePath) {
        MyScanner scanner = new MyScanner(filePath);
        scanner.scan();
        printToFile(filePath.replace(".txt", "ST.txt"), scanner.getSymbolTable());
        printToFile(filePath.replace(".txt", "PIF.txt"), scanner.getPif());
    }

    public static void main(String[] args) {
        Grammar g = new Grammar("IO/G1.txt");
        System.out.println("Nonterminals - " + g.getNonterminals());
        System.out.println("Terminals - " + g.getTerminals());
        System.out.println("Starting symbol - " + g.getStartingSymbol());
        System.out.println("Productions - ");
        g.getProductions().forEach((lhs, rhs) -> System.out.println(lhs + "->" + rhs));
        System.out.println("Is CFG ? " + g.isCFG());
        new Parser(g).parse();
    }
}
