package userio;

import java.util.Scanner;

/**
 * Get command prompt input using Scanner. Wrap input methods and handle
 * clearing of buffers and potential exceptions.
 */
public class SystemInput {

    // Private to prevent instantiation. Only static methods in this class.
    private SystemInput() {
    }

    private final static Scanner SC = new Scanner(System.in);

    /**
     * Display error message. Call after input exception.
     */
    private static void handleBadInput() {
        System.out.print("Bad input. Please reenter > ");
    }

    public static double getDouble() {
        while (true) {
            try {
                return Double.parseDouble(getString());
            } catch (NumberFormatException e) {
                handleBadInput();
            }
        }
    }

    public static double getDoubleAbortOnEmpty() throws SystemInputAbortedException {
        while (true) {
            try {
                return Double.parseDouble(getStringAbortOnEmpty());
            } catch (NumberFormatException e) {
                handleBadInput();
            }
        }
    }

    public static int getInt() {
        while (true) {
            try {
                return Integer.parseInt(getString());
            } catch (NumberFormatException e) {
                handleBadInput();
            }
        }
    }

    public static int getIntAbortOnEmpty() throws SystemInputAbortedException {
        while (true) {
            try {
                return Integer.parseInt(getStringAbortOnEmpty());
            } catch (NumberFormatException e) {
                handleBadInput();
            }
        }
    }

    public static String getString() {
        return SC.nextLine().trim();
    }

    public static String getStringAbortOnEmpty() throws SystemInputAbortedException {
        String input = getString();
        if (input.isEmpty()) {
            throw new SystemInputAbortedException("Aborted due to empty input");
        }
        return input;
    }
    
}
