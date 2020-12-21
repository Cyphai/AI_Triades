package graphicalUserInterface;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * The <code>TrampolineAction</code> call the command method of the controller
 * when the actionPerformed method is invoked.
 */
public class TrampolineAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final Object controller;

	private transient Method method;

	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			if (method == null) {
				method = controller.getClass().getMethod(
						(String) getValue(Action.ACTION_COMMAND_KEY),
						new Class[] { ActionEvent.class });
			}
			method.invoke(controller, new Object[] { e });
		} catch (NoSuchMethodException ex1) {
			throw new RuntimeException(ex1);
		} catch (InvocationTargetException ex2) {
			ex2.getTargetException().printStackTrace();
			throw new RuntimeException(ex2.getTargetException());
		} catch (Exception ex3) {
			throw new RuntimeException(ex3);
		}
	}

	/**
	 * TrampolineAction constructor.
	 * 
	 * @param command
	 *            method name
	 * @param name
	 *            action name
	 * @param icon
	 *            action icon
	 * @param controller
	 *            controller object invoked on actionPerformed event.
	 */
	public TrampolineAction(String command, String name, Icon icon,
			final Object controller) {
		super(name, icon);
		this.putValue(Action.ACTION_COMMAND_KEY, command);
		this.controller = controller;
	}
}
