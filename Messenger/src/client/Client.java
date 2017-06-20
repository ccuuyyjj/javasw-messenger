package client;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;

import client.gui.ChatRoomGUI;
import client.gui.LoginGUI;
import general.container.Connection;
import general.container.Friends;
import general.container.Message;

public class Client {
	public static Connection conn = null;
	public static JFrame currentMainGUI = null;
	public static Friends friends = null;
	public static String identity = null;
	public static HashMap<String, ChatRoomGUI> chatList = new HashMap<>();
	public static ClientReceiver receiver;

	public static void main(String[] args) throws IOException {
		currentMainGUI = new LoginGUI();
	}
	// 메세지 수신
	class ClientReceiver extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					String[] header = conn.getHeader();
					if (header != null) {
						if (header[1].equals("Message")) {
							Message msg = (Message) conn.getObject(Integer.parseInt(header[2]));
							String sender = msg.getSender();
							ChatRoomGUI gui = chatList.get(sender);
							if(gui == null){
								gui = new ChatRoomGUI(msg.getSender());
								chatList.put(sender, gui);
							}
							gui.messageHandler(msg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
