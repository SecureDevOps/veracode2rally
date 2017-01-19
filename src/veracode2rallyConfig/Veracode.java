package veracode2rallyConfig;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "username", "password", "api_id", "api_key", "login_credential", "encrypt" })
public class Veracode {
	private String username;
	private String password;
	private String api_id;
	private String api_key;
	private String login_credential;
	@XmlAttribute
	private Boolean encrypt;

	public Veracode() {
	}

	public Veracode(String username, String password, String api_id, String api_key, String login_credential,
			Boolean encrypt) {
		super();
		this.username = username;
		this.password = password;
		this.api_id = api_id;
		this.api_key = api_key;
		this.login_credential = login_credential;
		this.encrypt = encrypt;
	}

	@XmlElement
	public String getUsername() {
		return username.trim();
	}

	public void setUsername(String username) {
		this.username = username.trim();
	}

	@XmlElement
	public String getPassword() {
		return password.trim();

	}

	public void setPassword(String password) {
		this.password = password.trim();

	}

	@XmlElement
	public String getApi_id() {
		return api_id.trim();
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id.trim();
	}

	@XmlElement
	public String getApi_key() {
		return api_key.trim();
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key.trim();
	}

	@XmlElement
	public String getLogin_credential() {
		return login_credential.trim();
	}

	public void setLogin_credential(String login_credential) {
		this.login_credential = login_credential.trim();
	}

	// @XmlAttribute
	public Boolean getEncyrpt() {
		return encrypt;

	}

	// @XmlAttribute
	public void setEncrypt(Boolean encrypt) {
		this.encrypt = encrypt;

	}

}