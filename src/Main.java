import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    private static List<String> loadSequenceFromFile(String filePath) throws FileNotFoundException {
        List<String> sequence = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                sequence.add(scanner.nextLine());
            }
        }
        return sequence;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Grammar g = new Grammar("IO/G1.txt");
        System.out.println("Nonterminals - " + g.getNonterminals());
        System.out.println("Terminals - " + g.getTerminals());
        System.out.println("Starting symbol - " + g.getStartingSymbol());
        System.out.println("Productions - ");
        g.getProductions().forEach((lhs, rhs) -> System.out.println(lhs + "->" + rhs));
        System.out.println("Is CFG ? " + g.isCFG());
        new Parser(g).parse(loadSequenceFromFile("IO/SEQ1.txt"));
    }
}
