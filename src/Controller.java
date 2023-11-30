/**
 * This application provides a text editor to write a document stored in memory.
 * Controller initiates the application and handles propagation of commands.
 */

public class Controller {
    private Document document;
    private final UserInterface userInterface;
    private Command command;

    public Controller() {
        document = new Document();
        userInterface = new UserInterface();
    }

    public void init() { //CHANGED: switch statement to switch enhanced statement
        userInterface.welcome();
        userInterface.prompt();
        while (true) {
            String userInput = userInterface.getCommand();
            command = getCommand(userInput);
            Integer index = getIndex(userInput);

            if (command == null || index == null) continue;

            switch (command) {
                case EXIT -> System.exit(0);
                case ADD -> add(index);
                case DEL -> delete(index);
                case FORMAT_FIX -> formatFix();
                case FORMAT_RAW -> formatRaw();
                case INDEX -> index();
                case PRINT -> print();
                case REPLACE -> replace(index);
                case DUMMY -> dummy(index);
                case HELP -> userInterface.prompt();
                default -> userInterface.invalidCommand();
            }
        }
    }

    private Command getCommand(String userInput) {
        String[] parts = userInput.split("\\s+");
        try {
            return Command.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            userInterface.invalidCommand();
            return null;
        }
    }

    /*
     * Get the index from the command string
     * Returns -1 if no index is provided by user
     * Returns null if the index is not numeric
     * @param commandString The command string
     */
    private Integer getIndex(String userInput) {
        String[] parts = userInput.split("\\s+");

        if (parts.length == 1) {
            return -1;
        } else if (parts.length == 2 && isNumeric(parts[1])) {
            return Integer.parseInt(parts[1]);
        }
        userInterface.invalidCommandIndex();
        return null;
    }

    /*
     * Check if the string is numeric[0-9] via regex and disclose 0, as array needs to start at 1
     * @param str The string to check
     */
    private boolean isNumeric(String str) {
        return !str.equals("0") && str.matches("\\d+");
    }

    public void add(int index) {
        if (index > document.getParagraphs().size()) {
            userInterface.invalidDocumentIndex();
            return;
        }
        userInterface.promptAdd();
        document.addParagraph(userInterface.getInput(), index - 1);
    }

    public void delete(int index) {
        if (document.getParagraphs().isEmpty())
            userInterface.documentEmpty();
        else if (index > document.getParagraphs().size())
            userInterface.invalidDocumentIndex();
        else
            document.deleteParagraph(index - 1);
    }

    private void dummy(int index) {
        if (index > document.getParagraphs().size()) {
            userInterface.invalidDocumentIndex();
            return;
        }
        document.addDummy(index - 1);
    }

    public void print() {
        if (document.getParagraphs().isEmpty()) {
            userInterface.documentEmpty();
            return;
        }
        userInterface.printDocument(document.getParagraphs());
    }

    public void replace(int index) {
        if (document.getParagraphs().isEmpty())
            userInterface.documentEmpty();
        else if (index > document.getParagraphs().size())
            userInterface.invalidDocumentIndex();
        else {
            userInterface.promptSearchText();
            String searchText = userInterface.getInput();

            if (!document.hasSearchText(searchText, index -1)) {
                userInterface.invalidSearchText();
                return;
            }

            userInterface.promptReplaceText();
            String replaceText = userInterface.getInput();

            document.replace(searchText, replaceText, index -1);
        }
    }

    public void formatFix() {

    }

    public void formatRaw() {

    }

    public void index() {
        HashMap<String, HashSet<Integer>> index = generateIndex();
        document.printIndex(index);
    }

    public HashMap<String, HashSet<Integer>> generateIndex() {
        HashMap<String, HashSet<Integer>> index = new HashMap<>();
        HashMap<String, Integer> wordCount = countWords();
        ArrayList<Paragraph> paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            String[] words = paragraphs.get(i).getText().split("\\s+");
            for (String word : words) {
                if (Character.isUpperCase(word.charAt(0)) && wordCount.get(word) > 3) {
                    index.putIfAbsent(word, new HashSet<>());
                    index.get(word).add(i + 1);
                }
            }
        }
        return index;
    }

    /*
     *  Hilfsfunktion, die die Häufigkeit jedes Wortes in den Absätzen zählt, und
     *  diese Funktion dann in der generateIndex-Methode verwenden, um zu überprüfen,
     *  ob ein Wort mehr als dreimal vorkommt, bevor es zum Index hinzugefügt wird
     */
    private HashMap<String, Integer> countWords() {
        HashMap<String, Integer> wordCount = new HashMap<>();
        ArrayList<Paragraph> paragraphs = document.getParagraphs();
        for (Paragraph paragraph : paragraphs) {
            String[] words = paragraph.getText().split("\\s+");
            for (String word : words) {
                if (Character.isUpperCase(word.charAt(0))) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }
        return wordCount;
    }
}
