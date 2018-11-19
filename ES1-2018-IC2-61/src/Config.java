import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.crypto.dsig.dom.DOMSignContext;
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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Config {

	private String facebookAccount;
	private String twitterAccount;
	private String emailAccount;

	public Config() {
		read("Facebook");
		read("Twitter");
		read("Email");
	}

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
				if (nl.item(i).getNodeName().equals("Conta")) {
					if (s.equals("Facebook"))
						facebookAccount = nl.item(i).getFirstChild().getNodeValue();
					if (s.equals("Twitter"))
						twitterAccount = nl.item(i).getFirstChild().getNodeValue();
					if (s.equals("Email"))
						emailAccount = nl.item(i).getFirstChild().getNodeValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void write(String s, String email, String token) {
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Element element = doc.createElement(s);
			element.setAttribute("Account", email);
			element.setAttribute("Token", token);

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

	public void delete(String s) {
		System.out.println("olaolaoalaoalaoaolaoa");
		try {
			File inputFile = new File("src/resources/config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			Element element = (Element) doc.getElementsByTagName(s).item(0);
			System.out.println(element.getNodeName());
			System.out.println(element.getParentNode().getNodeName());
			;
			element.getParentNode().removeChild(element);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new FileOutputStream(inputFile));
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFacebookAccount() {
		return facebookAccount;
	}

	public String getTwitterAccount() {
		return twitterAccount;
	}

	public String getEmailAccount() {
		return emailAccount;
	}
}
