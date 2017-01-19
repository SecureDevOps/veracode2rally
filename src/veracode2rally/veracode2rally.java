package veracode2rally;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonObject;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.request.UpdateRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.response.UpdateResponse;
import com.rallydev.rest.util.QueryFilter;

public class veracode2rally {
	private static String app_id;
	private static String build_id;
	private static String action;
	private static String comment;
	private static String rally_projectid;
	private static String rally_projectname;
	private static String import_filter;
	private static String veracode2rallyid;
	private static String veracode_issueid;
	private static NodeList rallyfieldNodeList;
	private static String strNodeName;
	private static String strNodeValue;
	private static String TimeStamp;
	private static String filter_query;

	// main class to download a flaw report from Veracode, send to Rally, and create new defect tickets

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException,
			TransformerException, URISyntaxException, XPathExpressionException {

		TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(TimeStamp + "  Beginning Veracode to Rally Synchronization...");

		// Download applicationbuilds.xml file.
		// Access for what app_ids that can be downloaded are tied to the permissions of the Veracode account being used.
		// The corresponding build_ids in applicationbuilds.xml are associated to scans with the latest published results.
		getappbuilds_do.main();

		// Load veracode2rally_config.xml. Get the Veracode App IDs that were entered from the Project Mapping tab in
		// the Configuration application.
		NodeList configNodeList = veracode2rally_config_xml.getInstance().GetXMLNodeList("//mapping/veracodeid/text()");

		// Cycle through all app_ids in veracode_config.xml
		for (int i = 0, isize = configNodeList.getLength(); i < isize; i++) {

			// capture the app_id and the build_id for this cycle using xpath
			app_id = configNodeList.item(i).getNodeValue();
			build_id = applicationbuilds_xml.getInstance().GetXMLValue(
					"//application[@app_id=" + configNodeList.item(i).getNodeValue() + "]/build/@build_id");

			if (build_id.equals("")) {

				System.out.println("Error downloading Veracode detailed report for appid: " + app_id
						+ " - No build_id, check permissions ");

				break;

			} // if (build_id == "")

			System.out.println("Processing: " + veracode2rally_config_xml.getInstance()
					.GetXMLValue("//mapping[" + (i + 1) + "]/veracodename/text()"));
			System.out.println("Veracode AppID: " + app_id);
			System.out.println("Veracode BuildID: " + build_id);

			rally_projectid = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("//mapping[" + (i + 1) + "]/rallyid/text()").trim());

			rally_projectname = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("//mapping[" + (i + 1) + "]/rallyname/text()").trim());

			System.out.println("Rally Project Name: " + rally_projectname);
			System.out.println("Rally Project ID: " + rally_projectid);

			// download the Veracode detailedreport.xml using the current build_id
			// Process Veracode detailreport.xml using XSLT to create veracode2rally.xml file.
			detailedreport_do.main(build_id);

			// Get import filter that is specified in the veracode2rally_config.xml file from the Project Mapping tab in the Configuration application
			import_filter = "";
			import_filter = veracode2rally_config_xml.getInstance()
					.GetXMLValue(("//mapping[" + (i + 1) + "]/importfilter/text()").trim());

			// using the import_filter, determine what the xpath expression is for filter_query
			filter_query = GetFilterQuery(import_filter);

			System.out.println("Import Filter: " + import_filter);

			// run filter_query to return a NodeList of the flaws that will be processed
			NodeList flawNodeList = veracode2rally_xml.getInstance().GetXMLNodeList(filter_query);

