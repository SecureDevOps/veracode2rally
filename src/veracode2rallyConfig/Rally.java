package veracode2rallyConfig;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "username", "password", "api_key", "login_credential", "encrypt" })
public class Rally {
	private String username;
	private String password;
	private String api_key;
	//private String uri;
	private String login_credential;
	@XmlAttribute
	private Boolean encrypt;


	public Rally() {
	}

	public Rally(String username, String password, String api_key, String login_credential,  Boolean encrypt) {
		super();
		this.username = username;
		this.password = password;
		this.api_key = api_key;
		this.login_credential = login_credential;
	    this.encrypt = encrypt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

//	public String getUri() {
//		return uri;
//	}

	//public void setUri(String uri) {
	//	this.uri = uri;
	//}

	public String getLogin_credential() {
		return login_credential;
	}

	public void setLogin_credential(String login_credential) {
		this.login_credential = login_credential;
	}

		
	public Boolean getEncyrpt() {
        return encrypt;
		
	}

	public void setEncrypt(Boolean encrypt) {
      this.encrypt = encrypt;
		
		
	}

}