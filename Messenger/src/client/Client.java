package client;

import java.util.List;

import javax.swing.JFrame;

import client.gui.LoginGUI;
import general.container.Connection;
import general.container.Friends;

public class Client {
	public static Connection conn = null;
	public static JFrame currentGUI = null;
	public static Friends friends = null;

	public static void main(String[] args) {
		currentGUI = new LoginGUI();
	}
}