			// Cycle through the flaws in the NodeList and process one at a time
			for (int j = 0, jsize = flawNodeList.getLength(); j < jsize; j++) {

				// veracode2rallyid is the unique identifier used in Rally
				veracode2rallyid = (flawNodeList.item(j).getAttributes().getNamedItem("veracode2rallyid")
						.getNodeValue());

				// veracode_issueid is the Veracode issue id in the detailreport
				veracode_issueid = flawNodeList.item(j).getAttributes().getNamedItem("veracode_issueid").getNodeValue();

				// Query Rally for this defect
				QueryResponse queryResponse = QueryRally(veracode2rallyid);

				// if no records were returned. This is a new defect
				// Create a new Rally defect
				if (queryResponse.getTotalResultCount() == 0) {

					// run xpath to return a NodeList of Veracode to Rally mapped fields that need processing.
					rallyfieldNodeList = veracode2rally_xml.getInstance().GetXMLNodeList(
							"//flaw[@veracode_issueid=" + veracode_issueid + "]/rally_fields/rally_field");

					// Process the list of Veracode to Rally mapped fields
					CreateRallyDefect(rallyfieldNodeList);

				} else { // this is an existing defect. Call UpdateRallyDefect

					// run xpath to return a NodeList of Veracode to Rally mapped fields that need processing.
					rallyfieldNodeList = veracode2rally_xml.getInstance().GetXMLNodeList(
							"//flaw[@veracode_issueid=" + veracode_issueid + "]/rally_fields/rally_field");

					// First update the mitigation action and comment fields if the user specified them in the
					// Configuration application. Check to verify these fields are not blank.
					if (!(veracode2rally_config_xml.mitigation_action_field.equals(""))
							&& (!(veracode2rally_config_xml.mitigation_comment_field).equals(""))) {

						// Do not run UpdateRallyMitigationProposed if the flaw has been fixed. Mitigation History in Rally will be lost
						if (!(veracode2rally_xml.getInstance()
								.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@remediation_status")
								.equals("Fixed"))) {

							UpdateRallyMitigationProposed(veracode2rallyid);
						}

					}

					// Update the existing defect in Rally
					UpdateRallyDefect(queryResponse, rallyfieldNodeList);

				} // if (queryResponse.getTotalResultCount() == 0)

				// Update the mitigation history field if the user specified it in the Configuration application
				if (!(veracode2rally_config_xml.mitigation_history_field.equals(""))) {

					// Do not run UpdateRallyMitigationHistory if the flaw has been fixed. Mitigation History in Rally will be lost
					if (!(veracode2rally_xml.getInstance()
							.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@remediation_status")
							.equals("Fixed"))) {

						UpdateRallyMitigationHistory();

					}
				}

			}

