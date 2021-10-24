import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class MyScanner {
    private final ArrayList<String> OPERATORS = new ArrayList<>(
            List.of("+", "-", "*", "/", "<=", ">=", "==", "<", ">", "%", "=", "cin>>", "cout<<"));
    private final ArrayList<String> SEPARATORS = new ArrayList<>(
            List.of("{", "}", ":", ";", " ", ",", "[", "]", "(", ")", "\""));
    private final ArrayList<String> RESERVED_WORDS = new ArrayList<>(
            List.of("int", "char", "void", "struct", "while", "if", "else", "main"));

    private final String filePath;
    private SymbolTable symbolTable;

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine());
        }
        return fileContent.toString()
                .replace("\n", "")
                .replace("\t", "");
    }

    private List<String> tokenizeWithCompleteStrings(List<String> tokensIncludingSeparators) {
        List<String> tokensWithCompleteStrings = new ArrayList<>();
        boolean inString = false;
        StringBuilder currentString = new StringBuilder();
        for (String token : tokensIncludingSeparators) {
            if (token.equals("\"")) {
                if (inString) { // end of string
                    tokensWithCompleteStrings.add(currentString.toString());
                    currentString = new StringBuilder();
                }
                inString = !inString;
            }
            else {
                if (inString) { // add the current token to the string
                    currentString.append(token);
                }
                else if (!this.SEPARATORS.contains(token)) {
                    tokensWithCompleteStrings.add(token);
                }
            }
        }
        return tokensWithCompleteStrings;
    }

    private List<String> tokenize() {
        try {
            String fileContent = this.readFile();
            String separators = this.SEPARATORS.stream().reduce("", (a, b) -> a + b);
            List<String> tokensIncludingSeparators = Collections.list(new StringTokenizer(fileContent, separators, true)).stream()
                    .map(token -> (String) token)
                    .collect(Collectors.toList());
            return this.tokenizeWithCompleteStrings(tokensIncludingSeparators);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addToSymbolTable() {
        List<String> tokens = this.tokenize();
        if (tokens == null) {
            return;
        }
        tokens.forEach(token -> {
            System.out.print(token);
            if (this.RESERVED_WORDS.contains(token)) {
                System.out.print(" - reserved word");
            }
            else if (this.OPERATORS.contains(token)) {
                System.out.print(" - operator");
            }
            else {
                System.out.print(" - identifier / constant");
                this.symbolTable.add(token);
            }
            System.out.println();
        });
    }

    public void scan() {
        this.addToSymbolTable();
        System.out.println(this.symbolTable);
    }
}
