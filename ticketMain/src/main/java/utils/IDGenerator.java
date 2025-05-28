package utils;

/**
 * Utility class for generating unique tickets IDs.
 * Uses a simple icrementiing counter apporcach.
 * In a real system, this might us UUID or database sequences
 *
 * This class uses the Singleton pattern to ensure only one isntance
 * exists throguht the application lifecycle
 *
 * @author rosslankoulli
 * @version 1.0
 */
public class IDGenerator {
    // Singleton isntance
    private static IDGenerator instance;

    // Counter for generating IDs, starts at 1000
    private int currentID;

    /**
     * Private construcotr to prevent direct insatiation
     */

    private IDGenerator() {

        this.currentID = 1000;
    }

    /** Gets the singleton isntance of IDGenerator
     *
     * @return The single instance of IDGenerator
     */

    public static synchronized IDGenerator getInstance() {
        if(instance == null) {
            instance = new IDGenerator();
        }
        return instance;
    }

    /**
     * Generates the next unique ticket ID
     *
     * @return A unique ticket ID
     */
    public synchronized int generateId() {

        return currentID++;
    }

    /**
     * Gets the current ID without incrementing
     * USeful for debugging or display purposes
     *
     * @return The current ID value
     */
    public synchronized int getCurrentID() {
        return currentID;
    }

    /**
     * Resets the ID counter to initial value
     * WARNING: Use only for testing or system reset
     */

    public synchronized void reset() {
        currentID = 1000;
    }
}
