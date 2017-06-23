package general.container;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Server;
import server.impl.ServerUtil;

public class Connection implements Closeable {
	private Socket socket;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private ServerUtil serverUtil = null;
	private ServerReceiver receiver = null;
	private String identity = null;

	public Connection(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedInputStream(socket.getInputStream());
		out = new BufferedOutputStream(socket.getOutputStream());
	}

	public void sendHeader(String[] header) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("\\");
		for (String arg : header)
			sb.append(arg).append("\\");
		byte[] tmp = sb.toString().getBytes();
		byte[] buffer = new byte[4096];
		for (int i = 0; i < buffer.length; i++) {
			if (i < tmp.length)
				buffer[i] = tmp[i];
			else
				buffer[i] = ' ';
		}
		out.write(buffer, 0, buffer.length);
		out.flush();
	}

	public void sendFile(File f) throws IOException {
		String[] header = new String[] { "FILE", f.getName(), String.valueOf(f.length()) };
		sendHeader(header);
		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f));
		byte[] buffer = new byte[4096];
		long sent = 0;
		while (sent != f.length()) {
			if ((f.length() - sent) < 4096)
				buffer = new byte[(int) (f.length() - sent)];
			int read = fin.read(buffer);
			if (read == -1)
				break;
			out.write(buffer, 0, read);
			out.flush();
			sent += read;
		}
		fin.close();
	}

	public void sendObject(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(o);
		int length = baos.size();
		String[] header = new String[] { "OBJECT", o.getClass().getSimpleName(), String.valueOf(length) };
		sendHeader(header);
		ByteArrayInputStream oin = new ByteArrayInputStream(baos.toByteArray(), 0, length);
		for (int i = 0; i < length; i++) {
			out.write(baos.toByteArray()[i]);
			out.flush();
		}
		oin.close();
	}

	public String[] getHeader() throws IOException { // String[] header =
										// conn.getHeader();
		byte[] buffer = new byte[4096];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = (byte) in.read();
			if (buffer[i] == -1)
				return null;
		}
		String tmp = new String(buffer, 0, buffer.length);
		String[] header = tmp.trim().substring(1).split("\\\\");
		return header;
	}

	public File getFile(String name, long length) throws IOException {
		File target = new File("received", name);
		if (!target.getParentFile().exists())
			target.getParentFile().mkdirs();
		while (target.exists())
			target = new File("received", "(new)" + target.getName());
		BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(target));
		byte[] buffer = new byte[4096];
		long received = 0;
		while (received < length) {
			if ((length - received) < 4096)
				buffer = new byte[(int) (length - received)];
			int read = in.read(buffer);
			if (read <= 0) {
				System.out.println("received : " + received + " 지점에서 읽기 실패");
				continue;
			}
			received += read;
			fout.write(buffer, 0, read);
			fout.flush();
		}
		fout.close();
		return target;
	}

	public Object getObject(int length) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < length; i++) {
			baos.write(in.read());
			baos.flush();
		}
		Object o = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
		return o;
	}

	public class ServerReceiver extends Thread {
		private Connection conn = null;
		private ServerUtil util = null;
		private boolean running;
		{
			this.setDaemon(true);
		}

		public ServerReceiver(Connection conn) {
			this.conn = conn;
			this.util = conn.getServerUtil();
			System.out.println(conn.identity + "에 대한 Receiver 설정 완료");
		}

		@Override
		public void run() {
			while (running) {
				try {
					String[] header = conn.getHeader();
					if (header != null) {
						if (header[0].equals("OBJECT") && header[1].equals("Message")) {
							System.out.println("header[0] : " + header[0] + " header[1] : " + header[1]);
							Message msg = (Message) conn.getObject(Integer.parseInt(header[2]));
							util.handleMessage(msg);
						} else if (header[0].equals("CLOSE")) {
							System.out.println("연결 종료 헤더 수신");
							conn.close();
						} else {
							System.out.println("서버 접근방법이 잘못됨!");
							conn.close();
						}
					}
				} catch (Exception e) {
				}
			}
		}

		public boolean isRunning() {
			return running;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedInputStream getIn() {
		return in;
	}

	public BufferedOutputStream getOut() {
		return out;
	}

	public ServerUtil getServerUtil() {
		return serverUtil;
	}

	public ServerReceiver getServerReceiver() {
		return receiver;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void InitServerUtil() {
		this.serverUtil = new ServerUtil(this);
	}

	public void InitServerReceiver() {
		this.receiver = new ServerReceiver(this);
		this.receiver.setRunning(true);
	}

	@Override
	public void close() {
		try {
			if (receiver != null) {
				if (Server.getClientList().remove(identity) != null)
					receiver.setRunning(false);
			} else
				sendHeader(new String[] { "CLOSE", "CLOSE", "CLOSE" });
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
