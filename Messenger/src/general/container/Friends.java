package general.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Friends implements Serializable {
	private static final long serialVersionUID = -3117451285694131816L;

	private Map<String, String> friendsList = new HashMap<>();

	public Friends() {

	}

	public Map<String, String> getFriendsList() {
		return friendsList;
	}

	public void add(String id, String nickname) {
		friendsList.put(id, nickname);
	}

}