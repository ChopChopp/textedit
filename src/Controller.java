import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This application provides a text editor to write a document stored in memory.
 * Controller initiates the application and handles propagation of commands.
 */

public class Controller {
    private final Document document;
    private final UserInterface userInterface;

    public Controller() {
        document = new Document();
        userInterface = new UserInterface();
    }

    public void init() {
        userInterface.welcome();
        userInterface.prompt();
        while (true) {
            String userInput = userInterface.getCommand();
            Command command = getCommand(userInput);
            Integer index = getIndex(userInput);

            if (command == null || index == null) continue;

            switch (command) {
                case EXIT -> System.exit(0);
                case ADD -> add(index);
                case DEL -> delete(index);
                case FORMAT_FIX -> formatFix(index);
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
        String commandString;

        if (parts.length > 1 && !isNumeric(parts[1])) {
            commandString = parts[0] + "_" + parts[1];
        } else {
            commandString = parts[0];
        }

        try {
            return Command.valueOf(commandString.toUpperCase());
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

        } else if (parts.length == 2) {
            if (isNumeric(parts[1])) return Integer.parseInt(parts[1]);
            else return -1;
        } else if (parts.length > 2 && isNumeric(parts[parts.length - 1])) {
            return Integer.parseInt(parts[parts.length - 1]);
        } else {
            userInterface.invalidCommandIndex();
            return null;
        }

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

    private void print() {
        if (document.getParagraphs().isEmpty()) {
            userInterface.documentEmpty();
            return;
        }
        if (document.getFormat() == Format.FIX) {
            TextEditor.logger.info("Printing document in fixed format");
            userInterface.printDocumentFix(document.getParagraphs(), document.getFixColumnWidth());
        } else if (document.getFormat() == Format.RAW) {
            TextEditor.logger.info("Printing document in raw format");
            userInterface.printDocumentRaw(document.getParagraphs());
        }
    }

    public void replace(int index) {
        if (document.getParagraphs().isEmpty())
            userInterface.documentEmpty();
        else if (index > document.getParagraphs().size())
            userInterface.invalidDocumentIndex();
        else {
            userInterface.promptSearchText();
            String searchText = userInterface.getInput();

            if (!document.hasSearchText(searchText, index - 1)) {
                userInterface.invalidSearchText();
                return;
            }

            userInterface.promptReplaceText();
            String replaceText = userInterface.getInput();

            document.replace(searchText, replaceText, index - 1);
        }
    }

    public void formatFix(int columnWidth) {
        if (columnWidth < 1) {
            userInterface.invalidCommandIndex();
            return;
        }
        document.setFormatFix(columnWidth);
    }

    public void formatRaw() {
        document.setFormatRaw();
    }

    public void index() {
        HashMap<String, HashSet<Integer>> index = generateIndex();
        document.printIndex(index);
    }

    /*
     * Create an index of all words that start with a capital letter and occur more than three times
     * @return HashMap<String, HashSet<Integer>> The index of words and their paragraph numbers
     */
    public HashMap<String, HashSet<Integer>> generateIndex() {
        HashMap<String, HashSet<Integer>> index = new HashMap<>();
        HashMap<String, Integer> wordCount = countWords();

        for (int i = 0; i < document.getParagraphs().size(); i++) {
            String[] words = document.getParagraphs().get(i).getText().split("\\s+");
            for (String word: words) {
                if (Character.isUpperCase(word.charAt(0)) && wordCount.get(word) > 3) {
                    index.putIfAbsent(word, new HashSet<>());
                    index.get(word).add(i + 1);
                }
            }
        }
        return index;
    }

    /*
     * Helper function that counts the frequency of each word in the whole document.
     * Used in generateIndex() to check if a word occurs more than three times before adding it to the index.
     * @return HashMap<String, Integer> The word and its frequency
     */
    private HashMap<String, Integer> countWords() {
        HashMap<String, Integer> wordCount = new HashMap<>();
        for (Paragraph paragraph: document.getParagraphs()) {
            String[] words = paragraph.getText().split("\\s+");
            for (String word: words) {
                if (Character.isUpperCase(word.charAt(0))) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }
        return wordCount;
    }
}
