package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import general.container.Connection;
import general.container.Message;
import server.impl.ServerUtil;

public class Server {
	private static File loginDB;	//ID/PW를 저장하는 DB파일 (한줄에 아이디와 패스워드 한쌍, 그 사이는 탭(\t)으로 구분)
	private static File friendsDB;	//ID가 키, 그 아이디의 친구목록이 값인 HashMap이 저장된 DB파일
	private static Map<String, Connection> clientlist = new HashMap<>();	//연결된 클라이언트들이 저장되는 공간
	private static ServerSocket server;	//서버에서 클라이언트의 접속을 대기하는 소켓
	private static int port;	//서버가 접속을 대기할 포트
	private static int timeout;	//접속 대기 시간
	private static ServerListener listener;	//클라이언트의 접속을 대기하는 스레드
	private class ServerListener extends Thread {
		private boolean running;	//접속 대기 스레드가 계속 돌아가야하는지
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
			while(running){
				try {
					server.setSoTimeout(timeout);
					Socket socket = server.accept();
					Connection conn = new Connection(socket);
					ServerUtil util = new ServerUtil(conn);
					//로그인 정보 확인 (로그인 정보가 안오면 접속 종료)
					String[] header = conn.getHeader();
					if(header[0].equals("OBJECT") && header[1].equals("Message")){
						Message msg = (Message) conn.getObject(Integer.parseInt(header[2]));
						util.handleMessage(msg);
					} else conn.close();
				} catch (SocketTimeoutException e) {
					System.out.println(new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + " 서버 대기중");
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (ClassNotFoundException e) {
					System.err.println(e.getMessage());
				}
			}
			for(String identity : clientlist.keySet()){
				try {
					Connection conn = clientlist.get(identity);
					System.out.println("사용자 " + identity + "의 접속 종료중...");
					conn.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
	public Server(int port, int timeout) throws IOException{
		File dbdir = new File("db");
		if(!dbdir.exists() || !dbdir.isDirectory()){
			System.out.println("DB폴더를 생성합니다.");
			System.out.println("생성 결과 : " + (dbdir.mkdirs()?dbdir.getAbsolutePath():"실패"));
		}
		if(!dbdir.exists())
			System.out.println("서버 구동 실패!");
		loginDB = new File("db", "loginDB.db");
		friendsDB = new File("db", "friendsDB.db");
		Server.port = port;
		Server.timeout = timeout;
		server = new ServerSocket(Server.port);
		listener = new ServerListener();
		System.out.println("서버 구동 준비 완료!");
	}
	public Server(int port) throws IOException{
		this(port, 5000);
	}
	public Server() throws IOException{
		this(20000, 5000);
	}
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
		System.out.println("서버 구동 완료!");
		Scanner s = new Scanner(System.in);
		while(true)
			if(s.nextLine().equalsIgnoreCase("EXIT")) break;
		s.close();
	}
	public static File getLoginDB(){
		return loginDB;
	}
	public static File getFriendsDB(){
		return friendsDB;
	}
	public static Map<String, Connection> getClientList(){
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
	public void start(){
		if(listener == null)
			listener = new ServerListener();
		listener.setRunning(true);
		listener.start();
	}
	public void stop(){
		listener.setRunning(false);
	}
}
