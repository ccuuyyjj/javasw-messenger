package client.impl;

import java.net.InetAddress;
import java.net.Socket;

import client.Client;
import general.container.Connection;
import general.container.Friends;
import general.container.LoginInfo;
import general.container.Message;

public class LoginImpl {

	public static boolean login(String id, String pw, String addr) throws Exception {
		boolean result = false;
		if(Client.conn == null){
			Socket socket = new Socket(InetAddress.getByName(addr), 20000);
			Client.conn = new Connection(socket);
		}
		LoginInfo info = new LoginInfo(id, pw);
		Message sendmsg = new Message(info);
		Client.conn.sendObject(sendmsg);
		String[] header = Client.conn.getHeader();
		if(header[0].equals("OBJECT") && header[1].equals("Message")){
			Message recvmsg = (Message) Client.conn.getObject(Integer.parseInt(header[2]));
			if((Boolean)(recvmsg.getMsg()))
				result = true;
		}
		
//		친구 목록 받아오기
		String[] fheader = Client.conn.getHeader();
		if(fheader[0].equals("OBJECT") && fheader[1].equals("Friends")) {
			Client.friends = (Friends) Client.conn.getObject(Integer.parseInt(header[2]));
		}
		
		return result;
	}
	
	public static boolean join(String id, String pw, String addr) throws Exception {
		boolean result = false;
		if(Client.conn == null) {
			Socket socket = new Socket(InetAddress.getByName(addr), 20000);
			Client.conn = new Connection(socket);
		}
		LoginInfo info = new LoginInfo(id, pw);
		Message sendmsg = new Message(info);
		Client.conn.sendObject(sendmsg);
		String[] header = Client.conn.getHeader();
		if(header[0].equals("OBJECT") && header[1].equals("Message")){
			Message recvmsg = (Message) Client.conn.getObject(Integer.parseInt(header[2]));
			if((Boolean)(recvmsg.getMsg()))
				result = true;
		}
		return result;
	}
}
