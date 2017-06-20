package general.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friends implements Serializable {
	private static final long serialVersionUID = -3117451285694131816L;

	private List<String> listname = new ArrayList<String>(); // 사람 이름
	private List<String> nickname = new ArrayList<String>(); // ID
	private String target;//FriendsList에서 채팅 시작시 받아올 상대방의 이름
	
	public Friends() {
		listname.lastIndexOf(listname);
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<String> getListname() {
		return listname;
	}

	public void setListname(String name) {

		this.listname.add(name);
	}

	public List<String> getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {

		this.nickname.add(nickname);

	}

}