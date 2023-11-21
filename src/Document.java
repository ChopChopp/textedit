import java.util.ArrayList;
import java.util.HashMap;

/**
 * Document holds the state of text the user writes.
 * It stores the provided text in a list of paragraphs.
 */

public class Document {
    private ArrayList<Paragraph> paragraphs = new ArrayList<>();
    private Formatter formatter = new Formatter();
    private final String DUMMY = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    public void addParagraph(String text, int index) {
        if (index != -1) {
            paragraphs.add(new Paragraph(text));
        } else {
            paragraphs.add(index, new Paragraph(text));
        }
    }

    public void deleteParagraph(int index) {
        if (index == -1)
            paragraphs.remove(paragraphs.size() - 1);
        else
            paragraphs.remove(index);
    }

    public boolean isEmpty() {
        return paragraphs.isEmpty();
    }

    private void addDummy(int index) {
    }

    public void setFormatFix(int columnWidth) {

    }

    public void setFormatRaw() {

    }

    public HashMap<Paragraph, int[]> getIndex() {
        return null;
    }

    public void printDocument() {

    }

    public void replace(String searchText, String targetText, int index) {

    }
}
