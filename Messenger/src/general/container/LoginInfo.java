package general.container;

import java.io.Serializable;

public class LoginInfo implements Serializable {
	private static final long serialVersionUID = -2672436705888817789L;
	private String identity, password;
	private int flag; // join老 版快 0, login老 版快 1

	public LoginInfo(String identity, String password, int flag) {
		this.identity = identity;
		this.password = password;
	}

	public String getIdentity() {
		return identity;
	}

	public String getPassword() {
		return password;
	}
	
	public int getflag() {
		return flag;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
