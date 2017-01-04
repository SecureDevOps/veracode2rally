package veracode2rallyConfig;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "unique_id", "mitigation_action", "mitigation_comment", "mitigation_history" })
public class Customfield {
	private String mitigation_action;
	private String mitigation_comment;
	private String mitigation_history;
	private String unique_id;

	public Customfield() {
	}

	public Customfield(String mitigation_action, String mitigation_comment, String mitigation_history,
			String unique_id) {
		super();
		this.mitigation_action = mitigation_action;
		this.mitigation_comment = mitigation_comment;
		this.mitigation_history = mitigation_history;
		this.unique_id = unique_id;
	}

	public String getMitigation_action() {
		return mitigation_action;
	}

	public void setMitigation_action(String mitigation_action) {
		this.mitigation_action = mitigation_action;
	}

	public String getMitigation_comment() {
		return mitigation_comment;
	}

	public void setMitigation_comment(String mitigation_comment) {
		this.mitigation_comment = mitigation_comment;
	}

	public String getMitigation_history() {
		return mitigation_history;
	}

	public void setMitigation_history(String mitigation_history) {
		this.mitigation_history = mitigation_history;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

}