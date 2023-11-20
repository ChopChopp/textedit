/**
 * Command handles the possible commands which can
 * be used by the user to interact with the document
 */

public enum Command {
    ADD,
    DEL,
    DUMMY,
    EXIT,
    FORMAT_FIX,
    FORMAT_RAW,
    INDEX,
    PRINT,
    REPLACE,
    HELP;

    public static Command parseCommand(String commandString) {
        String[] parts = commandString.split( "\\s+" ); // Split the command string by whitespace

        if (parts.length == 1) {
            try {
                return valueOf( parts[0] );
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else if (parts.length == 2) {
            return valueOf( parts[0] );
        }
        return null;
    }

    /*
     * Get the index from the command string
     * Returns -1 if no index is provided by user
     * Returns null if the index is not numeric
     * @param commandString The command string
     */
    public static Integer parseIndex(String commandString) {
        String[] parts = commandString.split( "\\s+" ); // Split the command string by whitespace

        if (parts.length == 1) {
            return -1;
        } else if (parts.length == 2 && isNumeric( parts[1] )) {
            return Integer.parseInt( parts[1] );
        }
        return null;
    }

    /*
     * Check if the string is numeric via regex
     * @param str The string to check
     */
    private static boolean isNumeric(String str) {
        return str.matches( "\\d+" );
    }
}