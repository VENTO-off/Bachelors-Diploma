package nuzp.yuliy_gorichenko.openhab_helper.exceptions;

public class OpenHabItemException extends Exception {
    private final String itemName;

    /**
     * Constructor
     */
    public OpenHabItemException(String itemName) {
        super("error.openhabitem");
        this.itemName = itemName;
    }

    /**
     * Get item name
     */
    public String getItemName() {
        return itemName;
    }
}
