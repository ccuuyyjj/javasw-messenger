package general.container;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Broadcast extends Thread{

	public void caster(String str) {

		int port = 20000;
		String ip = "192.168.0.255";
		InetAddress inet = null;
		try {
			inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		String striming = str;
		while (true) {
			DatagramPacket sp = new DatagramPacket(striming.getBytes(), striming.getBytes().length, inet, port);

			try {
				ds.send(sp);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
