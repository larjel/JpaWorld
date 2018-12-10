package userio;

/**
 * Interface for implementing enum based menus. Also contains helper methods for
 * displaying the menu and matching a numeric (int) to an enum.
 *
 * @author Lars Jelleryd
 */
public interface MenuInterface {

    String getText();

    int getNumeric();

    /**
     * Prints the menu title and all menu rows.
     *
     * @param title Menu title
     * @param enums Enum values (enum.values())
     */
    public static <E extends Enum<E> & MenuInterface> void printMenu(String title, E[] enums) {
        System.out.println("========== " + title + " ==========");
        for (E e : enums) {
            if (e.ordinal() != 0) { // Position 0 should always hold the 'invalid' enum
                System.out.println(e.getNumeric() + ". " + e.getText());
            }
        }
        System.out.print("> ");
    }

    /**
     * Matches given numeric (integer) to an enum.
     *
     * @param numeric Numeric (integer) to match to enum.
     * @param enums Enum values (enum.values()) to match to numeric.
     * @return The found enum or the enum at position 0 (which should be the
     * 'invalid' enum) if no match found.
     */
    public static <E extends Enum<E> & MenuInterface> E numericToEnum(int numeric, E[] enums) {
        for (E e : enums) {
            if (e.getNumeric() == numeric) {
                return e;
            }
        }
        return enums[0]; // Position 0 should always hold the 'invalid' enum
    }

}
