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
//					System.out.println("파일 수신 시작");
//					target = conn.getFile(header[1], Long.parseLong(header[2]));
//					System.out.println("파일 수신 완료");
//					break;
//				case "OBJECT":
//					System.out.println("오브젝트("+header[1]+") 수신 시작");
//					o = conn.getObject(Integer.parseInt(header[2]));
//					System.out.println("오브젝트("+header[1]+") 수신 완료");
//					break;
//				default:
//				}
//				if(target != null)
//					System.out.println("파일명 : " + target.getName() + " 파일크기: " + target.length());
//				if(o != null)
//					System.out.println("오브젝트명 : " + header[1] + " 내용: " + ((Friends)o).getFriends());
//			}
//		}
		{ //서버로 평문을 보낼때
			String sender = "me";
			String receiver = "you";
			String msg = "안녕안녕";
			Message m = new Message(sender, receiver, msg);
			conn.sendObject(m);
		}
		
		{ //서버에서 일어나는 일을 상상
			String[] header = conn.getHeader();
			if(header != null){
				switch(header[0]){
				case "OBJECT":
					Message m = (Message) conn.getObject(Integer.parseInt(header[2]));
					//여기서 conn은 you와 연결된 서버측의 conn
					conn.sendObject(m);
					break;
				}
			}
		}
		
	}
}
