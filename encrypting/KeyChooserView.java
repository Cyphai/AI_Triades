package encrypting;

import graphicalUserInterface.Program;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

public class KeyChooserView extends JWindow {
	private static final long serialVersionUID = -2718163511630088923L;
	
	static public SecretKeySpec createView() {
		int firstIndex = new Integer(JOptionPane.showInputDialog(Program.myMainFrame, "Premier nombre :"));
		int secondIndex = new Integer(JOptionPane.showInputDialog(Program.myMainFrame, "Deuxième nombre :"));
		return new KeyE(firstIndex, secondIndex).getKey();
	}
}
