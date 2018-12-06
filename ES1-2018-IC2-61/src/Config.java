import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.mail.Message;
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

public class Config {

	private static Config configurations = null;

	private boolean loggedFacebook;
	private boolean loggedTwitter;
	private boolean loggedEmail;

	public ArrayList<Post> postsList = new ArrayList<Post>();
	public ArrayList<Status> tweetsList = new ArrayList<Status>();

	private String facebookToken;
	private String twitterToken;
	private String twitterTokenSecret;
	private String emailAccount;
	private String emailPassword;


	public boolean isLoggedFacebook() {
		return loggedFacebook;
	}


	public boolean isLoggedTwitter() {
		return loggedTwitter;
	}


	public boolean isLoggedEmail() {
		return loggedEmail;
	}


	private Config() {
		read("Facebook");
		read("Twitter");
		read("Email");
	}

	public static Config getInstance(){

		if(configurations == null)
			configurations = new Config();

		return configurations;
	}

	/**
	 * Reads the account elements on the XML file and saves them to their respective fields.
	 *
	 *@param s String to determine which service to find and save
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
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				if (s.equals("Facebook")) {
					facebookToken = nl.item(i).getFirstChild().getNodeValue();
					loggedFacebook=true;
				}
				//						facebookAccount = nl.item(i).getFirstChild().getNodeValue();
				if (s.equals("Twitter")) {
					if(nl.item(i).getNodeName().equals("Token"))
						twitterToken = nl.item(i).getFirstChild().getNodeValue();
					if(nl.item(i).getNodeName().equals("TokenSecret"))
						twitterTokenSecret = nl.item(i).getFirstChild().getNodeValue();
					loggedTwitter=true;
				}
				//						twitterAccount = nl.item(i).getFirstChild().getNodeValue();
				if (s.equals("Email")) {
					if (nl.item(i).getNodeName().equals("Account"))
						emailAccount = nl.item(i).getFirstChild().getNodeValue();
					if (nl.item(i).getNodeName().equals("Password"))
						emailPassword = nl.item(i).getFirstChild().getNodeValue();
					loggedEmail=true;
				}
				//						twitterAccount = nl.item(i).getFirstChild().getNodeValue();
				if (nl.item(i).getNodeName().equals("Account"))
					loggedEmail=true;
				//						emailAccount = nl.item(i).getFirstChild().getNodeValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadLastSearch(String string) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/Config/"+string);
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				NamedNodeMap attributes = nl.item(i).getAttributes();
				if (string.equals("Post")) {
					Post p = new Post();
					p.setId(attributes.item(1).getNodeValue());
					DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
					TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
					Date date = dateFormat.parse(attributes.item(2).getNodeValue());
					p.setUpdatedTime(date);
					p.setMessage(attributes.item(0).getNodeValue());
					postsList.add(p);
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
			} else if (o instanceof Post){
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
				element.setAttribute("ID", st.getSource());
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

			if(s.equals("Facebook"))
				setLoggedFacebook(false);
			else if(s.equals("Twitter"))
				setLoggedTwitter(false);
			else if(s.equals("Email"))
				setLoggedEmail(false);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearResults(String s) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			for(int i=0; i<doc.getElementsByTagName(s).getLength(); i++) {
				Element element = (Element) doc.getElementsByTagName(s).item(i);

				element.getParentNode().removeChild(element);

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


	public void setLoggedFacebook(boolean loggedFacebook) {
		this.loggedFacebook = loggedFacebook;
		if (!loggedFacebook)
			setFacebookToken(null);
	}

	public void setLoggedTwitter(boolean loggedTwitter) {
		this.loggedTwitter = loggedTwitter;
		if (!loggedTwitter) {
			setTwitterToken(null);
			setTwitterTokenSecret(null);
		}
	}

	public void setLoggedEmail(boolean loggedEmail) {
		this.loggedEmail = loggedEmail;
		if (!loggedEmail) {
			setEmailAccount(null);
			setEmailPassword(null);
		}
	}

	public void setFacebookToken(String facebookToken) {
		this.facebookToken = facebookToken;
	}


	public void setTwitterToken(String twitterToken) {
		this.twitterToken = twitterToken;
	}


	public void setTwitterTokenSecret(String twitterTokenSecret) {
		this.twitterTokenSecret = twitterTokenSecret;
	}


	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}


	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getFacebookToken() {
		return facebookToken;
	}


	public String getTwitterToken() {
		return twitterToken;
	}


	public String getTwitterTokenSecret() {
		return twitterTokenSecret;
	}


	public String getEmailAccount() {
		return emailAccount;
	}


	public String getEmailPassword() {
		return emailPassword;
	}


	public ArrayList<Post> getPostsList() {
		return postsList;
	}

}