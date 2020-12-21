package graphicalUserInterface;



public class CreatorLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			Logger.getInstance().setLoggerCreator(true);
		}

		String[] arg = { "creator" }; //$NON-NLS-1$
		Program.main(arg);
	}
}
