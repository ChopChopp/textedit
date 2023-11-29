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
                case REPLACE -> System.out.println("Enter search text:");
                case DUMMY -> dummy();
                case HELP -> userInterface.prompt();
                default -> userInterface.invalidCommand();
            }
        }
    }

    private Command getCommand(String userInput) {
        String[] parts = userInput.split( "\\s+" );
        try {
            return Command.valueOf( parts[0] );
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
        String[] parts = userInput.split( "\\s+" );

        if (parts.length == 1) {
            return -1;
        } else if (parts.length == 2 && isNumeric( parts[1] )) {
            return Integer.parseInt( parts[1] );
        }
        userInterface.invalidCommandIndex();
        return null;
    }

    /*
     * Check if the string is numeric[0-9] via regex
     * @param str The string to check
     */
    private boolean isNumeric(String str) {
        return !str.equals("0") && str.matches("\\d+");
    }

    private void dummy() {
    }

    public void add(int index) {
        if (index > document.getParagraphs().size() ) {
            userInterface.invalidDocumentIndex();
            return;
        }
        userInterface.promptAdd();
        document.addParagraph( userInterface.getInput(), index );
    }


    public void delete(int index) {
        if (document.isEmpty()) {
            userInterface.documentEmpty();
            return;
        }

        document.deleteParagraph( index );
    }

    public void formatFix() {

    }

    public void formatRaw() {

    }

    public void index() {

    }

    public void print() {
        if (document.isEmpty()) {
            userInterface.documentEmpty();
            return;
        }
        userInterface.printDocument(document.getParagraphs());
    }

    public void replace(String searchText, String targetText, int... index) {

    }

}
