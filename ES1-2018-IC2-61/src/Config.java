import java.io.File;
import java.io.FileOutputStream;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.restfb.types.Post;

import twitter4j.Status;

public class Config {

	private String facebookAccount;
	private String twitterAccount;
	private String emailAccount;

	private boolean loggedFacebook;
	private boolean loggedTwitter;
	private boolean loggedEmail;

	public boolean isLoggedFacebook() {
		return loggedFacebook;
	}


	public boolean isLoggedTwitter() {
		return loggedTwitter;
	}


	public boolean isLoggedEmail() {
		return loggedEmail;
	}


	public Config() {
		read("Facebook");
		read("Twitter");
		read("Email");
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
			System.out.println("-------/////-------");
			// Query
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/Config/" + s + "/@*");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println(nl.item(i).getNodeName() + ": ");
				System.out.println(nl.item(i).getFirstChild().getNodeValue());
				if (nl.item(i).getNodeName().equals("Token")) {
					if (s.equals("Facebook"))
						loggedFacebook=true;
					//						facebookAccount = nl.item(i).getFirstChild().getNodeValue();
					if (s.equals("Twitter"))
						loggedTwitter=true;
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
				element.setAttribute("Date", p.getUpdatedTime().toString());
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
	//	
	//	public void write(String s, String email, String token) {
	//		try {
	//			File inputFile = new File("src/resources/config.xml");
	//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	//			Document doc = dBuilder.parse(inputFile);
	//			doc.getDocumentElement().normalize();
	//			Element element = doc.createElement(s);
	//			element.setAttribute("Conta", email);
	//			element.setAttribute("Token", token);
	//
	//			Node node = doc.getDocumentElement();
	//			node.appendChild(element);
	//
	//			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	//			StreamResult result = new StreamResult(new FileOutputStream(inputFile));
	//			DOMSource source = new DOMSource(doc);
	//			transformer.transform(source, result);
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//
	//	}


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
			System.out.println(element.getNodeName());
			System.out.println(element.getParentNode().getNodeName());

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

	public void setLoggedFacebook(boolean loggedFacebook) {
		this.loggedFacebook = loggedFacebook;
	}


	public void setLoggedTwitter(boolean loggedTwitter) {
		this.loggedTwitter = loggedTwitter;
	}


	public void setLoggedEmail(boolean loggedEmail) {
		this.loggedEmail = loggedEmail;
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
				System.out.println(element.getNodeName());
				System.out.println(element.getParentNode().getNodeName());

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

	public void setFacebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
	}

	public void setTwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	/**
	 * Returns the current Facebook account.
	 * 
	 * @return facebookAccount
	 */
	public String getFacebookAccount() {
		return facebookAccount;
	}

	/**
	 * Returns the current Twitter account.
	 * 
	 * @return twitterAccount
	 */
	public String getTwitterAccount() {
		return twitterAccount;
	}

	/**
	 * Returns the current Email account.
	 * 
	 * @return emailAccount
	 */
	public String getEmailAccount() {
		return emailAccount;
	}

}
