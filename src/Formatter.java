/*
 * This class manages the formatter objects.
 */
public class Formatter {
    private Format format = Format.RAW;
    private int fixColumnWidth;

    public void setFormat(Format format) {
        this.format = format;
    }

    public void setFixColumnWidth(int fixColumnWidth) {
        this.fixColumnWidth = fixColumnWidth;
    }

    public Format getFormat() {
        return format;
    }

    public int getFixColumnWidth() {
        return fixColumnWidth;
    }
}