package server.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
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
	public static Boolean checkLogin(LoginInfo login) throws FileNotFoundException {
		String identity = login.getIdentity();
		String password = login.getPassword();
		Scanner s = new Scanner(Server.getLoginDB());
		s.useDelimiter("\t");
		boolean result = false;
		while(s.hasNextLine()){
			if(s.next().equals(identity)){
				if(s.next().equals(password))
					result = true;
				else break;
			}
		}
		s.close();
		return result;
	}
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
					conn.sendObject(getFriends(login));
					Server.getClientList().put(login.getIdentity(), conn);
				}
			} catch (Exception e) {}
			break;
		case "String":
			
			break;
		}
	}

}
