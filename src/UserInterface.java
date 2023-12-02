/*
 * Manages the user interface for the text editor.
 *
 */

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private static final Scanner reader = new Scanner(System.in);

    public void welcome() {
        System.out.println("Welcome to the text editor!");
    }

    public String getCommand() {
        System.out.print("\n> ");
        return reader.nextLine().toUpperCase();
    }

    public String getInput() {
        return reader.nextLine();
    }

    public void prompt() {
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

    public void promptAdd() {
        System.out.println("Enter paragraph text:");
    }

    public void documentEmpty() {
        System.err.println("Document is empty.");
    }

    public void invalidCommand() {
        System.err.println("Invalid command.");
    }

    public void invalidCommandIndex() {
        System.err.println("Invalid command index.");
    }

    public void invalidDocumentIndex() {
        System.err.println("Invalid document index. Index does not exist.");
    }

    public void printDocumentRaw(ArrayList<Paragraph> paragraphs) {
        for (int i = 0; i < paragraphs.size(); i++) {
            System.out.println("<" + (i + 1) + ">: " + paragraphs.get(i).getText());
        }
    }

    public void printDocumentFix(ArrayList<Paragraph> paragraphs, int columnWidth) {
        for (Paragraph paragraph : paragraphs) {
            String text = paragraph.getText();
            String[] line = text.split("\\s+");
            int currentLineLength = 0;

            for (String word : line) {
                // Überprüfe, ob das Wort in die aktuelle Zeile passt.
                // Wenn nein, dann füge einen Zeilenumbruch hinzu.
                if (currentLineLength + word.length() > columnWidth) {
                    System.out.println(); // Zeilenumbruch
                    currentLineLength = 0; // Zurücksetzen der Länge für die neue Zeile
                }

                // Das Wort wird zur aktuellen Zeile hinzugefügt.
                System.out.print(word);
                currentLineLength += word.length();

                // Füge ein Leerzeichen hinzu, wenn nach dem Hinzufügen des Worts noch Platz ist,
                // und wenn es nicht das letzte Wort in der Zeile ist.
                if (currentLineLength < columnWidth) {
                    System.out.print(" ");
                    currentLineLength++; // Inkludiere das Leerzeichen in die Längenberechnung
                }
            }

            // Füge nach jedem Absatz einen Zeilenumbruch hinzu.
            System.out.println();
        }

    }

    public void promptSearchText() {
        System.out.println("Enter search text:");
    }

    public void promptReplaceText() {
        System.out.println("Enter replacement text:");
    }

    public void invalidSearchText() {
        System.err.println("Search text not found in target paragraph.");
    }


}
