package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;

import general.container.Connection;
import general.container.Friends;
import general.container.Message;
import server.impl.ServerUtil;

public class Server {
	private static File loginDB; // ID/PW�� �����ϴ� DB���� (���ٿ� ���̵�� �н����� �ѽ�, �� ���̴�
						// ��(\t)���� ����)
	private static File friendsDB; // ID�� Ű, �� ���̵��� ģ������� ���� HashMap�� ����� DB����
	private static File filelistDB; // �Ʒ� filelist�� ������ �����ϴ� DB����
	private static HashMap<String, Friends> friends = null;
	private static HashMap<Message, File> filelist = null; // ������ ����� ���ϵ��� ���
	private static HashMap<String, Connection> clientlist = new HashMap<>(); // �����
														// Ŭ���̾�Ʈ����
														// ����Ǵ�
														// ����
	private static ServerSocket server; // �������� Ŭ���̾�Ʈ�� ������ ����ϴ� ����
	private static int port; // ������ ������ ����� ��Ʈ
	private static int timeout; // ���� ��� �ð�
	private static ServerListener listener; // Ŭ���̾�Ʈ�� ������ ����ϴ� ������

	private class ServerListener extends Thread {
		private boolean running; // ���� ��� �����尡 ��� ���ư����ϴ���
		{
			this.setDaemon(true);
		}

		@SuppressWarnings("unused")
		private boolean getRunning() {
			return this.running;
		}

		private void setRunning(boolean running) {
			this.running = running;
		}

		@Override
		public void run() {
			while (running) {
				try {
					server.setSoTimeout(timeout);
					Socket socket = server.accept();
					System.out.println(socket.getInetAddress() + "���� ������ ����");
					Connection conn = new Connection(socket);
					conn.InitServerUtil();
					ServerUtil util = conn.getServerUtil();
					// �α��� ���� Ȯ�� (�α��� ������ �ȿ��� ���� ����)
					String[] header = conn.getHeader();
					if (header[0].equals("OBJECT") && header[1].equals("Message")) {
						Message msg = (Message) conn.getObject(Integer.parseInt(header[2]));
						util.handleMessage(msg);
					} else
						conn.close();
				} catch (SocketTimeoutException e) {
					System.out.println(new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
							+ " ���� �����");
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (ClassNotFoundException e) {
					System.err.println(e.getMessage());
				}
			}
			for (String identity : clientlist.keySet()) {
				try {
					Connection conn = clientlist.get(identity);
					System.out.println("����� " + identity + "�� ���� ������...");
					conn.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	public Server(int port, int timeout) throws IOException, ClassNotFoundException {
		File dbdir = new File("db");
		if (!dbdir.exists() || !dbdir.isDirectory()) {
			System.out.println("DB������ �����մϴ�.");
			System.out.println("���� ��� : " + (dbdir.mkdirs() ? dbdir.getAbsolutePath() : "����"));
		}
		if (!dbdir.exists())
			System.out.println("���� ���� ����!");
		loginDB = new File("db", "loginDB.db");
		if (!loginDB.exists())
			loginDB.createNewFile();
		friendsDB = new File("db", "friendsDB.db");
		if (!friendsDB.exists()) {
			friendsDB.createNewFile();
			friends = new HashMap<>();
			closeFriends();
		}
		filelistDB = new File("db", "filelistDB.db");
		if (!filelistDB.exists()) {
			friendsDB.createNewFile();
			filelist = new HashMap<>();
			closeFileList();
		}
		Server.port = port;
		Server.timeout = timeout;
		server = new ServerSocket(Server.port);
		listener = new ServerListener();
		System.out.println("���� ���� �غ� �Ϸ�!");
	}

	public Server(int port) throws IOException, ClassNotFoundException {
		this(port, 5000);
	}

	public Server() throws IOException, ClassNotFoundException {
		this(20000, 5000);
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Server server = new Server();
		server.start();
		System.out.println("���� ���� �Ϸ�!");
		Scanner s = new Scanner(System.in);
		while (true)
			if (s.nextLine().equalsIgnoreCase("EXIT"))
				break;
		s.close();
	}

	public static File getLoginDB() {
		return loginDB;
	}

	public static File getFriendsDB() {
		return friendsDB;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<Message, File> getFileList() {
		if (filelist == null) {
			try {
				ObjectInputStream oin = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(filelistDB)));
				filelist = (HashMap<Message, File>) oin.readObject();
				oin.close();
			} catch (Exception e) {
			}
		}
		return filelist;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Friends> getFriends() {
		if (friends == null) {
			try {
				ObjectInputStream oin = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(friendsDB)));
				friends = (HashMap<String, Friends>) oin.readObject();
				oin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return friends;
	}

	public static void closeFileList() {
		if (filelist != null) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(filelistDB)));
				out.writeObject(filelist);
				out.flush();
				out.close();
			} catch (Exception e) {
			}
		}
		filelist.clear();
		filelist = null;
	}

	public static void closeFriends() {
		if (friends != null) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(friendsDB)));
				out.writeObject(friends);
				out.flush();
				out.close();
			} catch (Exception e) {
			}
		}
		friends.clear();
		friends = null;
	}

	public static HashMap<String, Connection> getClientList() {
		return clientlist;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static int getPort() {
		return port;
	}

	public static void setTimeout(int timeout) {
		Server.timeout = timeout;
	}

	public void start() {
		if (listener == null)
			listener = new ServerListener();
		listener.setRunning(true);
		listener.start();
	}

	public void stop() {
		listener.setRunning(false);
	}
}
