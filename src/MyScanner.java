import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyScanner {
    private final ArrayList<String> OPERATORS = new ArrayList<>(
            List.of("+", "-", "*", "/", "<=", ">=", "==", "<", ">", "%", "=", "cin>>", "cout<<"));
    private final ArrayList<String> SEPARATORS = new ArrayList<>(
            List.of("{", "}", ":", ";", " ", "'", ",", "[", "]", "(", ")", "\"", "\n"));
    private final ArrayList<String> RESERVED_WORDS = new ArrayList<>(
            List.of("int", "char", "void", "struct", "while", "if", "else", "main"));

    private final String filePath;
    private PIF pif;
    private SymbolTable symbolTable;

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
        this.pif = new PIF();
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }
        return fileContent.toString().replace("\t", "");
    }

    private List<Pair<String, Integer>> tokenizeWithCompleteStringsAndLineNumbers(List<String> tokensIncludingSeparators) {
        List<Pair<String, Integer>> tokensWithCompleteStrings = new ArrayList<>();
        boolean inString = false;
        StringBuilder currentString = new StringBuilder();
        int lineNumber = 1;

        for (String token : tokensIncludingSeparators) {
            if (token.equals("\"")) {
                currentString.append(token);
                if (inString) { // end of string
                    tokensWithCompleteStrings.add(new Pair<>(currentString.toString(), lineNumber));
                    currentString = new StringBuilder();
                }
                inString = !inString;
            }
            else if (token.equals("\n")) {
                lineNumber++;
            }
            else {
                if (inString) { // add the current token to the string
                    currentString.append(token);
                }
                else if (!token.equals(" ")) {
                    tokensWithCompleteStrings.add(new Pair<>(token, lineNumber));
                }
            }
        }
        return tokensWithCompleteStrings;
    }

    private List<Pair<String, Integer>> tokenize() {
        try {
            String fileContent = this.readFile();
            String separators = this.SEPARATORS.stream().reduce("", (a, b) -> a + b);
            List<String> tokensIncludingSeparators = Collections.list(new StringTokenizer(fileContent, separators, true)).stream()
                    .map(token -> (String) token)
                    .collect(Collectors.toList());
            return this.tokenizeWithCompleteStringsAndLineNumbers(tokensIncludingSeparators);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void scan() {
        List<Pair<String, Integer>> tokens = this.tokenize();
        AtomicBoolean foundLexicalError = new AtomicBoolean(false);
        if (tokens == null) {
            return;
        }
        tokens.forEach(tokenLinePair -> {
            String token = tokenLinePair.getFirst();
            if (this.RESERVED_WORDS.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 2);
            }
            else if (this.OPERATORS.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 3);
            }
            else if (this.SEPARATORS.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 4);
            }
            else if (new FiniteAutomaton("IO/FA_identifier.txt").acceptsSequence(token)) { // identifier
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, this.symbolTable.findPositionOfTerm(token)), 0);
            }
            else if (Pattern.compile("\"[a-zA-Z0-9 ]*\"|'[a-zA-Z0-9 ]'").matcher(token).matches() || new FiniteAutomaton("IO/FA_integerConstants.txt").acceptsSequence(token)) { // constant
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, this.symbolTable.findPositionOfTerm(token)), 1);
            }
            else {
                System.out.println("Lexical error, line " + tokenLinePair.getSecond());
                foundLexicalError.set(true);
            }
        });

        if (!foundLexicalError.get()) {
            System.out.println("Lexically correct");
        }
    }

    public PIF getPif() {
        return this.pif;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
}
