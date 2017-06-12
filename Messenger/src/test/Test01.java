package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import container.Friends;

public class Test01 {
	public static void main(String[] args) throws Exception {
		Friends f = new Friends("abc");
		f.getFriends().add("def");
		f.getFriends().add("ghi");
		System.out.println(f.getIdentity() + " " + f.getFriends());
		File target = new File("testfile");
		FileOutputStream out = new FileOutputStream(target);
		BufferedOutputStream buffer = new BufferedOutputStream(out);
		ObjectOutputStream writer = new ObjectOutputStream(buffer);
		writer.writeObject(f);
		writer.close();
	}
}
