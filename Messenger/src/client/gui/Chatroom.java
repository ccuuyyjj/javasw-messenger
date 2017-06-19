package client.gui;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

import client.Client;
import general.container.Connection;
import general.container.Message;

class JFrameChatroom extends JFrame {

	private JTextArea textarea = new JTextArea();
	private JTextArea textfield = new JTextArea();
	private JButton upload = new JButton("파일 올리기");
	// private JButton insert = new JButton("대화 입력"); //Enter로 전송 교체
	private JButton chatexit = new JButton("대화 종료");
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	private Receiver receiver = new Receiver();
	// list -> 내아이피값 가져오기

	private Message msg;

	private String myid = Client.conn.getIdentity(); // 내 아이디
	private String[] youid = { "너" }; // 접속중인 아이디
	private FileDialog FD;

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
			receiver.destroy();
		});
		textfield.addKeyListener(new KeyListener() {
			// Enter 입력시 전송실행
			public void keyTyped(KeyEvent e) {
				// 텍스트 전송코드
				if (e.getKeyChar() == 10) {
					for (String id : youid) {
						msg = new Message(myid, id, textfield.getText());
						textarea.append("보낸사람: " + myid + "\n 내용: " + textfield.getText());
						textfield.setText("");
						try {
							Client.conn.sendObject(msg);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				String keyCode = Integer.toString(e.getKeyChar());
				if (e.getKeyChar() == 65535) {
					textfield.append("\n");
				}
			}
		});
		upload.addActionListener(e -> {
			FD = new FileDialog(this, "업로드 파일", FileDialog.LOAD);
			FD.setVisible(true);
		});

	}

	private void menu() {

	}

	public JFrameChatroom() {
		super.setTitle("Chatingroom");
		super.setSize(450, 700);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		display();
		event();
		menu();
		super.setVisible(true);

		receiver.setDaemon(true);
		receiver.start();
	}
}

class Receiver extends Thread {
	private Connection conn = Client.conn;

	@Override
	public void run() {
		while (true) {
			try {
				String[] header = conn.getHeader();
				if(header != null){
					if(header[1].equals("Message")){
						String yourmsg = null;
						Message msg = (Message) conn.getObject(Integer.parseInt(header[2])); 
						if (msg.getMsg() != null)
							yourmsg = (String) msg.getMsg();
						System.out.println(yourmsg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
