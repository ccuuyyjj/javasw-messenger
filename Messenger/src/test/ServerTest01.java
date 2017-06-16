package test;

import java.io.File;
import java.io.IOException;

import general.container.Connection;
import general.container.Friends;
import server.impl.Server;

public class ServerTest01 {
	public static void main(String[] args) throws IOException {
		File target = new File("ffmpeg.exe");
		Friends f = new Friends("sadf");
		f.getFriends().add("asdfsadf");
		f.getFriends().add("afdbdbgf");
		
		Server server = new Server(){
			@Override
			public void onConnection(Connection conn) {
				onConnection(conn);
			}
		};
		server.start();
	}
	protected static void onConnection(Connection conn) {
		conn.getReceiver().start();
	}
}
