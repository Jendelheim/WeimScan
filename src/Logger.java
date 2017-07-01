/**
 * Placeholder Logger
 * 
 * author: Gustav Henning
 * 
 */
public class Logger {

	public static Level LEVEL = Level.INFO;

	public enum Level {
		INFO, FINE, FINEST
	}

	/**
	 * Returns true iff the level of the desired action is at least as desirable
	 * as the preset value LEVEL in this class with the following priorities: --
	 * INFO > FINE > FINEST where FINEST will give you maximum information.
	 * 
	 * @param l
	 * @return
	 */
	public static boolean is(Level l) {
		switch (LEVEL) {
		case INFO:
			return l == Level.INFO;
		case FINE:
			return l != Level.FINEST;
		case FINEST:
			return true;
		default:
			System.err.println("Debug.java: Illegal debug level. Exiting...");
			System.exit(1);
		}
		return false;
	}

	/*
	 * Used for the most top level messages of a normal run
	 */
	public static void info(String message) {
		if (is(Level.INFO)) {
			System.out.println(message);
		}
	}

	/*
	 * Provides detail to the current progress of the run
	 */
	public static void fine(String message) {
		if (is(Level.FINE)) {
			System.out.println(message);
		}
	}

	/*
	 * Provides in depth detailed information about the process.
	 */
	public static void finest(String message) {
		if (is(Level.FINEST)) {
			System.out.println(message);
		}
	}

	/**
	 * We always print errors. Used primarily for convention until further
	 * requirements are necessary.
	 * 
	 * @param message
	 */
	public static void err(String message) {
		System.err.println(message);
	}
}
