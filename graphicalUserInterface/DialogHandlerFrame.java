package graphicalUserInterface;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import translation.Messages;

public class DialogHandlerFrame {

	public static void showErrorDialog(Component cmp, String message) {
		JOptionPane.showMessageDialog(cmp, message, Messages.getString("DialogHandlerFrame.0"), //$NON-NLS-1$
				JOptionPane.WARNING_MESSAGE);
	}

	public static void showErrorDialog(String message) {
		showErrorDialog(Program.myMainFrame, message);
	}

	public static int showYesNoCancelDialog(Component cmp, String message) {
		return JOptionPane.showConfirmDialog(cmp, message);
	}

	public static int showYesNoCancelDialog(String message) {
		return showYesNoCancelDialog(Program.myMainFrame, message);
	}

	public static int showYesNoDialog(Component cmp, String message) {
		return JOptionPane.showConfirmDialog(cmp, message,
				Messages.getString("DialogHandlerFrame.1"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
	}

	public static int showYesNoDialog(String message) {
		return showYesNoDialog(Program.myMainFrame, message);
	}

	public static void showInformationDialog(Component cmp, String title,
			String message, Icon icon) {
		JOptionPane.showMessageDialog(cmp, message, title,
				JOptionPane.INFORMATION_MESSAGE, icon);
	}

	public static void showInformationDialog(String string) {
		JOptionPane.showMessageDialog(Program.myMainFrame, string, Messages.getString("Version_1_0_2.DialogHandlerFrame.2"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		
	}
}
