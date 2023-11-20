import java.util.ArrayList;

/**
 * Document holds the state of text the user writes.
 * It stores the provided text in a list of paragraphs.
 */

public class Document {
    ArrayList<Paragraph> paragraphs = new ArrayList<>();

    public void add(String text, int index) {
        if (index != -1) {
            paragraphs.add( new Paragraph( text ) );
        } else {
            paragraphs.add( index, new Paragraph( text ) );
        }
    }

    public boolean isEmpty() {
        return paragraphs.isEmpty();
    }

    public void delete(int index) {
        if (index == -1)
            paragraphs.remove( paragraphs.size() - 1 );
        else
            paragraphs.remove( index );
    }
}
