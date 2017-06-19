package client.impl;

import java.net.InetAddress;
import java.net.Socket;

import client.Client;
import general.container.Connection;
import general.container.Friends;
import general.container.LoginInfo;
import general.container.Message;

public class LoginImpl {
	
	final static int JOIN = 0;
	final static int LOGIN = 1;
	
	public static boolean join(String id, String pw, String addr) throws Exception {
		boolean result = false;
		if(Client.conn == null) {
			Socket socket = new Socket(InetAddress.getByName(addr), 20000);
			Client.conn = new Connection(socket);
		}
		LoginInfo info = new LoginInfo(id, pw, JOIN);
		Message sendmsg = new Message(info);
		Client.conn.sendObject(sendmsg);
		String[] header = Client.conn.getHeader();
		if(header[0].equals("OBJECT") && header[1].equals("Message")){
			Message recvmsg = (Message) Client.conn.getObject(Integer.parseInt(header[2]));
			if((Boolean)(recvmsg.getMsg()))
				result = true;
		}
		return result;
	} // 회원가입 버튼을 누르면 Message형태로 LoginInfo(id, pw, 회원가입flag)를 서버에 보내고 결과값(t/f)을 받아옴

	public static boolean login(String id, String pw, String addr) throws Exception {
		boolean result = false;
		if(Client.conn == null){
			Socket socket = new Socket(InetAddress.getByName(addr), 20000);
			Client.conn = new Connection(socket);
		}
		LoginInfo info = new LoginInfo(id, pw, LOGIN);
		Message sendmsg = new Message(info);
		Client.conn.sendObject(sendmsg);
		String[] header = Client.conn.getHeader();
		if(header[0].equals("OBJECT") && header[1].equals("Message")){
			Message recvmsg = (Message) Client.conn.getObject(Integer.parseInt(header[2]));
			if((Boolean)(recvmsg.getMsg()))
				result = true;

//				로그인 결과값이 true일 경우 친구 목록 받아오기
				String[] fheader = Client.conn.getHeader();
				if(fheader[0].equals("OBJECT") && fheader[1].equals("Friends")) {
					Client.friends = (Friends) Client.conn.getObject(Integer.parseInt(header[2]));
				}
		}
		
		return result;
	} // 로그인 버튼을 누르면 Message형태로 LoginInfo(id, pw, 로그인flag)를 서버에 보내고 결과값(t/f)을 받아옴
}
