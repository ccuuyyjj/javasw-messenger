package client.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import client.Client;
import general.container.Connection;
import general.container.Friends;
import general.container.LoginInfo;
import general.container.Message;

public class ClientUtil {
	
	public final static int JOIN = 0;
	public final static int LOGIN = 1;
	
	public static boolean joinNlogin(String id, String pw, String addr, int flag) {
		boolean result = false;
			try {
				if(Client.conn == null){
					Socket socket = new Socket(InetAddress.getByName(addr), 20000);
					Client.conn = new Connection(socket);
				}
				LoginInfo info = new LoginInfo(id, pw, flag);
				Message sendmsg = new Message(info);
				Client.conn.sendObject(sendmsg);
				String[] header = Client.conn.getHeader();
				if(header[0].equals("OBJECT") && header[1].equals("Boolean")){
					Boolean recvmsg = (Boolean) Client.conn.getObject(Integer.parseInt(header[2]));
					if(recvmsg){
						result = true;
//						������� true�� ��� ģ�� ��� �޾ƿ���
						while(true){
							String[] fheader = Client.conn.getHeader();
							if(fheader != null){
								if(fheader[0].equals("OBJECT") && fheader[1].equals("Friends"))
									Client.friends = (Friends) Client.conn.getObject(Integer.parseInt(fheader[2]));
								System.out.println(Client.friends.getListname().toString());
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return result;
	} // ȸ������/�α��� ��ư�� ������ Message���·� LoginInfo(id, pw, flag)�� ������ ������ �����(t/f)�� �޾ƿ�
}
