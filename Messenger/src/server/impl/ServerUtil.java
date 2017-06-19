package server.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
					if(s.next().trim().equals(identity.trim())){
						if(s.next().trim().equals(password.trim()))
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
					PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(Server.getLoginDB(), true)));
					writer.println(idpw);
					writer.close();
				}
			}
			s.close();
		} catch (IOException e) {}
		return result;
	}
	public Friends getFriends(LoginInfo login) {
		Friends result = Server.getFriends().get(login.getIdentity());
		if(result == null){
			Server.getFriends().put(login.getIdentity(), new Friends());
			result = Server.getFriends().get(login.getIdentity());
		}
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
//					getFriends(login).getListname().add("asdf");
//					getFriends(login).getNickname().add("asdf");
					conn.sendObject(getFriends(login));
					Server.closeFriends();
					Server.getClientList().put(login.getIdentity(), conn);
					System.out.println("���� ������ �� : " + Server.getClientList().size());
					conn.InitServerReceiver();
					conn.getServerReceiver().start();
				} else {
					conn.close();
				}
			} catch (Exception e) {}
			break;
		case "Friends":
			try{
				String identity = msg.getSender();
				Friends friends = (Friends) msg.getMsg();
				Server.getFriends().put(identity, friends);
				Server.closeFriends();
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
				if(msg.getSender().equals(conn.getIdentity())){	//�޼����� ������ = ���� �����̸� ������ �޾ƿ;���
					String[] header = conn.getHeader();
					while(true){
						if(header != null){
							File tmp = conn.getFile(header[1], Long.parseLong(header[2]));
							Server.getFileList().put(msg, tmp);
							Server.closeFileList();
						}
					}
				} else {	//�޼����� ������ != ���� �����̸� ������ ���������
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
