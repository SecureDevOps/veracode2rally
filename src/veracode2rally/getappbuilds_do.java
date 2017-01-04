package veracode2rally;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class getappbuilds_do {

	public static void main() throws FileNotFoundException, IOException, XPathExpressionException,
			ParserConfigurationException, SAXException {

		try {

			// Create a date string for the range of time to query for scans of applications that have changed or been published
			// That will be passed to Veracode getAppBuilds to look for the last build that was scanned
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -2);
			String report_changed_since = new SimpleDateFormat("MM/dd/yyy").format(cal.getTime());

			// Set console print stream to download applicationbuilds.xml

			PrintStream ps_console = System.out;
			File file = new File("resources/applicationbuilds.xml");
			FileOutputStream fos = new FileOutputStream(file);

			// Create new print stream for file.
			PrintStream ps = new PrintStream(fos);

			// Set file print stream.
			System.setOut(ps);

			System.out.print(
					credentials.getInstance().getresultswrapper().getAppBuilds(report_changed_since, "True", ""));

			System.setOut(ps_console);

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

}
