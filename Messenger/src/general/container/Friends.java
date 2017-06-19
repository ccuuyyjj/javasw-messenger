package general.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friends implements Serializable{
	private static final long serialVersionUID = -3117451285694131816L;
	private List<String> listname=new ArrayList<>();
	private List<String> nickname=new ArrayList<>();
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
		System.out.println("name"+namecount);
	}
	

	public List<String> getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname.add(nickcount,nickname);
		this.nickcount=nickcount+1;
		System.out.println("nick"+nickcount);
	}
	


	

	


}