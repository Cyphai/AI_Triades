package automate;

import javax.swing.*;

public class EmptyAction extends Action {

	public JComponent toComponent()
	{
		return new JTextField("Action vide");
	}
	
}
