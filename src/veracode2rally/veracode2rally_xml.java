package veracode2rally;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class veracode2rally_xml {
	private static veracode2rally_xml instance = null;

	public Document veracode2rallyDoc;

	protected veracode2rally_xml()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		// Remediates CWE-611: Improper Restriction of XML External Entity Reference ('XXE')
		docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		veracode2rallyDoc = docBuilder.parse(new File("resources/veracode2rally.xml"));

	}

	public static veracode2rally_xml getInstance()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		if (instance == null) {
			instance = new veracode2rally_xml();
		}

		return instance;
	}

	public static veracode2rally_xml getDestructor()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		if (instance != null) {
			instance = null;
		}

		return instance;
	}

	// GetXMLValue evaluates a XPath expression and returns a single value
	public String GetXMLValue(String strXPath) throws XPathExpressionException {
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		String strResult = (String) xpath.compile(strXPath).evaluate(veracode2rallyDoc, XPathConstants.STRING);

		return strResult;
	}

	// GetXMLNodeList evaluates a XPath expression and returns a nodelist
	public NodeList GetXMLNodeList(String strXPath) throws XPathExpressionException {
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xPath = xpathfactory.newXPath();

		NodeList nodeList = (NodeList) xPath.evaluate(strXPath, veracode2rallyDoc, XPathConstants.NODESET);

		return nodeList;

	}

}
