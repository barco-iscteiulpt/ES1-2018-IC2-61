package BDA;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.restfb.types.Post;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Config {

	private static Config configurations = null;

	private boolean loggedFacebook;
	private boolean loggedTwitter;
	private boolean loggedEmail;

	private ArrayList<Post> postsList = new ArrayList<Post>();
	private ArrayList<Status> tweetsList = new ArrayList<Status>();
	private ArrayList<Message> emailsList = new ArrayList<Message>();

	private String facebookToken;
	private String twitterToken;
	private String twitterTokenSecret;
	private String emailAccount;
	private String emailPassword;

	/**
	 * Returns the current state of Email login
	 * 
	 * @return loggedEmail
	 */
	public boolean isLoggedFacebook() {
		return loggedFacebook;
	}
	
	/**
	 * Returns the current state of Twitter login
	 * 
	 * @return loggedTwitter
	 */
	public boolean isLoggedTwitter() {
		return loggedTwitter;
	}

	/**
	 * Returns the current state of Email login
	 * 
	 * @return loggedEmail
	 */
	public boolean isLoggedEmail() {
		return loggedEmail;
	}

	private Config() {
		read("Facebook");
		read("Twitter");
		read("Email");
	}


	/**
	 * Returns the single instance of configurations
	 * 
	 * @return configurations
	 */
	public static Config getInstance() {

		if (configurations == null)
			configurations = new Config();

		return configurations;
	}

	/**
	 * Reads the account elements on the XML file and saves them to their respective
	 * fields.
	 *
	 * @param s String to determine which service to find and save
	 */
	public void read(String s) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			// Query
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/Config/" + s + "/@*");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (s.equals("Facebook")) {
					facebookToken = nodeList.item(i).getFirstChild().getNodeValue();
					loggedFacebook = true;
				}
				// facebookAccount = nl.item(i).getFirstChild().getNodeValue();
				if (s.equals("Twitter")) {
					if (nodeList.item(i).getNodeName().equals("Token"))
						twitterToken = nodeList.item(i).getFirstChild().getNodeValue();
					if (nodeList.item(i).getNodeName().equals("TokenSecret"))
						twitterTokenSecret = nodeList.item(i).getFirstChild().getNodeValue();
					loggedTwitter = true;
				}
				// twitterAccount = nl.item(i).getFirstChild().getNodeValue();
				if (s.equals("Email")) {
					if (nodeList.item(i).getNodeName().equals("Account"))
						emailAccount = nodeList.item(i).getFirstChild().getNodeValue();
					if (nodeList.item(i).getNodeName().equals("Password"))
						emailPassword = nodeList.item(i).getFirstChild().getNodeValue();
					loggedEmail = true;
				}
				// twitterAccount = nl.item(i).getFirstChild().getNodeValue();
				if (nodeList.item(i).getNodeName().equals("Account"))
					loggedEmail = true;
				// emailAccount = nl.item(i).getFirstChild().getNodeValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the XML file and creates objects for each respective element. Adds these objects to lists that are displayed in GUI
	 *
	 * @param string
	 * 		the type of element to load from XML
	 */
	public void loadLastSearch(String string) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/Config/" + string);
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap attributes = nodeList.item(i).getAttributes();
				if (string.equals("Post")) {
					Post p = new Post();
					p.setId(attributes.item(1).getNodeValue());
					DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
					TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
					Date date = dateFormat.parse(attributes.item(2).getNodeValue());
					p.setUpdatedTime(date);
					p.setMessage(attributes.item(0).getNodeValue());
					postsList.add(p);
				}
				if (string.equals("Status")) {
					try {
						TwitterFactory factory = new TwitterFactory();
						Twitter twitter = factory.getInstance();
						twitter.setOAuthConsumer("0I3XKOkznUjpuwdDOSkLvcSpg",
								"zy8i9meaxzK5Rn05rKIsJwWvclJPfdpmtfnE5UuJvHxsW6oZ0G");
						twitter.setOAuthAccessToken(new AccessToken(getTwitterToken(), getTwitterTokenSecret()));
						Status s = twitter.showStatus(Long.parseLong(attributes.item(2).getNodeValue()));
						tweetsList.add(s);
					} catch (TwitterException e) {
					}
					// System.out.println("conteudo do tweet: "+s.getText());

				}
				if (string.equals("Message")) {
					Session session = Session.getDefaultInstance(System.getProperties());
					Message m = new MimeMessage(session);
					m.setSubject(attributes.item(0).getNodeValue());
					DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
					TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
					Date date = dateFormat.parse(attributes.item(1).getNodeValue());
					m.setSentDate(date);
					InternetAddress from = new InternetAddress(attributes.item(2).getNodeValue());
					m.setFrom(from);
					emailsList.add(m);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Writes all the elements on the XML file according to their type.
	 * 
	 * @param o Object to determine the service used
	 */
	public void write(Object o) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Element element = null;
			if (o instanceof TwitterLoginRequest) {
				TwitterLoginRequest lr = (TwitterLoginRequest) o;
				element = doc.createElement(lr.getService());
				element.setAttribute("Token", lr.getToken());
				element.setAttribute("TokenSecret", lr.getTokenSecret());
			} else if (o instanceof FacebookLoginRequest) {
				FacebookLoginRequest lr = (FacebookLoginRequest) o;
				element = doc.createElement(lr.getService());
				element.setAttribute("Token", lr.getToken());
			} else if (o instanceof EmailLoginRequest) {
				EmailLoginRequest lr = (EmailLoginRequest) o;
				element = doc.createElement(lr.getService());
				element.setAttribute("Account", lr.getAccount());
				element.setAttribute("Password", lr.getPassword());
			} else if (o instanceof Post) {
				Post p = (Post) o;
				element = doc.createElement("Post");
				element.setAttribute("UpdatedTime", p.getUpdatedTime().toString());
				element.setAttribute("Content", p.getMessage());
				element.setAttribute("ID", p.getId());
			} else if (o instanceof Status) {
				Status st = (Status) o;
				element = doc.createElement("Status");
				element.setAttribute("Date", st.getCreatedAt().toString());
				element.setAttribute("Content", st.getText());
				String string = Long.toString(st.getId());
				element.setAttribute("ID", string);
			} else if (o instanceof Message) {
				Message msg = (Message) o;
				element = doc.createElement("Message");
				element.setAttribute("Date", msg.getSentDate().toString());
				element.setAttribute("Content", msg.getSubject());
				element.setAttribute("GetFrom", InternetAddress.toString(msg.getFrom()));
			}

			Node node = doc.getDocumentElement();
			node.appendChild(element);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new FileOutputStream(inputFile));
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Deletes one account element according to the parameter type it receives.
	 * Useful when logging out of an account.
	 * 
	 * @param s String to determine which element to delete
	 */
	public void delete(String s) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			Element element = (Element) doc.getElementsByTagName(s).item(0);

			element.getParentNode().removeChild(element);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new FileOutputStream(inputFile));
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			if (s.equals("Facebook"))
				setLoggedFacebook(false);
			else if (s.equals("Twitter"))
				setLoggedTwitter(false);
			else if (s.equals("Email"))
				setLoggedEmail(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Removes all elements of the specified type in its parameter. Does not work with login requests. 
	 * @param s
	 * 		the type of elements to be cleared
	 */
	public void clearResults(String s) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nl = doc.getElementsByTagName(s);
			int aux = nl.getLength();

			for (int i = 0; i < aux; i++) {
				Element element = (Element) nl.item(0);

				Node parent = element.getParentNode();
				parent.removeChild(element);

				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				StreamResult result = new StreamResult(new FileOutputStream(inputFile));
				DOMSource source = new DOMSource(doc);
				transformer.transform(source, result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the login state to its parameter and nullifies facebook data.
	 * 
	 * @param loggedFacebook
	 *            the wanted value of login state
	 */
	public void setLoggedFacebook(boolean loggedFacebook) {
		this.loggedFacebook = loggedFacebook;
		if (!loggedFacebook)
			setFacebookToken(null);
	}

	/**
	 * Sets the login state to its parameter and nullifies twitter data.
	 * 
	 * @param loggedTwitter
	 *            the wanted value of login state
	 */
	public void setLoggedTwitter(boolean loggedTwitter) {
		this.loggedTwitter = loggedTwitter;
		if (!loggedTwitter) {
			setTwitterToken(null);
			setTwitterTokenSecret(null);
		}
	}

	/**
	 * Sets the login state to its parameter and nullifies email data.
	 * 
	 * @param loggedEmail
	 *            the wanted value of login state
	 */
	public void setLoggedEmail(boolean loggedEmail) {
		this.loggedEmail = loggedEmail;
		if (!loggedEmail) {
			setEmailAccount(null);
			setEmailPassword(null);
		}
	}

	/**
	 * Sets the specified String as Facebook token.
	 * 
	 * @param facebookToken
	 *            the current facebook token
	 */
	public void setFacebookToken(String facebookToken) {
		this.facebookToken = facebookToken;
	}

	/**
	 * Sets the specified String as Twitter token.
	 * 
	 * @param twitterToken
	 *            the current twitter token
	 */
	public void setTwitterToken(String twitterToken) {
		this.twitterToken = twitterToken;
	}

	/**
	 * Sets the specified String as Twitter token secret.
	 * 
	 * @param twitterTokenSecret
	 *            the current twitter token secret 
	 */
	public void setTwitterTokenSecret(String twitterTokenSecret) {
		this.twitterTokenSecret = twitterTokenSecret;
	}

	/**
	 * Sets the specified string as email account.
	 * 
	 * @param emailAccount
	 *            the current email Account
	 */
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	/**
	 * Sets the specified string as email password.
	 * 
	 * @param emailPassword
	 *            the current email password
	 */
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}


	/**
	 * Returns the current Facebook Token.
	 * 
	 * @return facebookToken
	 */
	public String getFacebookToken() {
		return facebookToken;
	}

	/**
	 * Returns the current Twitter Token.
	 * 
	 * @return twitterToken
	 */
	public String getTwitterToken() {
		return twitterToken;
	}

	/**
	 * Returns the current Twitter Token Secret.
	 * 
	 * @return twitterTokenSecret
	 */
	public String getTwitterTokenSecret() {
		return twitterTokenSecret;
	}

	/**
	 * Returns the current Email account.
	 * 
	 * @return emailAccount
	 */
	public String getEmailAccount() {
		return emailAccount;
	}


	/**
	 * Returns the current Email password.
	 * 
	 * @return emailPassword
	 */
	public String getEmailPassword() {
		return emailPassword;
	}

	/**
	 * Returns the current Post List
	 * 
	 * @return postsList
	 */
	public ArrayList<Post> getPostsList() {
		return postsList;
	}

	/**
	 * Returns the current Tweets List
	 * 
	 * @return tweetList
	 */
	public ArrayList<Status> getTweetsList() {
		return tweetsList;
	}

	/**
	 * Returns the current Emails List
	 * 
	 * @return emailsList
	 */
	public ArrayList<Message> getEmailsList() {
		return emailsList;
	}

	/**
	 * Sets the specified user name.
	 * 
	 * @param facebookUsername
	 *            the user name 
	 */
	public void setFacebookUsername(String facebookUsername) {
	}

}