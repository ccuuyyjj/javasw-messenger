package client;

import java.awt.Frame;
import java.awt.TrayIcon.MessageType;
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
		
		/*lookNfell ����*/
		try{
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		}catch(Exception e){
			e.printStackTrace();
		}
		currentMainGUI = new LoginGUI();
		
	}

	// �޼��� ����
	public static class ClientReceiver extends Thread {
		private boolean running = true;

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			while (running) {
				try {
					String[] header = conn.getHeader();
					if (header != null) {
						System.out.println("Client (" + Client.identity + ")���� ���� header : " + header[1]);
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
										gui.setVisible(true);
										gui.setState(Frame.ICONIFIED);
										gui.requestFocus();
									}
									gui.messageHandler(msg);
//									if(!((FriendsListGUI) currentMainGUI).isVisible())
//										((FriendsListGUI) currentMainGUI).setVisible(true);
//									if(!gui.isVisible())
//										((FriendsListGUI) currentMainGUI).selectUser(sender);
								}
							} else if (header[1].equals("Friends")) {
								Client.friends = (Friends) Client.conn
										.getObject(Integer.parseInt(header[2]));
								while(true){
									String[] oheader = Client.conn.getHeader();
									if(oheader != null){
										HashSet<String> tmp = (HashSet<String>) Client.conn
												.getObject(Integer.parseInt(oheader[2]));
										if(tmp.size() > Client.online.size()){
											//ģ���� ���� �α����� ���
											for(String id : tmp){
												if(!Client.online.contains(id)){
													//id�� ���� �α����� ģ���� ���̵�
													String nick = Client.friends.getFriendsList().get(id);
													((FriendsListGUI) currentMainGUI).showTrayMessage("�˸� ("+Client.identity+")", nick + "(" + id + ")���� �α����ϼ̽��ϴ�.", MessageType.INFO);
													//�迩�⿡ �ڵ带 �ۼ�
													break;
												}
											}
										}else if(tmp.size() < Client.online.size()){
											//ģ���� �α׾ƿ��� ���
										}
										Client.online = tmp;
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
									System.out.println("���� ��� : " + target.getAbsolutePath());
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
					System.out.println("���� ����");
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
