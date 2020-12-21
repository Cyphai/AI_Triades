package Mailing;

import java.util.Vector;

import translation.Messages;

public class MailFormFactory {
	private static final String relationForm = Messages.getString("MailFormFactory.addRelationSubject"); //$NON-NLS-1$
	private static final String informationForm = Messages.getString("MailFormFactory.informationRequestSubject"); //$NON-NLS-1$
	
	public static MailForm getInstance(Object name) {
		MailForm mailForm = null;
		
		if(name == relationForm) {
			mailForm = new MailAddRelationForm();
		} else {
			throw new IllegalArgumentException(name + " is not a MailForm type !"); //$NON-NLS-1$
		}
		
		return mailForm;
	}
	
	public static Object[] getFormTypes() {
		Vector<String> types = new Vector<String>();
		
		types.add(relationForm);
		types.add(informationForm);
		
		return types.toArray();
	}
}
