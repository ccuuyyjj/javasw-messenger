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
		File target = new File("incomingstreamsample"); // �ӽ÷� ���Ͽ��� �޾ƿ����� ����
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
		int returncode = -1;	// ���̵� ������� -1 ��ȯ
		File target = new File("db", "LoginDB.db");
		Scanner s = new Scanner(target);
		s.useDelimiter("\t");
		while(s.hasNextLine()){
			String id = s.next();
			String pw = s.next();
			if(login.getIdentity().equals(id)){
				if(login.getPassword().equals(pw))
					returncode = 0;	// �α��� ������ 0 ��ȯ
				else
					returncode = 1;	// �н����� ����ġ�� 1 ��ȯ
				break;
			}
		}
		s.close();
		return returncode;
	}
}
