package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import container.Connection;
import container.Friends;

public class Test02 {
	public static void main(String[] args) throws Exception {
//		File target = new File("testfile");
//		FileInputStream in = new FileInputStream(target);
		InetAddress inet = InetAddress.getByName("warrock.iptime.org");
		Socket socket = new Socket(inet, 20000);
		Connection conn = new Connection(socket);
		BufferedInputStream bis = conn.getIn();
		//Thread t = new Thread(()->{
		while(true){
			byte[] buffer = new byte[4096];
			bis.read(buffer, 0, buffer.length);
			String s = new String(buffer).trim();
			if(s.startsWith("\\")){
				Scanner scan = new Scanner(s);
				scan.useDelimiter("\\\\");
				String type = scan.next();
				if(type.equals("=[FILE]=")){
					System.out.println("파일 수신 시작");
					String name = scan.next();
					long length = Long.parseLong(scan.next());
					File target = conn.getFile(name, length);
					System.out.println("파일 수신 끝");
					System.out.println(target.getAbsolutePath());
				} else if(type.equals("=[OBJECT]=")){
					System.out.println("오브젝트 수신 시작");
					String name = scan.next();
					long length = Long.parseLong(scan.next());
					if(name.equals("Friends")){
						Friends f = (Friends) conn.getObject(length);
						System.out.println("오브젝트 수신 끝");
						System.out.println(f.getFriends());
					} else {
						conn.getObject(length);
					}
				}
			}
		}
		//});
		//t.setDaemon(true);
		//t.start();
	}
}
