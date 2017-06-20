package client;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;

import client.gui.ChatRoomGUI;
import client.gui.FriendsListGUI;
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
	public static ClientReceiver receiver = new ClientReceiver();

	public static void main(String[] args) throws IOException {
		currentMainGUI = new LoginGUI();
	}
	// 메세지 수신
	public static class ClientReceiver extends Thread {
		private boolean running = true;
		
		@Override
		public void run() {
			while (running) {
				try {
					String[] header = conn.getHeader();
					if (header != null) {
						System.out.println("Client에서 받은 header : " + header[1]);
						if (header[1].equals("Message")) {
							Message msg = (Message) conn.getObject(Integer.parseInt(header[2]));
							String sender = msg.getSender();
							ChatRoomGUI gui = chatList.get(sender);
							if(gui == null){
								gui = new ChatRoomGUI(msg.getSender());
								chatList.put(sender, gui);
							}
							gui.messageHandler(msg);
						} else if(header[1].equals("Friends")){
							Client.friends = (Friends) Client.conn.getObject(Integer.parseInt(header[2]));
							((FriendsListGUI) currentMainGUI).load();
							((FriendsListGUI) currentMainGUI).getModel().reload();
						}
					}
				} catch (IOException e) {
					System.out.println("접속 종료");
				} catch (ClassNotFoundException e) {}
			}
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
	}
}
