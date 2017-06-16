package server.impl;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import general.container.Connection;

public class Server implements Closeable {
	private ServerSocket server;
	private int port;
	private int timeout;
	private List<Connection> clientlist = new ArrayList<>();
	private ServerThread listener;
	private class ServerThread extends Thread {
		private boolean running;
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
					clientlist.add(conn);
					onConnection(conn);
				} catch (IOException e) {
					System.err.println(e.getMessage());
					continue;
				}
			}
		}
	}
	public Server(int port, int timeout) throws IOException{
		this.port = port;
		this.timeout = timeout;
		server = new ServerSocket(this.port);
		listener = new ServerThread();
	}
	public void onConnection(Connection conn){} // Override to use
	public Server(int port) throws IOException{
		this(port, 5);
	}
	public Server() throws IOException{
		this(20000, 5);
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getPort() {
		return port;
	}
	public List<Connection> getClientlist() {
		return clientlist;
	}
	public void start(){
		listener.setRunning(true);
		listener.start();
	}
	public void stop(){
		listener.setRunning(false);
	}
	public void close() throws IOException{
		for(Connection conn : clientlist){
			try {
				conn.sendHeader(new String[]{"SERVERSHUTDOWN"});
				conn.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			} finally {
				clientlist.remove(conn);
			}
		}
		this.stop();
		server.close();
	}
}
