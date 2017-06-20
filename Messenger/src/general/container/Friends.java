package general.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friends implements Serializable {
	private static final long serialVersionUID = -3117451285694131816L;

	private List<String> listname = new ArrayList<String>(); // ��� �̸�
	private List<String> nickname = new ArrayList<String>(); // ID
	private String target;//FriendsList���� ä�� ���۽� �޾ƿ� ������ �̸�
	
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