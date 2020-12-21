package Mailing;

import graphicalUserInterface.Program;
import translation.Messages;

public class MailAddRelationForm extends MailForm {

	public MailAddRelationForm() {
		super(MailSender.robertMail);
	}

	@Override
	protected String getSubject() {
		return  Messages.getString("MailAddRelationForm.0") + subject;  //$NON-NLS-1$
	}

	@Override
	protected String getText() {
		return  text; 
	}

	@Override
	protected String getInitText() {
		return Messages.getString("MailAddRelationForm.addRelationSubject"); //$NON-NLS-1$
	}

	@Override
	protected String getInitSubject() {
		return Program.myMainFrame.getDataPack().getCompanyInfo().getName() + " " + Messages.getString("MailAddRelationForm.addRelationIntroduction"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
