package graphicalUserInterface;


public class ExecutionMode {
	
	public enum Mode implements Comparable<Mode> 
	{
		debug, intern, extern;
	};
	
	private static final Mode mode = Mode.debug;
	
	public static boolean isDebug()
	{
		return mode == Mode.debug;
	}
	
	public static boolean isIntern()
	{
		return mode == Mode.debug || mode == Mode.intern;
	}
}
