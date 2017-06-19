package test;

import java.io.File;
import java.net.InetAddress;
import java.net.Socket;

import general.container.Connection;
import general.container.Friends;
import general.container.Message;

public class Test02 {
	public static void main(String[] args) throws Exception {
		InetAddress inet = InetAddress.getByName("warrock.iptime.org");
		Socket socket = new Socket(inet, 20000);
		Connection conn = new Connection(socket);
//		while(true){
//			String[] header = conn.getHeader();
//			if(header != null){
//				File target = null;
//				Object o = null;
//				switch(header[0]){
//				case "FILE":
//					System.out.println("���� ���� ����");
//					target = conn.getFile(header[1], Long.parseLong(header[2]));
//					System.out.println("���� ���� �Ϸ�");
//					break;
//				case "OBJECT":
//					System.out.println("������Ʈ("+header[1]+") ���� ����");
//					o = conn.getObject(Integer.parseInt(header[2]));
//					System.out.println("������Ʈ("+header[1]+") ���� �Ϸ�");
//					break;
//				default:
//				}
//				if(target != null)
//					System.out.println("���ϸ� : " + target.getName() + " ����ũ��: " + target.length());
//				if(o != null)
//					System.out.println("������Ʈ�� : " + header[1] + " ����: " + ((Friends)o).getFriends());
//			}
//		}
		{ //������ ���� ������
			String sender = "me";
			String receiver = "you";
			String msg = "�ȳ�ȳ�";
			Message m = new Message(sender, receiver, msg);
			conn.sendObject(m);
		}
		
		{ //�������� �Ͼ�� ���� ���
			String[] header = conn.getHeader();
			if(header != null){
				switch(header[0]){
				case "OBJECT":
					Message m = (Message) conn.getObject(Integer.parseInt(header[2]));
					//���⼭ conn�� you�� ����� �������� conn
					conn.sendObject(m);
					break;
				}
			}
		}
		
	}
}
