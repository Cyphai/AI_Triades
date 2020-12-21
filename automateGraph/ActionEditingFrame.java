package automateGraph;

import javax.swing.JFrame;
import automate.*;


public class ActionEditingFrame extends JFrame {

	public static final long serialVersionUID = 54365765;
	
	
	
	protected static ActionEditingFrame instance;
	protected Action currentAction;
	
	
	public static void editAction(Action a)
	{
		
	}
	
	protected void setAction(Action action)
	{
		if (action != currentAction)
		{
			removeAll();
			add(action.toComponent());
			
		}
	}
	
	
	
}
