import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomaton {
    private final String ELEMENT_SEPARATOR = ";";
    private final String TRANSITION_SEPARATOR = "-";

    private String initialState;
    private List<String> states;
    private List<String> alphabet;
    private Map<String, Set<Pair<String, String>>> transitions;
    private List<String> finalStates;

    private void readFromFile(String filePath) {
        // assume the file is valid
        try (Scanner scanner = new Scanner(new File(filePath))) {
            this.states = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
            this.initialState = this.states.get(0);
            this.alphabet = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));

            this.transitions = new HashMap<>();
            String[] transitionStrings = scanner.nextLine().split(this.ELEMENT_SEPARATOR);
            for (String transition : transitionStrings) {
                String[] splitTransition = transition.split(this.TRANSITION_SEPARATOR);
                this.transitions.putIfAbsent(splitTransition[0], new HashSet<>());
                this.transitions.get(splitTransition[0]).add(new Pair<>(splitTransition[2], splitTransition[1]));
            }

            this.finalStates = new ArrayList<>(List.of(scanner.nextLine().split(this.ELEMENT_SEPARATOR)));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public FiniteAutomaton(String filePath) {
        this.readFromFile(filePath);
    }

    public List<String> getStates() {
        return this.states;
    }

    public List<String> getAlphabet() {
        return this.alphabet;
    }

    public Map<String, Set<Pair<String, String>>> getTransitions() {
        return this.transitions;
    }

    public List<String> getFinalStates() {
        return this.finalStates;
    }
}
