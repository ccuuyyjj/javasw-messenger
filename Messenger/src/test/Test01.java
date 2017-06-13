package test;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import container.Friends;

public class Test01 {
	public static void main(String[] args) throws Exception {
		Friends f = new Friends("abc");
		f.getFriends().add("def");
		f.getFriends().add("ghi");
		System.out.println(f.getIdentity() + " " + f.getFriends());
//		File target = new File("testfile");
//		FileOutputStream out = new FileOutputStream(target);
		ServerSocket server = new ServerSocket(20000);
		Socket socket = server.accept();
		BufferedOutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
		ObjectOutputStream writer = new ObjectOutputStream(buffer);
		writer.writeObject(f);
		writer.close();
		server.close();
	}
}
