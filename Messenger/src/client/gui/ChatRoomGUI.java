package client.gui;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.Client;
import general.container.Message;

public class ChatRoomGUI extends JFrame {

	private JTextArea textarea = new JTextArea();
	private JTextArea textfield = new JTextArea();
	private JButton upload = new JButton("파일 올리기");
	// private JButton insert = new JButton("대화 입력"); //Enter로 전송 교체
	private JButton chatexit = new JButton("대화 종료");
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	// private Receiver receiver = new Receiver();
	// list -> 내아이피값 가져오기

	private String myid = Client.identity; // 내 아이디
	private String youid = null; // 상대방 아이디
	private FileDialog fd;

	private void display() {
		Container con = super.getContentPane();
		con.setLayout(null);
		panel.setLayout(null);

		scroll.setBounds(10, 10, 420, 500);
		scroll.setViewportView(textarea);// 자동스크롤 이동

		textarea.setLineWrap(true);// 자동줄바꿈
		textarea.setEditable(false);// 채팅내용 변경불가

		textfield.setLineWrap(true);
		panel.add(scroll);
		upload.setBounds(10, 520, 420, 30);
		textfield.setBounds(10, 560, 420, 60);
		// insert.setBounds(10, 595, 420, 30);
		chatexit.setBounds(10, 630, 420, 30);

		add(panel);
		con.add(scroll);
		con.add(upload);
		con.add(textfield);
		// con.add(insert);
		con.add(chatexit);

	}

	private void event() {

		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		chatexit.addActionListener(e -> {
			this.dispose();
			// receiver.destroy();
		});
		textfield.addKeyListener(new KeyListener() {
			// Enter 입력시 전송실행
			public void keyTyped(KeyEvent e) {
				// 텍스트 전송코드
				if (e.getKeyChar() == 10) {
					Message msg = new Message(myid, youid, textfield.getText());
					textarea.append("보낸사람: " + myid + "\n 내용: " + textfield.getText());
					textfield.setText("");
					try {
						Client.conn.sendObject(msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 65535) {
					textfield.append("\n");
				}
			}
		});
		upload.addActionListener(e -> {
			fd = new FileDialog(this, "업로드 파일", FileDialog.LOAD);
			fd.setVisible(true);
			File upfile = new File(fd.getDirectory(), fd.getFile());
			
			Message msg = new Message(myid, youid, upfile);
//			Message msg2 = new Message(myid, youid, "파일을 받으시겠습니까? 예/아니요");
			try {
				Client.conn.sendObject(msg);
				Client.conn.sendFile(upfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		//임시폴더에 파일 저장 , 파일 저장 위치 지정
		

	}

	private void menu() {

	}

	@Override
	public void dispose() {
		Client.chatList.remove(youid);
		super.dispose();
	}

	public ChatRoomGUI(String youid) {
		this.youid = youid;
		String younick = Client.friends.getFriendsList().get(youid);
		super.setTitle(younick + "님과의 채팅");
		super.setSize(450, 700);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		display();
		event();
		menu();
		super.setVisible(false);

		// receiver.setDaemon(true);
		// receiver.start();
	}

	public void messageHandler(Message msg) {
		System.out.println(Client.identity + "가 받음 : " + msg.getMsg().getClass().getSimpleName());
		switch (msg.getMsg().getClass().getSimpleName()) {
		case "String":
			textarea.append("보낸사람: " + msg.getSender() + "\n 내용: " + (String) msg.getMsg());
			break;
		case "File":
			File received = (File) msg.getMsg();
			textarea.append("보낸사람: " + msg.getSender() + "\n 받은 파일: " + received.getName());
			break;
		default:
			System.out.println("default");
		}
	}
}
