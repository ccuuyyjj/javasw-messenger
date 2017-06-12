package container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friends implements Serializable{
	private static final long serialVersionUID = -3117451285694131816L;
	private String identity;
	private List<String> friends;
	
	public Friends(String identity) {
		this.identity = identity;
		this.friends = new ArrayList<>();
	}

	public String getIdentity() {
		return identity;
	}

	public List<String> getFriends() {
		return friends;
	}
}