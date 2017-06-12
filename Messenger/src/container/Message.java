package container;

public class Message {
	private final long time_created;
	private String sender;
	private String receiver;
	private Object msg;
	{
		this.time_created = System.currentTimeMillis();
	}
	public Message(String sender, String receiver, Object msg) {
		this.sender = sender;
		this.receiver = receiver;
		this.msg = msg;
	}
	public Message(LoginInfo login) {
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
}
