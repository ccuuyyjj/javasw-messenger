package server.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

	public static Boolean checkLogin(LoginInfo login) {
		boolean result = false;
		String identity = login.getIdentity();
		String password = login.getPassword();
		try {
			Scanner s = new Scanner(Server.getLoginDB());
			s.useDelimiter("[\t\n]");
			if (login.getflag() == 1) {
				while (s.hasNext()) {
					String id = s.next().trim();
					String pw = s.next().trim();
					if (id.equals(identity.trim())) {
						if (pw.equals(password.trim()))
							result = true;
						break;
					}
				}
			} else {
				result = true;
				while (s.hasNext()) {
					String id = s.next().trim();
					s.next();
					if (id.equals(identity)) {
						result = false;
						break;
					}
				}
				if (result) {
					String idpw = identity + "\t" + password;
					System.out.println("회원가입 : " + idpw);
					s.close();
					PrintWriter writer = new PrintWriter(
							new BufferedWriter(new FileWriter(Server.getLoginDB(), true)));
					writer.println(idpw);
					writer.close();
				}
			}
			s.close();
		} catch (IOException e) {
		}
		return result;
	}

	public Friends getFriends(LoginInfo login) {
		Friends result = Server.getFriends().get(login.getIdentity());
		if (result == null) {
			Server.getFriends().put(login.getIdentity(), new Friends());
			result = Server.getFriends().get(login.getIdentity());
		}
		return result;
	}

	public void handleMessage(Message msg) {
		String name = msg.getMsg().getClass().getSimpleName();
		System.out.println("getMsg() classname : " + name);
		switch (name) {
		case "LoginInfo":
			try {
				LoginInfo login = (LoginInfo) msg.getMsg();
				Boolean check = checkLogin(login);
				conn.sendObject(check);
				if (check) {
					conn.setIdentity(login.getIdentity());
					// getFriends(login).getListname().add("asdf");
					// getFriends(login).getNickname().add("asdf");
					conn.sendObject(getFriends(login));
					Server.closeFriends();
					Server.getClientList().put(login.getIdentity(), conn);
					System.out.println("현재 접속자 수 : " + Server.getClientList().size());
					conn.InitServerReceiver();
					conn.getServerReceiver().start();
				} else {
					conn.close();
				}
			} catch (Exception e) {
			}
			break;
		case "Friends":
			try {
				String identity = msg.getSender();
				Friends friends = (Friends) msg.getMsg();
				boolean result = true;
				for (String id : friends.getFriendsList().keySet()) {
					Scanner s = new Scanner(Server.getLoginDB());
					s.useDelimiter("[\t\n]");
					while (s.hasNext()) {
						String str = s.next().trim();
						s.next();
						if (str.equals(id)) {
							result = true;
							break;
						} else
							result = false;
					}
					s.close();
					if (!result)
						break;
				}
				if (result) {
					Server.getFriends().put(identity, friends);
					conn.sendObject(friends);
					Server.closeFriends();
				} else {
					conn.sendObject(Server.getFriends().get(identity));
					Server.closeFriends();
				}
			} catch (Exception e) {
			}
			break;
		case "String":
			try {
				String receiver = msg.getReceiver();
				if (receiver.equals("=[BROADCAST]=")) {
					System.out.println("Broadcast msg received");
					for (Connection conn : Server.getClientList().values())
						conn.sendObject(msg);
					System.out.println("Broadcast msg sent");
				} else {
					Connection conn = Server.getClientList().get(receiver);
					if (conn != null)
						conn.sendObject(msg);
					else
						System.out.println(receiver + "는 접속중이 아님.");
				}
			} catch (IOException e) {
			}
			break;
		case "File":
			System.out.println("여기까진 넘어옴");
			try {
				if (msg.getSender().equals(conn.getIdentity())) { // 메세지의
													// 보낸이
													// =
													// 현재
													// 연결이면
													// 파일을
													// 받아와야함
					String[] header = conn.getHeader();
					while (true) {
						if (header != null) {
							System.out.println("header[0] : " + header[0] + " header[1] : " + header[1]);
							File tmp = conn.getFile(header[1], Long.parseLong(header[2]));
							Server.getFileList().put(msg.getTime_created(), tmp);
							Server.closeFileList();
							break;
						}
					}
					System.out.println("파일 수신 완료");
					String receiver = msg.getReceiver();
					Connection conn = Server.getClientList().get(receiver);
					if (conn != null)
						conn.sendObject(msg);
					else
						System.out.println(receiver + "는 접속중이 아님.");
				} else { // 메세지의 보낸이 != 현재 연결이면 파일을 보내줘야함
					System.out.println("msg = " + msg.toString());
					File tmp = Server.getFileList().remove(msg.getTime_created());
					System.out.println(tmp.getAbsolutePath() + " 전송 시작");
					conn.sendFile(tmp);
					System.out.println("파일 전송 완료");
					tmp.delete();
					Server.closeFileList();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
}
