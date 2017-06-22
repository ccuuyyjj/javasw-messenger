package general.container;

import java.io.Serializable;

public class Message implements Serializable, Comparable<Message> {
	private static final long serialVersionUID = -868641557643453348L;
	private final long time_created;
	private String sender;
	private String receiver;
	private Object msg;
	{
		this.time_created = System.currentTimeMillis(); // 메세지가 만들어진 시간을 고정
										// (변경 불가)
	}

	public Message(String sender, String receiver, Object msg) { // 평범한 메세지 or
												// 파일 첨부
		this.sender = sender;
		this.receiver = receiver;
		this.msg = msg;
	}

	public Message(LoginInfo login) { // 로그인시 서버로 보내는 정보
		this.sender = login.getIdentity();
		this.receiver = "=SERVER=";
		this.msg = (Object) login;
	}

	public long getTime_created() {
		return time_created;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public Object getMsg() {
		return msg;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	@Override
	public int compareTo(Message o) {
		Long me = this.time_created;
		Long you = o.time_created;
		return me.compareTo(you);
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass().equals(this.getClass()) && (this.time_created == ((Message) obj).time_created);
	}
}
