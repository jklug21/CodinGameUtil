package log;

/**
 * Simple util to write logs to stdout <br>
 * If {@link Log#captureInput(int)} is set to 'true' instead of logging, only the inputs provided by the game are written
 * to stderr for replaying the game state in a local IDE for debugging<br>
 * <br>
 * In order to re-run recorded inputs locally, set up your scanner based on a file reader from the stored inputs:<br>
 * <code>new Scanner(new FileReader("src/main/resources/in.txt"))</code>
 */
public class Log {

    private static boolean captureInputs = false;

    public static void enableInputCapturing(boolean captureInputs) {
        Log.captureInputs = captureInputs;
    }

    public static void log(Object logger, String message) {
        log(logger.getClass().getSimpleName(), message);
    }

    public static void log(String logger, String message) {
        if (!captureInputs) {
            System.err.printf("%s: %s%n", logger, message);
        }
    }

    public static void captureInput(int input) {
        if (captureInputs) {
            System.err.println(input);
        }
    }
}
