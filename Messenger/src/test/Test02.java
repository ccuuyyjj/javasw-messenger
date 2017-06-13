package test;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import container.Friends;

public class Test02 {
	public static void main(String[] args) throws Exception {
//		File target = new File("testfile");
//		FileInputStream in = new FileInputStream(target);
		InetAddress inet = InetAddress.getByName("warrock.iptime.org");
		Socket socket = new Socket(inet, 20000);
		BufferedInputStream buffer = new BufferedInputStream(socket.getInputStream());
		ObjectInputStream writer = new ObjectInputStream(buffer);
		Friends f = (Friends) writer.readObject();
		writer.close();
		socket.close();
		System.out.println(f.getIdentity() + " " + f.getFriends());
	}
}
