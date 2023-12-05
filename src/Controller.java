import java.util.*;
import java.util.logging.Handler;

/**
 * This application provides a text editor to write a document stored in memory.
 * Controller initiates the application and handles propagation of commands.
 */

public class Controller {
    private final Document document;
    private final UserInterface userInterface;

    /*
     * default constructor
     */
    public Controller() {
        document = new Document();
        userInterface = new UserInterface();
    }

    /*
     * constructor with parameters, for testing
     */
    public Controller(Document document, UserInterface userInterface) {
        this.document = document;
        this.userInterface = userInterface;
    }

    /*
     * Initialize the application and handle propagation of commands
     */
    public void init() {
        userInterface.printWelcome();
        userInterface.printHelp();
        while (true) {
            String userInput = userInterface.getCommand();
            TextEditor.logger.info("Received user input: " + userInput);

            Command command = getCommand(userInput);
            if (command == null) continue;

            Integer index = getIndex(userInput);

            switch (command) {
                case EXIT -> exit();
                case ADD -> add(index);
                case DEL -> delete(index);
                case FORMAT_FIX -> formatFix(index);
                case FORMAT_RAW -> formatRaw();
                case INDEX -> index();
                case PRINT -> print();
                case REPLACE -> replace(index);
                case DUMMY -> dummy(index);
                case HELP -> userInterface.printHelp();
                default -> userInterface.invalidCommand();
            }
        }
    }

    /*
     * Close the logger and exit the program
     */
    public void exit() {
        TextEditor.logger.info("Exiting application");
        userInterface.exit();
        for (Handler handler: TextEditor.logger.getHandlers()) {
            handler.close();
        }
        System.exit(0);
    }

    /*
     * Get the command from the user entered input
     * Concatenates the command with _ if the command is two words (e.g. FORMAT FIX)
     * @param userInput The user input
     */
    public Command getCommand(String userInput) {
        String[] userInputParts = userInput.split("\\s+");
        StringBuilder commandString = new StringBuilder();

        for (String part: userInputParts) {
            if (!isNumeric(part)) {
                commandString.append(part.toUpperCase()).append("_");
            }
        }

        if (!commandString.isEmpty()) {
            TextEditor.logger.fine("Removing trailing underscore from command");
            commandString.setLength(commandString.length() - 1);
        }

        try {
            TextEditor.logger.info("Parsed command: " + commandString.toString().toUpperCase());
            return Command.valueOf(commandString.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            TextEditor.logger.warning("Invalid command");
            userInterface.invalidCommand();
            return null;
        }
    }

    /*
     * Get the index from the user entered input
     * Returns -1 if no index is provided by user
     * @param userInput The user input
     */
    public Integer getIndex(String userInput) {
        String[] parts = userInput.split("\\s+");

        if (isNumeric(parts[parts.length - 1])) {
            TextEditor.logger.info("Returning index " + parts[parts.length - 1]);
            return Integer.parseInt(parts[parts.length - 1]);
        } else {
            TextEditor.logger.info("No index provided. Returning -1");
            return -1;
        }

    }

    /*
     * Check if the string is numeric[1-9] via regex. Disclosing 0 as array needs to start at 1.
     * @param str The string to check
     */
    private boolean isNumeric(String str) {
        return !str.equals("0") && str.matches("[0-9]+");
    }

    /*
     * Add a paragraph to the document
     * @param index The index of the paragraph to add the new paragraph after
     */
    public void add(int index) {
        TextEditor.logger.info("Executing command ADD");
        if (index > document.getSize()) {
            userInterface.invalidDocumentIndex();
            return;
        }
        userInterface.promptAdd();
        document.addParagraph(userInterface.getInput(), index - 1);
    }

    /*
     * Delete a paragraph from the document
     * @param index The index of the paragraph to delete
     */
    public void delete(int index) {
        TextEditor.logger.info("Executing command DEL");
        if (document.getParagraphs().isEmpty())
            userInterface.documentEmpty();
        else if (index > document.getParagraphs().size())
            userInterface.invalidDocumentIndex();
        else
            document.deleteParagraph(index - 1);
    }

    /*
     * Add a dummy paragraph to the document
     * @param index The index of the paragraph to add the dummy paragraph after
     */
    public void dummy(int index) {
        TextEditor.logger.info("Executing command DUMMY");
        if (index > document.getParagraphs().size()) {
            userInterface.invalidDocumentIndex();
            return;
        }
        document.addDummy(index - 1);
    }

    /*
     * Print the entire document
     */
    public void print() {
        TextEditor.logger.info("Executing command PRINT");
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

    /*
     * Replace the target string with a replacement string
     * @param index The index of the paragraph to replace the string in
     */
    public void replace(int index) {
        TextEditor.logger.info("Executing command REPLACE");
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

    /*
     * Set the output format with a maximum column width
     * @param columnWidth The maximum column width
     */
    public void formatFix(int columnWidth) {
        TextEditor.logger.info("Executing command FORMAT FIX");
        if (columnWidth < 1) {
            userInterface.invalidCommandIndex();
            return;
        }
        document.setFormatFix(columnWidth);
    }

    /*
     * Set the output format with preceding paragraph number
     */
    public void formatRaw() {
        TextEditor.logger.info("Executing command FORMAT RAW");
        document.setFormatRaw();
    }

    /*
     * Print an index of all words that start with a capital letter and occur more than three times
     */
    public void index() {
        TextEditor.logger.info("Executing command INDEX");
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
