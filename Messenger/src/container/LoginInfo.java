package container;

public class LoginInfo {
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
