package Mailing;

import graphicalUserInterface.Logger;
import graphicalUserInterface.Program;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import translation.Messages;
import dataPack.DataPack;

public class MailSender {
	private final Properties props;
	private final Session session;
	private final Message message;
	
	private final static Timer timer = new Timer(true);

	public static String robertMail = "contact.triades@gmail.com"; //$NON-NLS-1$
	public static String supportMail = "contact.triades@gmail.com"; //$NON-NLS-1$

	private static String defaultUsername = "mail.triades@gmail.com"; //$NON-NLS-1$
	private static String defaultPassword = "Hy7.lKo(9"; //$NON-NLS-1$

	private static final MailSender defaultMailSender = new MailSender(
			defaultUsername, defaultPassword);

	public MailSender(final String username, final String password) {
		// smtp properties
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //$NON-NLS-1$ //$NON-NLS-2$
		props.put("mail.smtp.socketFactory.port", "465"); //$NON-NLS-1$ //$NON-NLS-2$
		props.put("mail.smtp.socketFactory.class", //$NON-NLS-1$
		"javax.net.ssl.SSLSocketFactory"); //$NON-NLS-1$
		props.put("mail.smtp.auth", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		props.put("mail.smtp.port", "465"); //$NON-NLS-1$ //$NON-NLS-2$

		MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); //$NON-NLS-1$
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); //$NON-NLS-1$
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); //$NON-NLS-1$
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); //$NON-NLS-1$
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); //$NON-NLS-1$
		CommandMap.setDefaultCommandMap(mc);

//		session = Session.getInstance(props);
		
		// authentification
		session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		message = new MimeMessage(session);
	}
	
	public boolean sendMailTo(String from, String to, String subject,
			String text, Vector<String> files)
	{
		return sendMailTo(from, to, subject, text, files, false);
	}

	public boolean sendMailTo(String from, String to, String subject,
			String text, Vector<String> files, boolean mute) {
		try {
			MimeMultipart body = new MimeMultipart();

			if (files != null) {
				for (String filename : files) {
					try {
						if(filename != null) {
							File file = new File(filename);
							if(file.exists() == false) {
								System.out.println(Messages.getString("Version_0_9_9_9.MailSender.5") + filename + Messages.getString("Version_0_9_9_9.MailSender.6")); //$NON-NLS-1$ //$NON-NLS-2$
								files.remove(filename);
								continue;
							}

							System.out.println("Add file to mail : " + filename + "(" + filename.substring(filename.lastIndexOf(File.separatorChar)+1) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							MimeBodyPart dataBody = new MimeBodyPart();
							dataBody.attachFile(file.getPath());

							body.addBodyPart(dataBody);
							
							MimeMultipart test = new MimeMultipart();
							MimeBodyPart testt = new MimeBodyPart();
							testt.setText("File : " + filename.substring(filename.lastIndexOf('/')+1)); //$NON-NLS-1$
							test.addBodyPart(testt);
						}
					} catch (Exception e) {
						if (!mute)
							System.out.println(Messages.getString("Version_0_9_9_9.MailSender.11") + filename); //$NON-NLS-1$
						e.printStackTrace();
					}
				}
			}

		    MimeBodyPart textBody = new MimeBodyPart();
		    textBody.setText(text);
		    body.addBodyPart(textBody);
			
			// set message
			message.setContent(body);
			System.out.println("Reply to : " + from); //$NON-NLS-1$
			message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(to));
			message.setReplyTo(InternetAddress.parse(from));
			message.setSubject(subject);
			message.setSentDate(new Date());

			Transport.send(message);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}

	private static String createSubject(String subject) {
		return Messages.getString("MailSender.subjectPrefix") + subject; //$NON-NLS-1$
	}

	static public boolean sendMailToRobert(String from, String subject,
			String text) {
		return defaultMailSender.sendMailTo(from, robertMail,
				createSubject(subject) + Messages.getString("MailSender.subjectSuffix"), text, null); //$NON-NLS-1$
	}

	static public boolean sendMailToSupportTeam(String from, String subject,
			String text) {

		System.out.println("Send mail to support team"); //$NON-NLS-1$

		Vector<String> files = new Vector<String>();

		File file;
		int cmp = 0;

		DataPack datapack = Program.myMainFrame.datapack;

		if(datapack.getFilePath() != null) {
			do {
				file = new File(datapack.getFilePath() + ".save" + (cmp++) + ".err"); //$NON-NLS-1$ //$NON-NLS-2$
			} while (file.exists());
		} else {
			file = new File("." + File.separatorChar + "datapackError.err"); //$NON-NLS-1$ //$NON-NLS-2$
			if(file.exists()) {
				file.delete();
			}
		}

		if(Program.saveObject(datapack, file.getPath(), true)) {
			files.add(file.getPath());
		} else {
			System.out.println(Messages.getString("MailSender.8")); //$NON-NLS-1$
		}

		files.add(Logger.getInstance().getFileLogPath());

		boolean result = defaultMailSender.sendMailTo(from, supportMail,
				createSubject(subject), text, files);

		if(file.exists()) {
			file.delete();
		}

		return result;
	}
	
	static public void sendAutoMail(final String from,final String subject,final String text)
	{
		if (!defaultMailSender.sendMailTo(from, supportMail, subject, text, null))
		{
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					sendAutoMail(from, subject, text);
				}
			}, 900000);
		}
		
	}

	static public MailSender getSingleton() {
		return defaultMailSender;
	}
}
