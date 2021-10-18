import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Parser {
    private final ArrayList<String> OPERATORS = new ArrayList<>(
            List.of("+", "-", "*", "/", "<", "<=", "==", ">=", "%", "=", "cin>>", "cout<<"));
    private final ArrayList<String> SEPARATORS = new ArrayList<>(
            List.of("{", "}", ":", ";", " ", ",", "[", "]", "(", ")"));
    private final ArrayList<String> RESERVED_WORDS = new ArrayList<>(
            List.of("int", "char", "void", "struct", "while", "if", "else", "main"));

    private String readFile(String filePath) throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine());
        }
        return fileContent.toString()
                .replace("\n", "")
                .replace("\t", "");
    }

    public List<String> tokenize(String filePath) {
        try {
            String fileContent = this.readFile(filePath);
            return new ArrayList<>(List.of(fileContent.split("[{};: ,\\[\\]()\"]")))
                    .stream()
                    .filter(possibleToken -> possibleToken.length() > 0)
                    .collect(Collectors.toList());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
