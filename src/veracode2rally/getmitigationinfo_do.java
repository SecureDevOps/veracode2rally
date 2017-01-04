package veracode2rally;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class getmitigationinfo_do {

	public static void main(String build_id, String flaw_id) throws FileNotFoundException, IOException,
			XPathExpressionException, ParserConfigurationException, SAXException {

		try {

			// Set console print stream so we can download the flaw report to
			// XML
			PrintStream ps_console = System.out;
			File file = new File("resources/mitigationinfo.xml");
			FileOutputStream fos = new FileOutputStream(file);

			// Create new print stream for file.
			PrintStream ps = new PrintStream(fos);

			// Set file print stream.
			System.setOut(ps);

			System.out.print(credentials.getInstance().getmitigationwrapper().getMitigationInfo(build_id, flaw_id));

			System.setOut(ps_console);

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

}
