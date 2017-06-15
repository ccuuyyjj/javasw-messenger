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
import java.io.OutputStream;
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
	/*
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
		try{Thread.sleep(10);}catch(Exception e){}
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
		while(true){
			try{Thread.sleep(10);}catch(Exception e){}
			buffer = new byte[4096];
			in.mark(8192);
			int read = in.read(buffer, 0, 4096);
			if(read == -1){
				in.reset();
				continue;
			}
			else if(new String(buffer, 0, read).trim().equals("\\=[FINISH]=\\"))
				break;
		}
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
		try{Thread.sleep(10);}catch(Exception e){}
		bos.close();
		byte[] fin = "\\=[FINISH]=\\".getBytes();
		byte[] buffer = new byte[4096];
		for(int i = 0; i<fin.length; i++){
			buffer[i] = fin[i];
		}
		out.write(buffer, 0, 4096);
		out.flush();
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
		try{Thread.sleep(10);}catch(Exception e){}
		out.write(baos.toByteArray(), 0, length);
		out.flush();
		
		while(true){
			try{Thread.sleep(10);}catch(Exception e){}
			buffer = new byte[4096];
			in.mark(8192);
			int read = in.read(buffer, 0, 4096);
			if(read == -1){
				in.reset();
				continue;
			} else {
				if(new String(buffer, 0, read).trim().equals("\\=[FINISH]=\\"))
					break;
				else {
					in.reset();
					continue;
				}
			}
		}
		
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
		try{Thread.sleep(10);}catch(Exception e){}
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray())));
		Object o = ois.readObject();
		byte[] fin = "\\=[FINISH]=\\".getBytes();
		byte[] buffer = new byte[4096];
		for(int i = 0; i<fin.length; i++){
			buffer[i] = fin[i];
		}
		out.write(buffer, 0, 4096);
		out.flush();
		System.out.println("getObject End");
		return o;
	}
	*/
	
	public void sendHeader(String[] header) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("\\");
		for(String arg : header)
			sb.append(arg).append("\\");
		byte[] buffer = sb.toString().trim().getBytes();
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
	public String[] getHeader() throws IOException{
		byte[] buffer = new byte[4096];
		int size = in.read(buffer, 0, buffer.length);
		String[] header = new String(buffer, 0, size).substring(1).split("\\\\");
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
			if(read == -1) break;
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
			if(read == -1) break;
			received += read;
			baos.write(buffer, 0, read);
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
}
