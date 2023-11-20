import static java.lang.String.valueOf;

/**
 * This application provides a text editor to write a document stored in memory.
 * Controller initiates the application and handles propagation of commands.
 */

public class Controller {
    private Document document;
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
            Command command = getCommand( userInput );
            Integer index = getIndex( userInput );
            if (command == null || index == null) continue;

            System.out.println( "Command: " + command );
            System.out.println( "Index: " + index );

            switch (command) {
                case EXIT:
                    System.exit( 0 );
                    break;
                case ADD:
                    add(index);
                    break;
                case DEL:
                    delete();
                    break;
                case FORMAT_FIX:
                    formatFix();
                    break;
                case FORMAT_RAW:
                    formatRaw();
                    break;
                case INDEX:
                    index();
                    break;
                case PRINT:
                    print();
                    break;
                case REPLACE:
                    System.out.println( "Enter search text:" );
                    break;
                case DUMMY:
                    dummy();
                    break;
                case HELP:
                    userInterface.prompt();
                    break;
                default:
                    userInterface.invalidCommand();
                    break;
            }
        }
    }

    private Integer getIndex(String userInput) {

        Integer index = Command.parseIndex( userInput );
        if (index == null) {
            userInterface.invalidIndex();
            return null;
        }

        return index;
    }

    private Command getCommand(String userInput) {
        Command command = Command.parseCommand( userInput );

        if (command == null) {
            userInterface.invalidCommand();
            return null;
        }

        return command;
    }

    private void dummy() {
    }

    public void add(int index) {
        userInterface.promptAdd();
        document.add( userInterface.getInput(), index );
    }


    public void delete(int index) {
        if (document.isEmpty()) {
            userInterface.documentEmpty();
            return;
        }

        document.delete( userInterface.getInput(), index );
    }

    public void formatFix() {

    }

    public void formatRaw() {

    }

    public void index() {

    }

    public void print() {

    }

    public void replace(String searchText, String targetText, int... index) {

    }

}
