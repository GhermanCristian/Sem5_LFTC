import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static String PROGRAM_FILE_NAME = "p1";
    private final static String PROGRAM_FILE_NPATH = "IO/" + PROGRAM_FILE_NAME + ".txt";
    private final static String GRAMMAR_FILE_PATH = "IO/G2.txt";
    private final static String PIF_FILE_PATH = "IO/" + PROGRAM_FILE_NAME + "PIF.txt";

    private static void printToFile(String filePath, Object object) {
        try(PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void generateSTAndPIF() {
        MyScanner scanner = new MyScanner(PROGRAM_FILE_NPATH);
        scanner.scan();
        printToFile(PROGRAM_FILE_NPATH.replace(".txt", "ST.txt"), scanner.getSymbolTable());
        printToFile(PROGRAM_FILE_NPATH.replace(".txt", "PIF.txt"), scanner.getPif());
    }

    private static List<String> loadSequenceFromFile() throws FileNotFoundException {
        List<String> sequence = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(PIF_FILE_PATH))) {
            while (scanner.hasNextLine()) {
                sequence.add(scanner.nextLine());
            }
        }
        return sequence;
    }

    public static void main(String[] args) throws FileNotFoundException {
        generateSTAndPIF();
        Grammar g = new Grammar(GRAMMAR_FILE_PATH);
        System.out.println("Nonterminals - " + g.getNonterminals());
        System.out.println("Terminals - " + g.getTerminals());
        System.out.println("Starting symbol - " + g.getStartingSymbol());
        System.out.println("Productions - ");
        g.getProductions().forEach((lhs, rhs) -> System.out.println(lhs + "->" + rhs));
        System.out.println("Is CFG ? " + g.isCFG());
        new Parser(g).parse(loadSequenceFromFile());
    }
}
