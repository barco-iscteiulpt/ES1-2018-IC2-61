
import java.io.IOException;
import java.util.*;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;


public class EmailHandler {


	private String sendingHost;
	private int sendingPort;
	private String from;
	private String to;
	private String subject;
	private String text;
	public String currentBody;
	private Config configs = Config.getInstance();
	private String userName = configs.getEmailAccount();
	private String password = configs.getEmailPassword();

//	public boolean loggedIn;

	public ArrayList<Message> finalEmailsList;

	public void login(String userName,String password){
		this.userName=userName; //sender's email can also use as User Name
		this.password=password;
		configs.setLoggedEmail(true);
	}

	public void sendEmail(String from, String to, String subject, String text){
		// This will send mail from -->sender@gmail.com to -->receiver@gmail.com
		this.from=from;
		this.to=to;
		this.subject=subject;
		this.text=text;
		// For a Gmail account--sending mails-- host and port should be as follows
		this.sendingHost="smtp.gmail.com";
		this.sendingPort=465;

		Properties propertiesSender = new Properties();
		propertiesSender.put("mail.smtp.host", this.sendingHost);
		propertiesSender.put("mail.smtp.port", String.valueOf(this.sendingPort));
		propertiesSender.put("mail.smtp.user", this.userName);
		propertiesSender.put("mail.smtp.password", this.password);
		propertiesSender.put("mail.smtp.auth", "true");

		Session session1 = Session.getDefaultInstance(propertiesSender);
		Message replyMessage = new MimeMessage(session1);
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		try {
			fromAddress = new InternetAddress(this.from);
			toAddress = new InternetAddress(this.to);
		} catch (AddressException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sending email to: " + to + " failed !!!", "Falied to Send!!!", JOptionPane.ERROR_MESSAGE);
		}
		try {
			replyMessage.setFrom(fromAddress);
			replyMessage.setRecipient(RecipientType.TO, toAddress);
			replyMessage.setSubject(this.subject);
			replyMessage.setText(this.text);
			Transport transport = session1.getTransport("smtps");
			transport.connect (this.sendingHost,sendingPort, this.userName, this.password);
			transport.sendMessage(replyMessage, replyMessage.getAllRecipients());
			transport.close();
			JOptionPane.showMessageDialog(null, "Mail sent successfully ...","Mail sent",JOptionPane.PLAIN_MESSAGE);
		} catch (MessagingException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sending email to: " + to + " failed !!!", "Falied to Send!!!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void searchGmail(String info, String period){
		finalEmailsList = new ArrayList<>();
		/*this will print subject of all messages in the inbox of sender@gmail.com*/
		Properties propertiesReceiver = System.getProperties();
		propertiesReceiver.setProperty("mail.store.protocol", "imaps");
		Session session2 = Session.getDefaultInstance(propertiesReceiver, null);
		try {
			Store store=session2.getStore("imaps");
			store.connect("imap.gmail.com",this.userName, this.password);
			Folder folder=store.getFolder("INBOX");//get inbox

			folder.open(Folder.READ_ONLY);//open folder only to read

			Message message[]=folder.getMessages();

			Calendar calendar = Calendar.getInstance();

			for(int i=0;i<message.length;i++){
				if(message[i].getSubject().contains(info)) {
					if(period.equals("Anytime")) {
						getBody(message[i]);
						Message m = new MimeMessage(session2);
						m.setSubject(message[i].getSubject()+"\n"+this.currentBody);
						m.setSentDate(message[i].getReceivedDate());
						InternetAddress fromAddress = new InternetAddress(InternetAddress.toString(message[i].getFrom()));;
						m.setFrom(fromAddress);
						finalEmailsList.add(m);
					}
					if (period.equals("Last hour")) {
						calendar.add(Calendar.HOUR_OF_DAY, -1);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							getBody(message[i]);
							Message m = new MimeMessage(session2);
							m.setSubject(message[i].getSubject()+"\n"+this.currentBody);
							m.setSentDate(message[i].getReceivedDate());
							InternetAddress fromAddress = new InternetAddress(InternetAddress.toString(message[i].getFrom()));;
							m.setFrom(fromAddress);
							finalEmailsList.add(m);
						}
					}

					if (period.equals("Last day")) {
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							getBody(message[i]);
							Message m = new MimeMessage(session2);
							m.setSubject(message[i].getSubject()+"\n"+this.currentBody);
							m.setSentDate(message[i].getReceivedDate());
							InternetAddress fromAddress = new InternetAddress(InternetAddress.toString(message[i].getFrom()));;
							m.setFrom(fromAddress);
							finalEmailsList.add(m);
						}
					}

					if (period.equals("Last week")) {
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							getBody(message[i]);
							Message m = new MimeMessage(session2);
							m.setSubject(message[i].getSubject()+"\n"+this.currentBody);
							m.setSentDate(message[i].getReceivedDate());
							InternetAddress fromAddress = new InternetAddress(InternetAddress.toString(message[i].getFrom()));;
							m.setFrom(fromAddress);
							finalEmailsList.add(m);
						}
					}

					if (period.equals("Last month")) {
						calendar.add(Calendar.MONTH, -1);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							getBody(message[i]);
							Message m = new MimeMessage(session2);
							m.setSubject(message[i].getSubject()+"\n"+this.currentBody);
							m.setSentDate(message[i].getReceivedDate());
							InternetAddress fromAddress = new InternetAddress(InternetAddress.toString(message[i].getFrom()));;
							m.setFrom(fromAddress);
							finalEmailsList.add(m);
						}
					}
				}
			}
			folder.close(true);
			store.close();
		} catch (Exception e) {
			configs.setLoggedEmail(false);
		}
	}

	public void getBody(Message email) {
		try {
			Object content = email.getContent();
			if (content instanceof Multipart) {
				StringBuffer messageContent = new StringBuffer();
				Multipart multipart = (Multipart) content;
				for (int i = 0; i < multipart.getCount(); i++) {
					Part part = multipart.getBodyPart(i);
					if (part.isMimeType("text/plain")) {
						messageContent.append(part.getContent().toString());
					}
				}
				this.currentBody = messageContent.toString();
				return;
			}
			this.currentBody = content.toString();
			return;

		} catch (IOException e) {
			System.out.println("Unable to get email body content.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentBody() {
		return this.currentBody;
	}

	public ArrayList<Message> getFinalEmailsList() {
		return this.finalEmailsList;
	}

	public String getUsername() {
		return this.userName;
	}

	public void setUsername(String email) {
		this.userName = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}