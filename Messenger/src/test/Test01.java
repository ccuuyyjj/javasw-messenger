package test;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import container.Connection;
import container.Friends;

public class Test01 {
	public static void main(String[] args) throws Exception {
		File target = new File("ffmpeg.exe");
		ServerSocket server = new ServerSocket(20000);
		System.out.println("���� �����");
		Socket socket = server.accept();
		System.out.println("���� ����");
		Connection conn = new Connection(socket);
		for(int i=0; i<10; i++){
			System.out.println("���� ���� ����");
			conn.sendFile(target);
			System.out.println("���� ���� ��");
			try{Thread.sleep(10);}catch(Exception e){}
			Friends f = new Friends("sadf");
			f.getFriends().add("asdfsadf");
			f.getFriends().add("afdbdbgf");
			System.out.println("������Ʈ " + f.getFriends() + " ���� ����");
			conn.sendObject(f);
			System.out.println("������Ʈ ���� ��");
			try{Thread.sleep(10);}catch(Exception e){}
		}
		try{Thread.sleep(10000);}catch(Exception e){}
		server.close();
	}
}
