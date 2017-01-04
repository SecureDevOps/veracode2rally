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

public class mitigationinfo_xml {
	private static mitigationinfo_xml instance = null;

	public static Document mitigationinfoDoc;

	public mitigationinfo_xml()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		mitigationinfoDoc = docBuilder.parse(new File("resources/mitigationinfo.xml"));

	}

	public static mitigationinfo_xml getInstance()
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		if (instance == null) {
			instance = new mitigationinfo_xml();
		}

		return instance;

	}

	// GetXMLValue evaluates a XPath expression and returns a single value
	public static String GetXMLValue(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		String strResult = (String) xpath.compile(strXPath).evaluate(mitigationinfoDoc, XPathConstants.STRING);

		return strResult;
	}

	// GetXMLNodeList evaluates a XPath expression and returns a nodelist
	public static NodeList GetXMLNodeList(String strXPath) throws XPathExpressionException {

		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xPath = xpathfactory.newXPath();

		NodeList nodeList = (NodeList) xPath.evaluate(strXPath, mitigationinfoDoc, XPathConstants.NODESET);

		return nodeList;

	}

	// LoadMitigationInfo formats data from mitigationinfo_xml
	// Will be displayed in Rally Mitigation History field
	public static String LoadMitigationInfo(String flaw_id_list)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		String mitigationhistory = "";

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		mitigationinfoDoc = docBuilder.parse(new File("resources/mitigationinfo.xml"));

		NodeList dateNodeList = GetXMLNodeList("//mitigation_action");

		for (int i = 1, isize = dateNodeList.getLength(); i < isize + 1; i++) {

			mitigationhistory = mitigationhistory + "<b>" + "Date: " + "</b>"
					+ GetXMLValue("//mitigation_action[" + i + "]/@date") + "<br/>";

			mitigationhistory = mitigationhistory + "<b>" + "Reviewer: " + "</b>"
					+ GetXMLValue("//mitigation_action[" + i + "]/@reviewer") + "<br/>";

			mitigationhistory = mitigationhistory + "<b>" + "Action: " + "</b>"
					+ GetXMLValue("//mitigation_action[" + i + "]/@desc") + "<br/>";
			mitigationhistory = mitigationhistory + "<b>" + "Comment: " + "</b>"
					+ GetXMLValue("//mitigation_action[" + i + "]/@comment") + "<br/>";
			mitigationhistory = mitigationhistory + "<br/>";

		}

		return mitigationhistory;
	}

}
