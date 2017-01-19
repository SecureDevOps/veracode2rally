package veracode2rallyConfig;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "veracodename", "veracodeid", "rallyname", "rallyid", "importfilter" })
public class Mapping {

	private String veracodename;
	private String veracodeid;
	private String rallyname;
	private String rallyid;
	private String importfilter;

	public Mapping() {
		this.veracodename = "";
		this.veracodeid = "";
		this.rallyname = "";
		this.rallyid = "";
		this.importfilter = "";
	}

	public Mapping(String veracodename, String veracodeid, String rallyname, String rallyid, String importfilter) {
		this.veracodename = veracodename;
		this.veracodeid = veracodeid;
		this.rallyname = rallyname;
		this.rallyid = rallyid;
		this.importfilter = importfilter;
	}

	public String getVeracodename() {
		return veracodename.trim();
	}

	public void setVeracodename(String veracodename) {
		this.veracodename = veracodename.trim();
	}

	public String getVeracodeid() {
		return veracodeid.trim();
	}

	public void setVeracodeid(String veracodeid) {
		this.veracodeid = veracodeid.trim();
	}

	public String getRallyname() {
		return rallyname.trim();
	}

	public void setRallyname(String rallyname) {
		this.rallyname = rallyname.trim();
	}

	public String getRallyid() {
		return rallyid.trim();
	}

	public void setRallyid(String rallyid) {
		this.rallyid = rallyid.trim();
	}

	public String getImportfilter() {
		return importfilter.trim();
	}

	public void setImportfilter(String importfilter) {
		this.importfilter = importfilter.trim();
	}

}
