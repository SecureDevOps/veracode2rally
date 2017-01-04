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

	public Veracode(String username, String password, String api_id, String api_key, String login_credential, Boolean encrypt) {
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
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@XmlElement
	public String getPassword() {
        return password;
		
	}

	public void setPassword(String password) {
      this.password = password;
		
		
	}

	@XmlElement
	public String getApi_id() {
		return api_id;
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id;
	}

	@XmlElement
	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	@XmlElement
	public String getLogin_credential() {
		return login_credential;
	}

	public void setLogin_credential(String login_credential) {
		this.login_credential = login_credential;
	}

	
	//@XmlAttribute
	public Boolean getEncyrpt() {
        return encrypt;
		
	}

	//@XmlAttribute
	public void setEncrypt(Boolean encrypt) {
      this.encrypt = encrypt;
		
		
	}


}