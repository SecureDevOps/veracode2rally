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

public class veracode2rally_config_xml {
	public static veracode2rally_config_xml instance = null;
	public static String mitigation_action_field = null;
	public static String mitigation_comment_field = null;
	public static String mitigation_history_field = null;
	public static String unique_id_field = null;

	public Document veracode2rally_configDoc;

	protected veracode2rally_config_xml()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		// Remediates CWE-611: Improper Restriction of XML External Entity Reference ('XXE')
		docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		veracode2rally_configDoc = docBuilder.parse(new File("resources/veracode2rally_config.xml"));

	}

	public static veracode2rally_config_xml getInstance()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		if (instance == null) {
			instance = new veracode2rally_config_xml();

			// Contains the names of user defined Rally custom fields entered in veracode2rallyConfig
			mitigation_action_field = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("/veracode2rally_config/customfield/mitigation_action/text()").trim());

			mitigation_comment_field = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("/veracode2rally_config/customfield/mitigation_comment/text()").trim());

			mitigation_history_field = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("/veracode2rally_config/customfield/mitigation_history/text()").trim());

			unique_id_field = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("/veracode2rally_config/customfield/unique_id/text()").trim());

		}

		return instance;
	}

	// GetXMLValue evaluates a XPath expression and returns a single value
	public String GetXMLValue(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		String strResult = (String) xpath.compile(strXPath).evaluate(veracode2rally_configDoc, XPathConstants.STRING);

		return strResult;
	}

	// GetXMLValueBool evaluates a XPath expression and returns a boolean value
	public Boolean GetXMLValueBool(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		Boolean boolResult = (Boolean) xpath.compile(strXPath).evaluate(veracode2rally_configDoc,
				XPathConstants.BOOLEAN);

		return boolResult;
	}

	// GetXMLNodeList evaluates a XPath expression and returns a nodelist
	public NodeList GetXMLNodeList(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xPath = xpathfactory.newXPath();

		NodeList nodeList = (NodeList) xPath.evaluate(strXPath, veracode2rally_configDoc, XPathConstants.NODESET);

		return nodeList;

	}

}
