
import java.util.*;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import twitter4j.Status;


public class EmailHandler {

	private String userName;
	private String password;
	private String sendingHost;
	private int sendingPort;
	private String from;
	private String to;
	private String subject;
	private String text;
	private String receivingHost;
	private int receivingPort;
	
	public ArrayList<Message> finalEmailsList;

	public void login(String userName,String password){

		this.userName=userName; //sender's email can also use as User Name
		this.password=password;

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
		Message simpleMessage = new MimeMessage(session1);
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
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			simpleMessage.setSubject(this.subject);
			simpleMessage.setText(this.text);
			Transport transport = session1.getTransport("smtps");
			transport.connect (this.sendingHost,sendingPort, this.userName, this.password);
			transport.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
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
		this.receivingHost="imap.gmail.com";//for imap protocol
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
						finalEmailsList.add(message[i]);
					}
					if (period.equals("Last hour")) {
						calendar.add(Calendar.HOUR_OF_DAY, -1);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							finalEmailsList.add(message[i]);
						}
					}

					if (period.equals("Last day")) {
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							finalEmailsList.add(message[i]);
						}
					}

					if (period.equals("Last week")) {
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							finalEmailsList.add(message[i]);
						}
					}

					if (period.equals("Last month")) {
						calendar.add(Calendar.MONTH, -1);
						if (message[i].getReceivedDate().after(calendar.getTime())) {
							finalEmailsList.add(message[i]);
						}
					}
				}
				finalEmailsList.add(message[i]);
				System.out.println(message[i].getSubject());
				System.out.println(message[i].getContent());
				//anything else you want
			}
			folder.close(true);
			store.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public ArrayList<Message> getFinalTweetsList() {
		return this.finalEmailsList;
	}

//	public static void main(String[] args) {
//		//Sender must be a Gmail account
//		String mailFrom=new String("projetoes61@gmail.com");
//		String mailTo=new String("barco@iscte-iul.pt");
//		EmailHandler newGmailClient=new EmailHandler();
//		//Setting up account details
//		newGmailClient.login("projetoes61@gmail.com", "rumoao20");
//		String mailSubject=new String("Testing Mail");
//		String mailText=new String("Have an Nice Day ...........!!!");
//		//Send mail
////		newGmailClient.sendGmail(mailFrom, mailTo, mailSubject, mailText);
//		//Receive mails
//		newGmailClient.searchGmail();
//
//	}

}