package general.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friends implements Serializable{
	private static final long serialVersionUID = -3117451285694131816L;
	private Map<String,String> list=new HashMap<>();
	private List<String> listname=new ArrayList<>();		//사람 이름
	private List<String> nickname=new ArrayList<>();		//ID
	private int namecount=0;
	private int nickcount=0;
	
	public Friends() {
		
	}
	public List<String> getListname() {
		return listname;
	}
	public void setListname(String name) {
		this.listname.add(namecount, name);
		this.namecount=this.namecount+1;

	}
	

	public List<String> getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname.add(nickcount,nickname);
		this.nickcount=nickcount+1;

	}
	


	

	


}