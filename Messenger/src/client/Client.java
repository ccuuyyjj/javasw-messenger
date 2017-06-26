package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.UIManager;

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
	public static HashSet<String> online = null;
	public static String identity = null;
	public static HashMap<String, ChatRoomGUI> chatList = null;
	public static ClientReceiver receiver = null;

	public static void main(String[] args) throws IOException {
		
		/*lookNfell 설정*/
		try{
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		}catch(Exception e){
			e.printStackTrace();
		}
		currentMainGUI = new LoginGUI();
		
	}

	// 메세지 수신
	public static class ClientReceiver extends Thread {
		private boolean running = true;

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			while (running) {
				try {
					String[] header = conn.getHeader();
					if (header != null) {
						System.out.println("Client (" + Client.identity + ")에서 받은 header : " + header[1]);
						if (header[0].equals("OBJECT")) {
							if (header[1].equals("Message")) {
								Message msg = (Message) conn.getObject(Integer.parseInt(header[2]));
								String recv = msg.getReceiver();
								if (recv.equals("=[BROADCAST]="))
									((FriendsListGUI) currentMainGUI).messageHandler(msg);
								else {
									String sender = msg.getSender();
									ChatRoomGUI gui = chatList.get(sender);
									if (gui == null) {
										gui = new ChatRoomGUI(msg.getSender());
										chatList.put(sender, gui);
										gui.setVisible(false);
									}
									if(!gui.isVisible()){
										((FriendsListGUI) currentMainGUI).selectUser(sender);
									}
									gui.messageHandler(msg);
								}
							} else if (header[1].equals("Friends")) {
//								Client.friends = (Friends) Client.conn
//										.getObject(Integer.parseInt(header[2]));
								Friends tmp = (Friends) Client.conn
										.getObject(Integer.parseInt(header[2]));
								if(tmp.getFriendsList().size() > Client.friends.getFriendsList().size()){
									//친구가 새로 로그인한 경우
									for(String id : tmp.getFriendsList().keySet()){
										if(!Client.friends.getFriendsList().containsKey(id)){
											//id는 새로 로그인한 친구의 아이디
											
											//↑여기에 코드를 작성
											break;
										}
									}
								}else if(tmp.getFriendsList().size() < Client.friends.getFriendsList().size()){
									//친구가 로그아웃한 경우
								}
								Client.friends = tmp;
								while(true){
									String[] oheader = Client.conn.getHeader();
									if(oheader != null){
										Client.online = (HashSet<String>) Client.conn
												.getObject(Integer.parseInt(oheader[2]));
										break;
									}
								}
								((FriendsListGUI) currentMainGUI).load();
							}
						} else if (header[0].equals("FILE")) {
							File tmp = Client.conn.getFile(header[1], Long.parseLong(header[2]));
							File target = null;
							for (ChatRoomGUI gui : chatList.values()) {
								if (gui.target != null) {
									target = gui.target;
									System.out.println("저장 경로 : " + target.getAbsolutePath());
									if (target != null){
										Files.move(Paths.get(tmp.getAbsolutePath()),
												Paths.get(target.getAbsolutePath()),
												StandardCopyOption.REPLACE_EXISTING);
									}
									gui.target = null;
									break;
								}
							}
						}
					}
				} catch (IOException e) {
					System.out.println("접속 종료");
					setRunning(false);
				} catch (ClassNotFoundException e) {
				}
			}
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
	}
}
