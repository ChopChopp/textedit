import java.util.logging.Level;
import java.util.logging.Logger;

public class TextEditor {

    private Controller controller;
    private final Logger logger = Logger.getLogger( TextEditor.class.getName() );;

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.run();
    }

    protected void run() {
        logger.log( Level.INFO, "Initializing objects..." );

        controller = new Controller();
        controller.init();
    }
}
