package client;

import java.io.IOException;

import javax.swing.JFrame;

import client.gui.LoginGUI;
import general.container.Connection;
import general.container.Friends;

public class Client {
	public static Connection conn = null;
	public static JFrame currentGUI = null;
	public static Friends friends = null;
	public static String identity = null;

	public static void main(String[] args) throws IOException {
		currentGUI = new LoginGUI();
		
	}
	//메세지 수신
}
