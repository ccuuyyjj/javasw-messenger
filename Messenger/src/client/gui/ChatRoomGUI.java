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
	private JButton upload = new JButton("���� �ø���");
	// private JButton insert = new JButton("��ȭ �Է�"); //Enter�� ���� ��ü
	private JButton chatexit = new JButton("��ȭ ����");
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	private Receiver receiver = new Receiver();
	// list -> �������ǰ� ��������

	private Message msg;

	private String myid = Client.conn.getIdentity(); // �� ���̵�
	private String[] youid = { "��" }; // �������� ���̵�
	private FileDialog FD;

	private void display() {
		Container con = super.getContentPane();
		con.setLayout(null);
		panel.setLayout(null);

		scroll.setBounds(10, 10, 420, 500);
		scroll.setViewportView(textarea);// �ڵ���ũ�� �̵�

		textarea.setLineWrap(true);// �ڵ��ٹٲ�
		textarea.setEditable(false);// ä�ó��� ����Ұ�

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
			// Enter �Է½� ���۽���
			public void keyTyped(KeyEvent e) {
				// �ؽ�Ʈ �����ڵ�
				if (e.getKeyChar() == 10) {
					for (String id : youid) {
						msg = new Message(myid, id, textfield.getText());
						textarea.append("�������: " + myid + "\n ����: " + textfield.getText());
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
			FD = new FileDialog(this, "���ε� ����", FileDialog.LOAD);
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