			// Reached the end of the app_id cycle, free the veracode2rally_xml object
			// A new veracode2rally_xml object will be created if another app_id needs to be processed
			veracode2rally_xml.getDestructor();

		} // for (int i = 0, isize = configNodeList.getLength(); i < isize; i++) { // Cycle through all app_ids in
			// veracode_config.xml

		TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(TimeStamp + "  Veracode to Rally Synchronization Complete");

	}

	// Pass a veracode2rallyid to QueryRally. This will query rally to test if a rally defect with the same veracode2rallyid already exists
	public static QueryResponse QueryRally(String ID) throws IOException, URISyntaxException, XPathExpressionException,
			ParserConfigurationException, SAXException {

		// Create a Rally query. Request is to see if the Veracode flaw is already in Rally
		QueryRequest queryRequest = new QueryRequest("defects");

		// Set Rally filter. If user specified a unique identifier field in the Configuration application, use that. Otherwise
		// search for veracode2rallyid contained in the Rally Description field
		if (veracode2rally_config_xml.unique_id_field.equals("")) {
			queryRequest.setQueryFilter(new QueryFilter("description", " contains ", "veracode2rally ID: " + ID));
		} else {

			queryRequest.setQueryFilter(new QueryFilter(veracode2rally_config_xml.unique_id_field, " = ", ID));
		}

		// set Rally Project ID to speed query and make sure this is the right project
		queryRequest.setProject("/project/" + rally_projectid);
		queryRequest.setScopedDown(true);

		// run the Rally query
		QueryResponse queryResponse = credentials.getInstance().getrallyapi().query(queryRequest);

		return queryResponse;

	}

	// Pass a rallyfieldNodeList to CreateRallyDefect. RallyfieldNodeList contains the field mapping between Veracode and Rally.
	// Using rallyfieldNodeList, create a new Rally Defect.
	public static void CreateRallyDefect(NodeList rallyfieldNodeList) throws IOException, XPathExpressionException,
			DOMException, ParserConfigurationException, SAXException, URISyntaxException {

		TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(TimeStamp + "  Creating new Rally defect...");

		JsonObject newDefect = new JsonObject();

		// add the Rally project number as a property for the new defect
		newDefect.addProperty("Project", "/project/" + rally_projectid);

		// cycle through the rally_field nodes to be processed
		for (int i = 0, isize = rallyfieldNodeList.getLength(); i < isize; i++) {

			// cycle through attributes of the rally_node
			// insert name/value pairs into JSON object for Rally
			// Rally field name and value is expressed as an attribute of the node
			for (int j = 0, jsize = rallyfieldNodeList.item(i).getAttributes().getLength(); j < jsize; j++) {

				strNodeName = rallyfieldNodeList.item(i).getAttributes().item(j).getNodeName();
				strNodeValue = rallyfieldNodeList.item(i).getAttributes().item(j).getNodeValue();

				newDefect.addProperty(strNodeName, strNodeValue);

			} // finished looping through attributes

		} // finished looping through Rally fields that need to be populated

		// if the user specified a Rally custom Unique ID field in the Configuration application, insert veracode2rallyid into that field
		if (!(veracode2rally_config_xml.unique_id_field.equals(""))) {
			newDefect.addProperty(veracode2rally_config_xml.unique_id_field, veracode2rallyid);
		}

		// if this defect has a remediation_status that is "Fixed" or has a mitigation_status that is "accepted", change state of Rally defect ticket to Closed
		if ((veracode2rally_xml.getInstance()
				.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@remediation_status").equals("Fixed"))
				|| (veracode2rally_xml.getInstance()
						.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@mitigation_status")
						.equals("accepted"))) {
			newDefect.addProperty("state", "Closed");
		}

		// if this defect has a remediation_status that is "Re-Open" or has a mitigation_status that is "rejected", change state of Rally defect ticket to Open
		if ((veracode2rally_xml.getInstance()
				.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@remediation_status")
				.equals("Re-Open"))
				|| (veracode2rally_xml.getInstance()
						.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@mitigation_status")
						.equals("rejected"))) {
			newDefect.addProperty("state", "Open");
		}

		// create the defect request and response in Rally
		CreateRequest createRequest = new CreateRequest("defect", newDefect);
		CreateResponse createResponse = credentials.getInstance().getrallyapi().create(createRequest);

		// Now that the defect has been created in Rally, write back a comment record to Veracode.
		// This will let users in Veracode know that a Rally defect was created and the Rally ID of that defect
		credentials.getInstance().getmitigationwrapper().updateMitigationInfo(build_id, "comment",
				"[Added by Veracode2Rally app] Rally Defect "
						+ createResponse.getObject().get("FormattedID").getAsString()
						+ " has been opened for this flaw.",
				veracode_issueid);

		TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(TimeStamp + " Rally ticket created with Veracode2Rally ID: " + veracode2rallyid
				+ " and Rally ID: " + createResponse.getObject().get("FormattedID").getAsString());

	}

	// Pass the queryResponse where a Rally defect exists and rallyfieldNodeList to UpdateRallyDefect.
	// The Rally defect will be updated with same field mapping as in CreateRallyDefect.
	public static void UpdateRallyDefect(QueryResponse queryResponse, NodeList rallyfieldNodeList) throws IOException,
			XPathExpressionException, DOMException, ParserConfigurationException, SAXException, URISyntaxException {

		JsonObject updatedDefect = new JsonObject();

		// cycle through the rally_field nodes to be processed
		for (int i = 0, isize = rallyfieldNodeList.getLength(); i < isize; i++) {

			// cycle through attributes of the rally_node
			// insert name/value pairs into JSON object for Rally
			// Rally field name and value is expressed as an attribute of the node
			for (int j = 0, jsize = rallyfieldNodeList.item(i).getAttributes().getLength(); j < jsize; j++) {

				strNodeName = rallyfieldNodeList.item(i).getAttributes().item(j).getNodeName();
				strNodeValue = rallyfieldNodeList.item(i).getAttributes().item(j).getNodeValue();

				updatedDefect.addProperty(strNodeName, strNodeValue);

			} // finished looping through attributes

		} // finished looping through Rally fields that need to be populated

		// if user has specified to use a unique_id_field in Rally, populate that field
		if (!(veracode2rally_config_xml.unique_id_field.equals(""))) {
			updatedDefect.addProperty(veracode2rally_config_xml.unique_id_field, veracode2rallyid);
		}

		// if this defect has a remediation_status that is "Fixed" or has a mitigation_status that is "accepted", change state of Rally defect ticket to Closed
		if ((veracode2rally_xml.getInstance()
				.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@remediation_status").equals("Fixed"))
				|| (veracode2rally_xml.getInstance()
						.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@mitigation_status")
						.equals("accepted"))) {
			updatedDefect.addProperty("state", "Closed");
		}

		// if this defect has a remediation_status that is "Re-Open" or has a mitigation_status that is "rejected", change state of Rally defect ticket to Open
		if ((veracode2rally_xml.getInstance()
				.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@remediation_status")
				.equals("Re-Open"))
				|| (veracode2rally_xml.getInstance()
						.GetXMLValue("//flaw[@veracode_issueid=" + veracode_issueid + "]/@mitigation_status")
						.equals("rejected"))) {
			updatedDefect.addProperty("state", "Open");
		}

		// update the Rally defect
		UpdateRequest updateRequest = new UpdateRequest(
				queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject().get("_ref").getAsString(),
				updatedDefect);
		UpdateResponse updateResponse = credentials.getInstance().getrallyapi().update(updateRequest);
		updatedDefect = updateResponse.getObject();

		TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(TimeStamp + "  Rally defect updated " + queryResponse.getResults().getAsJsonArray().get(0)
				.getAsJsonObject().get("FormattedID").getAsString());
	}

	// Pass a veracode2rallyid to UpdateRallyMitigationProposed to add a Veracode mitigation record.
	// This is only invoked if the user specifies Rally custom fields for mitiagtion_action and mitigation_comment.
	// in the Configuration file
	public static void UpdateRallyMitigationProposed(String ID) throws FileNotFoundException, XPathExpressionException,
			IOException, ParserConfigurationException, SAXException, URISyntaxException {

		// Create a Rally query using the ID that has been passed
		QueryResponse queryResponse = QueryRally(ID);

		// check to see if the mitigation_action_field is null
		// if not, populate action variable
		if (queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject()
				.get(veracode2rally_config_xml.mitigation_action_field).isJsonNull()) {
			action = "";
		} else {
			action = GetMitigationAction(queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject()
					.get(veracode2rally_config_xml.mitigation_action_field).getAsString());
		}

		// check to see if the mitigation_comment_field is null
		// if not, populate comment variable
		if (queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject()
				.get(veracode2rally_config_xml.mitigation_comment_field).isJsonNull()) {
			comment = "";
		} else {
			comment = queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject()
					.get(veracode2rally_config_xml.mitigation_comment_field).getAsString();
		}

		// if neither action or comment is blank, append a mitigation record in Veracode
		if (!action.equals("") && !comment.equals("")) {

			credentials.getInstance().getmitigationwrapper().updateMitigationInfo(build_id, action, comment,
					veracode_issueid);
			getmitigationinfo_do.main(build_id, veracode_issueid);

			// After updating Veracode mitigation history, clear mitigtion_action and mitigation comment fields in Rally

			// create updatedDefect
			JsonObject updatedDefect = new JsonObject();

			// clear Rally fields
			updatedDefect.addProperty(veracode2rally_config_xml.mitigation_action_field, "");
			updatedDefect.addProperty(veracode2rally_config_xml.mitigation_comment_field, "");

			// update Rally
			UpdateRequest updateRequest = new UpdateRequest(
					queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject().get("_ref").getAsString(),
					updatedDefect);
			UpdateResponse updateResponse = credentials.getInstance().getrallyapi().update(updateRequest);
			updatedDefect = updateResponse.getObject();
		}

	}

	// UpdateRallyMitigationHistory writes info from Veracode mitigation record to mitigation_history_field.
	// This is a custom field in Rally that can be configured using the Rally Custom Fields tab in Veracode2RallyConfig
	public static void UpdateRallyMitigationHistory() throws FileNotFoundException, XPathExpressionException,
			IOException, ParserConfigurationException, SAXException, URISyntaxException {

		// download getMitigationInfo from Veracode
		getmitigationinfo_do.main(build_id, veracode_issueid);

		// query Rally for the record matching current veracode2rallyid
		QueryResponse queryResponse = QueryRally(veracode2rallyid);

		// create mitigation_history JSON object
		JsonObject updatedDefect = new JsonObject();
		updatedDefect.addProperty(veracode2rally_config_xml.mitigation_history_field,
				mitigationinfo_xml.LoadMitigationInfo(veracode_issueid));

		// update Rally
		UpdateRequest updateRequest = new UpdateRequest(
				queryResponse.getResults().getAsJsonArray().get(0).getAsJsonObject().get("_ref").getAsString(),
				updatedDefect);

		UpdateResponse updateResponse = credentials.getInstance().getrallyapi().update(updateRequest);
		updatedDefect = updateResponse.getObject();

	}

	// Uses dropdown text in Veracode2RallyConfig and translates to xpath
	public static String GetFilterQuery(String import_filter) {

		String xpath_expression = "";

		switch (import_filter) {

		case "Unmitigated flaws affecting policy":
			xpath_expression = "//flaw[(@affects_policy_compliance = 'true') and (@mitigation_status = 'none')]";
			break;

		case "Flaws affecting policy":
			xpath_expression = "//flaw[@affects_policy_compliance = 'true']";
			break;

		case "All unmitigated flaws":
			xpath_expression = "//flaw[@mitigation_status = 'none']";
			break;

		case "All flaws":
			xpath_expression = "//flaw";
			break;

		}

		return xpath_expression;
	}

	// Uses text from choices in Rally mitigation_action field and translates to Veracode API language
	public static String GetMitigationAction(String action_param) {

		String strResult = "";

		switch (action_param) {

		case "Add Comment":
			strResult = "comment";
			break;

		case "Mitigate by Design":
			strResult = "appdesign";
			break;

		case "Mitigate by Network Environment":
			strResult = "netenv";
			break;

		case "Mitigate by OS Environment":
			strResult = "osenv";
			break;

		case "Potential False Positive":
			strResult = "fp";
			break;

		}

		return strResult;
	}

}