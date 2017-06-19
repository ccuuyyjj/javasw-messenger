package server.impl;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

import general.container.Connection;
import general.container.Friends;
import general.container.LoginInfo;
import general.container.Message;
import server.Server;

public class ServerUtil {
	private Connection conn = null;
	public ServerUtil(Connection conn) {
		this.conn = conn;
	}
	public static Boolean checkLogin(LoginInfo login){
		boolean result = false;
		String identity = login.getIdentity();
		String password = login.getPassword();
		try {
			Scanner s = new Scanner(Server.getLoginDB());
			s.useDelimiter("\t");
			if(login.getflag() == 1){
				while(s.hasNextLine()){
					if(s.next().equals(identity)){
						if(s.next().equals(password))
							result = true;
						else break;
					} else s.next();
				}
			} else {
				result = true;
				while(s.hasNextLine()){
					if(s.next().equals(identity)){
						result = false;
						break;
					} else s.next();
				}
				if(result){
					String idpw = identity + "\t" + password;
					PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(Server.getLoginDB())));
					writer.println(idpw);
					writer.close();
				}
			}
			s.close();
		} catch (IOException e) {}
		return result;
	}
	@SuppressWarnings("unchecked")
	public Friends getFriends(LoginInfo login) {
		Friends result = null;
		try{
			File db = Server.getFriendsDB();
			ObjectInputStream oin = new ObjectInputStream(
								new BufferedInputStream(
									new FileInputStream(db)));
			Map<String, Friends> friendsDB = (Map<String, Friends>) oin.readObject();
			result = friendsDB.get(login.getIdentity());
			oin.close();
		}catch(Exception e){}
		return result;
	}
	public void handleMessage(Message msg){
		String name = msg.getMsg().getClass().getSimpleName();
		switch(name){
		case "LoginInfo":
			try{
				LoginInfo login = (LoginInfo) msg.getMsg();
				Boolean check = checkLogin(login); 
				conn.sendObject(check);
				if(check){
					conn.setIdentity(login.getIdentity());
					conn.sendObject(getFriends(login));
					Server.getClientList().put(login.getIdentity(), conn);
				}
			} catch (Exception e) {}
			break;
		case "String":
			try{
				String receiver = msg.getReceiver();
				Connection conn = Server.getClientList().get(receiver);
				conn.sendObject(msg);
			} catch (IOException e) {}
			break;
		case "File":
			try {
				if(msg.getSender().equals(conn.getIdentity())){	//메세지의 보낸이 = 현재 연결이면 파일을 받아와야함
					String[] header = conn.getHeader();
					while(true){
						if(header != null){
							File tmp = conn.getFile(header[1], Long.parseLong(header[2]));
							Server.getFileList().put(msg, tmp);
							Server.closeFileList();
						}
					}
				} else {	//메세지의 보낸이 != 현재 연결이면 파일을 보내줘야함
					File tmp = Server.getFileList().get(msg);
					conn.sendFile(tmp);
					Server.closeFileList();
				}
				break;
			} catch (IOException e) {}
			break;
		}
	}
}
