package veracode2rally;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.xml.sax.SAXException;

import com.rallydev.rest.RallyRestApi;
import com.veracode.apiwrapper.wrappers.MitigationAPIWrapper;
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper;

public class credentials {
	private static credentials instance = null;

	private static String veracode_username;
	public static String veracode_password;
	private static String veracode_api_id;
	private static String veracode_api_key;
	private static String veracode_login_credential;

	public static Boolean veracode_encrypt;
	public static Boolean rally_encrypt;

	private static String rally_username;
	private static String rally_password;
	private static String rally_api_key;
	private static String rally_login_credential;

	public static String mitigation_action_field = null;
	public static String mitigation_comment_field = null;
	public static String mitigation_history_field = null;
	public static String unique_id_field = null;

	public static void main() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		veracode_username = veracode2rally_config_xml.getInstance()
				.GetXMLValue("/veracode2rally_config/veracode/username/text()");

		veracode_api_id = veracode2rally_config_xml.getInstance()
				.GetXMLValue("/veracode2rally_config/veracode/api_id/text()");

		veracode_login_credential = veracode2rally_config_xml.getInstance()
				.GetXMLValue("/veracode2rally_config/veracode/login_credential/text()");

		veracode_encrypt = veracode2rally_config_xml.getInstance()
				.GetXMLValueBool("boolean(/veracode2rally_config/veracode[@encrypt='true'])");

		if (veracode_encrypt == true) {

			veracode_password = Decrypt(veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/veracode/password/text()"));

			veracode_api_key = Decrypt(veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/veracode/api_key/text()"));

		} else {

			veracode_password = veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/veracode/password/text()");

			veracode_api_key = veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/veracode/api_key/text()");
		}

		rally_username = veracode2rally_config_xml.getInstance()
				.GetXMLValue("/veracode2rally_config/rally/username/text()");

		rally_login_credential = veracode2rally_config_xml.getInstance()
				.GetXMLValue("/veracode2rally_config/rally/login_credential/text()");

		rally_encrypt = veracode2rally_config_xml.getInstance()
				.GetXMLValueBool("boolean(/veracode2rally_config/rally[@encrypt='true'])");

		if (rally_encrypt == true) {

			rally_password = Decrypt(veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/rally/password/text()"));

			rally_api_key = Decrypt(
					veracode2rally_config_xml.getInstance().GetXMLValue("/veracode2rally_config/rally/api_key/text()"));

		} else {

			rally_password = veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/rally/password/text()");

			rally_api_key = veracode2rally_config_xml.getInstance()
					.GetXMLValue("/veracode2rally_config/rally/api_key/text()");
		}

	}

	public static credentials getInstance()
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		if (instance == null) {
			instance = new credentials();
		}
		credentials.main();
		return instance;
	}

	public ResultsAPIWrapper getresultswrapper()
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		ResultsAPIWrapper resultswrapper = new ResultsAPIWrapper();

		switch (veracode_login_credential) {

		case "Username/Password":
			resultswrapper.setUpCredentials(veracode_username, veracode_password);
			break;

		case "API ID/Key":
			resultswrapper.setUpApiCredentials(veracode_api_id, veracode_api_key);
			break;
		}

		return resultswrapper;

	}

	public MitigationAPIWrapper getmitigationwrapper()
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		MitigationAPIWrapper mitigationwrapper = new MitigationAPIWrapper();

		switch (veracode_login_credential) {

		case "Username/Password":
			mitigationwrapper.setUpCredentials(veracode_username, veracode_password);
			break;

		case "API ID/Key":
			mitigationwrapper.setUpApiCredentials(veracode_api_id, veracode_api_key);
			break;
		}

		return mitigationwrapper;

	}

	@SuppressWarnings("deprecation")
	public RallyRestApi getrallyapi() throws XPathExpressionException, ParserConfigurationException, SAXException,
			IOException, URISyntaxException {

		RallyRestApi rallyapi = null;

		switch (rally_login_credential) {

		case "Username/Password":
			rallyapi = new RallyRestApi(new URI("https://rally1.rallydev.com"), rally_username, rally_password);

		case "API Key":
			rallyapi = new RallyRestApi(new URI("https://rally1.rallydev.com"), rally_api_key);
		}

		return rallyapi;
	}

	public String Encrypt(String text) {

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(";?kpL={vfAr=q$[%");
		String strEncryptedText = encryptor.encrypt(text);
		return strEncryptedText;
	}

	public static String Decrypt(String text) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(";?kpL={vfAr=q$[%");
		String strDecryptedText = encryptor.decrypt(text);
		return strDecryptedText;

	}

}
