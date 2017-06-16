package client;

import javax.swing.JFrame;

import client.gui.LoginGUI;
import general.container.Connection;

public class Client {
	public static Connection conn = null;
	public static JFrame currentGUI = null;
	public static void main(String[] args) {
		currentGUI = new LoginGUI();
	}
}
