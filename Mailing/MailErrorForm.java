package Mailing;

import graphicalUserInterface.Program;
import translation.Messages;


public class MailErrorForm extends MailForm {

	private final Throwable exception;

	public MailErrorForm(Throwable e) {
		super(MailSender.supportMail);
		exception = e;
		isSupportMail = true;
	}
	
	@Override
	protected String getInitText() {
		String text = Messages.getString("MailErrorForm.0"); //$NON-NLS-1$
		text += exception.toString() + "<-\n"; //$NON-NLS-1$
		for(int i = 0 ; i < exception.getStackTrace().length ; i++) {
			text += exception.getStackTrace()[i].toString() + "\n"; //$NON-NLS-1$
		}
		return  text;
	}

	@Override
	protected String getInitSubject() {
		return Messages.getString("MailErrorForm.3"); //$NON-NLS-1$
	}

	@Override
	protected String getSubject() {
		return subject;
	}

	@Override
	protected String getText() {
		return text;
	}

	@Override
	public void endAction() {
		Program.askContinueOnErreur();
	}
}
