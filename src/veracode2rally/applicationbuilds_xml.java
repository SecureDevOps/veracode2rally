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

public class applicationbuilds_xml {
	private static applicationbuilds_xml instance = null;

	public Document appbuildsDoc;

	protected applicationbuilds_xml()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		appbuildsDoc = docBuilder.parse(new File("resources/applicationbuilds.xml"));

	}

	public static applicationbuilds_xml getInstance()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		if (instance == null) {
			instance = new applicationbuilds_xml();
		}

		return instance;
	}

	// GetXMLValue evaluates a XPath expression and returns a single value
	public String GetXMLValue(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		String strResult = (String) xpath.compile(strXPath).evaluate(appbuildsDoc, XPathConstants.STRING);

		return strResult;

	}

	// GetXMLNodeList evaluates a XPath expression and returns a nodelist
	public NodeList GetXMLNodeList(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xPath = xpathfactory.newXPath();

		NodeList nodeList = (NodeList) xPath.evaluate(strXPath, appbuildsDoc, XPathConstants.NODESET);

		return nodeList;

	}

}
