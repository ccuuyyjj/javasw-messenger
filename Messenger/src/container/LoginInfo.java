package container;

import java.io.Serializable;

public class LoginInfo implements Serializable {
	private static final long serialVersionUID = -2672436705888817789L;
	private String identity, password;

	public LoginInfo(String identity, String password) {
		this.identity = identity;
		this.password = password;
	}

	public String getIdentity() {
		return identity;
	}

	public String getPassword() {
		return password;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
