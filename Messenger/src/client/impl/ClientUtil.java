package client.impl;

import java.net.InetAddress;
import java.net.Socket;

import client.Client;
import general.container.Connection;
import general.container.Friends;
import general.container.LoginInfo;
import general.container.Message;

public class ClientUtil {
	
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
		System.out.println(header);
		if(header[0].equals("OBJECT") && header[1].equals("Boolean")){
			Boolean recvmsg = (Boolean) Client.conn.getObject(Integer.parseInt(header[2]));
			if(recvmsg) result = true;
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
		System.out.println(header);
		if(header[0].equals("OBJECT") && header[1].equals("Boolean")){
			Boolean recvmsg = (Boolean) Client.conn.getObject(Integer.parseInt(header[2]));
			System.out.println(recvmsg);
			if(recvmsg){
				result = true;
//				로그인 결과값이 true일 경우 친구 목록 받아오기
				while(true){
					String[] fheader = Client.conn.getHeader();
					System.out.println(fheader);
					if(fheader != null){
						if(fheader[0].equals("OBJECT") && fheader[1].equals("Friends"))
							Client.friends = (Friends) Client.conn.getObject(Integer.parseInt(fheader[2]));
						System.out.println(Client.friends.getListname().toString());
						break;
					}
				}
			}
		}
		
		return result;
	} // 로그인 버튼을 누르면 Message형태로 LoginInfo(id, pw, 로그인flag)를 서버에 보내고 결과값(t/f)을 받아옴
}
