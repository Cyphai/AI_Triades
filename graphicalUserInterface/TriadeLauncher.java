package graphicalUserInterface;

public class TriadeLauncher {

	protected int test;

	public static void main(String[] args) {
		if (args.length == 0) {
			Logger.getInstance().setLoggerTriade(true);
		}

		String[] arg = null;
		Program.main(arg);
	}
}
