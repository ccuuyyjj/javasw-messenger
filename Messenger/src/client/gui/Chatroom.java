package client.gui;

import java.awt.Container;
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
import general.container.Message;

class JFrameChatroom extends JFrame {

	private JTextArea textarea = new JTextArea();
	private JTextArea textfield = new JTextArea();
	private JButton upload = new JButton("파일 올리기");
	// private JButton insert = new JButton("대화 입력");
	private JButton chatexit = new JButton("대화 종료");
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane();

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
		});
		textfield.addKeyListener(new KeyListener() {
			// Enter 입력시 전송실행
			public void keyTyped(KeyEvent e) {

				if (e.getKeyChar() == 10) {
					Message msg = new Message("내 아이디", "받을사람", textfield.getText());
					try {
						Client.conn.sendObject(msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					textfield.setText("");
					// 텍스트 전송코드 입력 공간
					
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				String keyCode = Integer.toString(e.getKeyChar());
				System.out.println(keyCode);
				if (e.getKeyChar() == 65535) {
					textfield.append("\n");
				}
			}
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

	}
}

public class Chatroom {
	public static void main(String[] args) {
		JFrameChatroom window = new JFrameChatroom();

	}
}
