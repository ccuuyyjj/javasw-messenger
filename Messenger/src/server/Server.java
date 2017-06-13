package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.Scanner;

import container.LoginInfo;
import container.Message;

public class Server {
	
	public static void main(String[] args) throws Exception {
		File target = new File("incomingstreamsample"); // 임시로 파일에서 받아오도록 설정
		FileInputStream in = new FileInputStream(target);
		BufferedInputStream buffer = new BufferedInputStream(in);
		ObjectInputStream object = new ObjectInputStream(buffer);
		Message received = (Message) object.readObject();
		if(received.getReceiver().equals("=SERVER=")){
			LoginInfo login = (LoginInfo) received.getMsg();
			switch(login_process(login)){
			case 0:
				
			}
		}
	}
	
	private static int login_process(LoginInfo login) throws Exception{
		int returncode = -1;	// 아이디 미존재시 -1 반환
		File target = new File("db", "LoginDB.db");
		Scanner s = new Scanner(target);
		s.useDelimiter("\t");
		while(s.hasNextLine()){
			String id = s.next();
			String pw = s.next();
			if(login.getIdentity().equals(id)){
				if(login.getPassword().equals(pw))
					returncode = 0;	// 로그인 성공시 0 반환
				else
					returncode = 1;	// 패스워드 불일치시 1 반환
				break;
			}
		}
		s.close();
		return returncode;
	}
}
