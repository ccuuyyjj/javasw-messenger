package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import container.Friends;

public class Test02 {
	public static void main(String[] args) throws Exception {
		File target = new File("testfile");
		FileInputStream in = new FileInputStream(target);
		BufferedInputStream buffer = new BufferedInputStream(in);
		ObjectInputStream writer = new ObjectInputStream(buffer);
		Friends f = (Friends) writer.readObject();
		writer.close();
		System.out.println(f.getIdentity() + " " + f.getFriends());
	}
}
