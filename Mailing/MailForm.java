package Mailing;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;
import translation.Messages;


abstract public class MailForm {
	protected String mail;
	protected String subject;
	protected String text;
	protected final String to;

	abstract protected String getInitText();
	abstract protected String getInitSubject();
	
	abstract protected String getSubject();
	abstract protected String getText();
	
	protected boolean isSupportMail;

	public MailForm(String to) {
		this.to = to;
		isSupportMail = false;
	}
	
	protected String getDescription() {
		return ""; //$NON-NLS-1$
	}

	public void setContent(String subject, String text, String mail) {
		this.mail = mail;
		this.subject = subject;
		this.text = text;
	}
	
	public boolean sendMail() {
		String text = getText();
		String subject = getSubject();
		
		boolean result;
		
		if(isSupportMail) {
			result = MailSender.sendMailToSupportTeam(mail, subject, text);
		} else {
			result = MailSender.getSingleton().sendMailTo(mail, to, subject, text, null);
		}
		
		if(result) {
			DialogHandlerFrame.showInformationDialog(Program.myMainFrame, Messages.getString("MailForm.1"), Messages.getString("MailForm.2"), null); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			DialogHandlerFrame.showErrorDialog(Messages.getString("MailForm.3")); //$NON-NLS-1$
		}
		
		return result;
	}
	public void endAction() {}
}
