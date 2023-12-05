/*
 * Manages the user interface for the text editor.
 */

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private static final Scanner reader = new Scanner(System.in);

    /**
     * Return user input (command) in uppercase.
     *
     * @return The command
     */
    public String getCommand() {
        System.out.print("\n> ");
        return reader.nextLine().toUpperCase();
    }

    /**
     * Return the text which has to be replaced.
     *
     * @return The replacement text
     */
    public String getInput() {
        return reader.nextLine();
    }

    /**
     * Prints a welcome message to the console.
     */
    public void printWelcome() {
        System.out.println("Welcome to the text editor!");
    }

    /**
     * Print available commands
     */
    public void printHelp() {
        System.out.println("'ADD [n] <text>'      Adds a paragraph at given index");
        System.out.println("'DEL [n]'             Deletes a paragraph at given index ");
        System.out.println("'DUMMY [n]'           Adds a dummy paragraph");
        System.out.println("'EXIT'                Exit program");
        System.out.println("'FORMAT RAW'          Sets the output format with preceding paragraph number");
        System.out.println("'FORMAT FIX <b>'      Sets maximum column width");
        System.out.println("'INDEX'               Prints a dictionary of most common words");
        System.out.println("'PRINT'               Prints the entire document");
        System.out.println("'REPLACE [n]'         Replaces the specified target string with a replacement string");
        System.out.println("'HELP'                Shows this help message");
    }

    /**
     * ask the user for paragraph text
     */
    public void promptAdd() {
        System.out.println("Enter paragraph text:");
    }

    /**
     * Inform that user hasn't entered any text.
     */
    public void documentEmpty() {
        System.err.println("Document is empty.");
    }

    /**
     * User has entered an invalid command.
     */
    public void invalidCommand() {
        System.err.println("Invalid command.");
    }

    /**
     * User has entered an invalid command index.
     */
    public void invalidCommandIndex() {
        System.err.println("Invalid command index.");
    }

    /**
     * User has entered an invalid document index.
     */
    public void invalidDocumentIndex() {
        System.err.println("Invalid document index. Index does not exist.");
    }

    /**
     * Print output in a format with preceding paragraph number.
     */
    public void printDocumentRaw(ArrayList<Paragraph> paragraphs) {
        for (int i = 0; i < paragraphs.size(); i++) {
            System.out.println("<" + (i + 1) + ">: " + paragraphs.get(i).getText());
        }
    }

    /**
     * Set the output format to an output with a maximum column width of b characters. The line breaking behavior is as follows:
     * -Line breaks are allowed only after a space.
     * -The space after which the line is broken does not count towards the line length. It is still output on the current line,
     * even if it may not fit.
     * -If no break point is found within the column width after a break, a line break may be made after the column width.
     */
    public void printDocumentFix(ArrayList<Paragraph> paragraphs, int columnWidth) {
        for (Paragraph paragraph: paragraphs) {
            String formattedParagraph = formatParagraphFix(paragraph.getText(), columnWidth);
            System.out.println(formattedParagraph);
        }
    }

    /**
     * Handles the formatting of each paragraph into lines of a specified maximum length
     *
     * @param text        The text to be formatted
     * @param columnWidth The maximum column width
     * @return The formatted text
     */
    private String formatParagraphFix(String text, int columnWidth) {
        String[] words = text.split("\\s+");
        StringBuilder formattedText = new StringBuilder();
        int currentLineLength = 0;
        for (String word: words) {
            if (currentLineLength + word.length() > columnWidth) {
                formattedText.append('\n');
                currentLineLength = 0;
            }
            formattedText.append(word);
            currentLineLength += word.length();
            if (currentLineLength < columnWidth) {
                formattedText.append(' ');
                currentLineLength++;
            }
        }
        return formattedText.toString();
    }

    /**
     * Request user to enter a search text.
     */
    public void promptSearchText() {
        System.out.println("Enter search text:");
    }

    /**
     * Request user to enter a replacement text.
     */
    public void promptReplaceText() {
        System.out.println("Enter replacement text:");
    }

    /**
     * Inform the user that the search text was not found in the document.
     */
    public void invalidSearchText() {
        System.err.println("Search text not found in target paragraph.");
    }

    public void exit() {
        System.out.println("Exiting program...");
    }
}