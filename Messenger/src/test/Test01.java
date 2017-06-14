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
		System.out.println("연결 대기중");
		Socket socket = server.accept();
		System.out.println("연결 성공");
		Connection conn = new Connection(socket);
		for(int i=0; i<10; i++){
			System.out.println("파일 전송 시작");
			conn.sendFile(target);
			System.out.println("파일 전송 끝");
			try{Thread.sleep(10);}catch(Exception e){}
			Friends f = new Friends("sadf");
			f.getFriends().add("asdfsadf");
			f.getFriends().add("afdbdbgf");
			System.out.println("오브젝트 " + f.getFriends() + " 전송 시작");
			conn.sendObject(f);
			System.out.println("오브젝트 전송 끝");
			try{Thread.sleep(10);}catch(Exception e){}
		}
		try{Thread.sleep(10000);}catch(Exception e){}
		server.close();
	}
}
