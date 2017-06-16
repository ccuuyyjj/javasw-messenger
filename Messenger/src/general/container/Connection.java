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

public class Connection implements Closeable {
	private Socket socket;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private Receiver receiver;
	public Connection(Socket socket) throws IOException{
		this.socket = socket;
		in = new BufferedInputStream(socket.getInputStream());
		out = new BufferedOutputStream(socket.getOutputStream());
		receiver = new Receiver();
		receiver.setDaemon(true);
	}
	public class Receiver extends Thread {
		@Override
		public void run() {
			while(!socket.isClosed()){
				try {
					String[] header = getHeader();
					if(header != null){
						File target = null;
						Object o = null;
						switch(header[0]){
						case "FILE":
							System.out.println("파일 수신 시작");
							target = getFile(header[1], Long.parseLong(header[2]));
							System.out.println("파일 수신 완료");
							break;
						case "OBJECT":
							System.out.println("오브젝트("+header[1]+") 수신 시작");
							o = getObject(Integer.parseInt(header[2]));
							System.out.println("오브젝트("+header[1]+") 수신 완료");
							break;
						default:
						}
						if(target != null)
							System.out.println("파일명 : " + target.getName() + " 파일크기: " + target.length());
						if(o != null)
							System.out.println("오브젝트명 : " + header[1] + " 내용: " + ((Friends)o).getFriends());
					}
				} catch (Exception e) {}
			}
		}
	}
	public void sendHeader(String[] header) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("\\");
		for(String arg : header)
			sb.append(arg).append("\\");
		byte[] tmp = sb.toString().getBytes();
		byte[] buffer = new byte[4096];
		for(int i=0; i<buffer.length; i++){
			if(i < tmp.length)
				buffer[i] = tmp[i];
			else
				buffer[i] = ' ';
		}
		out.write(buffer, 0, buffer.length);
		out.flush();
	}
	public void sendFile(File f) throws IOException{
		String[] header = new String[]{"FILE", f.getName(), String.valueOf(f.length())};
		sendHeader(header);
		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f));
		byte[] buffer = new byte[4096];
		long sent = 0;
		while(sent != f.length()){
			if((f.length() - sent) < 4096)
				buffer = new byte[(int)(f.length() - sent)];
			int read = fin.read(buffer);
			if(read == -1) break;
			out.write(buffer, 0, read);
			out.flush();
			sent+=read;
		}
		fin.close();
	}
	public void sendObject(Object o) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(o);
		int length = baos.size();
		String[] header = new String[]{"OBJECT", o.getClass().getSimpleName(), String.valueOf(length)};
		System.out.println(header);
		sendHeader(header);
		ByteArrayInputStream oin = new ByteArrayInputStream(baos.toByteArray(),0,length);
		byte[] buffer = new byte[4096];
		int sent = 0;
		while(sent != length){
			if((length - sent) < 4096)
				buffer = new byte[(length - sent)];
			int read = oin.read(buffer);
			if(read == -1) break;
			out.write(buffer, 0, read);
			out.flush();
			sent+=read;
		}
		oin.close();
	}
	public synchronized String[] getHeader() throws IOException{
		byte[] buffer = new byte[4096];
		int trial = 1;
		for(int i=0; i < buffer.length; i++){
			int read = in.read();
			if(read == -1){
				if(trial <= 10){
					System.out.println("reached the end of stream (trial : " + trial + " )");
					try{Thread.sleep(100);}catch(Exception e){}
					i--; trial++;
				} else {
					System.out.println("stream seems to be closed");
					this.close();
					return null;
				}
			} else {
				trial = 1;
				buffer[i] = (byte) read;
			}
		}
		String tmp = new String(buffer, 0, buffer.length);
		String[] header = tmp.trim().substring(1).split("\\\\");
		System.out.println(tmp);
		return header;
	}
	public File getFile(String name, long length) throws IOException{
		File target = new File("received", name);
		while(target.exists())
			target = new File("received", "(new)" + target.getName());
		BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(target));
		byte[] buffer = new byte[4096];
		long received = 0;
		while(received != length){
			if((length - received) < 4096)
				buffer = new byte[(int)(length - received)];
			int read = in.read(buffer);
			if(read == -1) continue;
			received += read;
			fout.write(buffer, 0, read);
			fout.flush();
		}
		fout.close();
		return target;
	}
	public Object getObject(int length) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int received = 0;
		while(received != length){
			if((length - received) < 4096)
				buffer = new byte[(length - received)];
			int read = in.read(buffer);
			if(read == -1) continue;
			received += read;
			baos.write(buffer, 0, read);
			baos.flush();
		}
		Object o = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
		return o;
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
	public Receiver getReceiver() {
		return receiver;
	}
	@Override
	public void close() throws IOException {
		in.close();
		out.close();
		socket.close();
	}
}
