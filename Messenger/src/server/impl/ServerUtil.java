package server.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import general.container.LoginInfo;

public class ServerUtil {
	private static File loginDB = new File("db", "loginDB.db");
	public static Boolean checkLogin(LoginInfo login) throws FileNotFoundException {
		String identity = login.getIdentity();
		String password = login.getPassword();
		Scanner s = new Scanner(loginDB);
		s.useDelimiter("\t");
		boolean result = false;
		while(s.hasNextLine()){
			if(s.next().equals(identity)){
				if(s.next().equals(password))
					result = true;
				else break;
			}
		}
		s.close();
		return result;
	}

}
