import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class TextEditor {

    private Controller controller;
    final static Logger logger = Logger.getLogger(TextEditor.class.getName());

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %5$s%6$s%n");
        TextEditor textEditor = new TextEditor();
        FileHandler fh;

        try {
            fh = new FileHandler("./TextEditor.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.setUseParentHandlers(false); // disable console output
            logger.addHandler(fh);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception occurred", e);
        }
        textEditor.run();
    }

    public void run() {
        logger.info("Initializing controller...");

        controller = new Controller();
        controller.init();
    }
}
