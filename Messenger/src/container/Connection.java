package container;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
	private Socket socket;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	public Connection(Socket socket) throws IOException{
		this.socket = socket;
		in = new BufferedInputStream(socket.getInputStream());
		out = new BufferedOutputStream(socket.getOutputStream());
	}
	public void sendFile(File f) throws IOException{
		System.out.println("sendFile Start");
		//FILE 타입이라는걸 알리고 파일명은 무엇인지 파일 크기는 얼마나 되는지 알림
		byte[] type = ("\\=[FILE]=\\" + f.getName() + "\\" + f.length() + "\\").getBytes();
		byte[] buffer = new byte[4096];
		for(int i = 0; i<type.length; i++){
			buffer[i] = type[i];
		}
		out.write(buffer, 0, buffer.length);
		out.flush();
		//파일의 내용물을 전송
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f), 4096);
		while(true){
			buffer = new byte[4096];
			int size = bis.read(buffer, 0, buffer.length);
			if(size == -1) break;
			out.write(buffer, 0, size);
			out.flush();
		}
		bis.close();
		System.out.println("sendFile End");
	}
	
	public File getFile(String name, long length) throws IOException{
		System.out.println("getFile Start");
		File tmp = new File("tmp", name);
		if(tmp.exists()) tmp.delete();
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmp), 4096);
		
		long moved = 0;
		while(true){
			byte[] buffer = new byte[4096];
			int size = in.read(buffer, 0, buffer.length);
			moved += size;
			bos.write(buffer, 0, size);
			bos.flush();
			if(moved == length) break;
		}
		
		bos.close();
		System.out.println("getFile End");
		return tmp;
	}
	
	public void sendObject(Object o) throws IOException{
		System.out.println("sendObject Start");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.flush();
		int length = baos.size();
		//Object 타입이라는걸 알리고 원래 클래스가 무엇인지 알림
		byte[] type = ("\\=[OBJECT]=\\" + o.getClass().getSimpleName() + "\\" + length + "\\").getBytes();
		byte[] buffer = new byte[4096];
		for(int i = 0; i<type.length; i++){
			buffer[i] = type[i];
		}
		out.write(buffer, 0, buffer.length);
		out.flush();
		out.write(baos.toByteArray(), 0, length);
		out.flush();
		System.out.println("sendObject End");
	}
	
	public Object getObject(long length) throws IOException, ClassNotFoundException{
		System.out.println("getObject Start");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long moved = 0;
		while(true){
			byte[] buffer = new byte[4096];
			int size = in.read(buffer, 0, buffer.length);
			moved += size;
			baos.write(buffer, 0, size);
			if(moved == length) break;
		}
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray())));
		Object o = ois.readObject();
		System.out.println("getObject End");
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
}
