package veracode2rallyConfig;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (name = "veracode2rally_config")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "mappings", "veracode", "rally", "customfield" })
public class Project {
	private List<Mapping> mappings;
	private List<Rally> rally;
	private List<Veracode> veracode;
	private List<Customfield> customfield;

	public Project() {
	}

	public Project(List<Mapping> mappings, List<Rally> rally, List<Veracode> veracode, List<Customfield> customfield) {
		super();
		this.mappings = mappings;
		this.rally = rally;
		this.veracode = veracode;
		this.customfield = customfield;
	}

	@XmlElementWrapper
	@XmlElement(name = "mapping")
	public List<Mapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	@XmlElement
	public List<Rally> getRally() {
		return rally;
	}

	public void setRally(List<Rally> rally) {
		this.rally = rally;
	}

	@XmlElement
	public List<Veracode> getVeracode() {
		return veracode;
	}

	public void setVeracode(List<Veracode> veracode) {
		this.veracode = veracode;
	}

	@XmlElement
	public List<Customfield> getCustomfield() {
		return customfield;
	}

	public void setCustomfield(List<Customfield> customfield) {
		this.customfield = customfield;
	}

}
