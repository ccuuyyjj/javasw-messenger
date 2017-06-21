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
	private JButton upload = new JButton("���� �ø���");
	// private JButton insert = new JButton("��ȭ �Է�"); //Enter�� ���� ��ü
	private JButton chatexit = new JButton("��ȭ ����");
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	// private Receiver receiver = new Receiver();
	// list -> �������ǰ� ��������

	private String myid = Client.identity; // �� ���̵�
	private String youid = null; // ���� ���̵�
	private FileDialog fd;

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
			// receiver.destroy();
		});
		textfield.addKeyListener(new KeyListener() {
			// Enter �Է½� ���۽���
			public void keyTyped(KeyEvent e) {
				// �ؽ�Ʈ �����ڵ�
				if (e.getKeyChar() == 10) {
					Message msg = new Message(myid, youid, textfield.getText());
					textarea.append("�������: " + myid + "\n ����: " + textfield.getText());
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
			fd = new FileDialog(this, "���ε� ����", FileDialog.LOAD);
			fd.setVisible(true);
			File upfile = new File(fd.getDirectory(), fd.getFile());
			
			Message msg = new Message(myid, youid, upfile);
//			Message msg2 = new Message(myid, youid, "������ �����ðڽ��ϱ�? ��/�ƴϿ�");
			try {
				Client.conn.sendObject(msg);
				Client.conn.sendFile(upfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		//�ӽ������� ���� ���� , ���� ���� ��ġ ����
		

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
		super.setTitle(younick + "�԰��� ä��");
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
		System.out.println(Client.identity + "�� ���� : " + msg.getMsg().getClass().getSimpleName());
		switch (msg.getMsg().getClass().getSimpleName()) {
		case "String":
			textarea.append("�������: " + msg.getSender() + "\n ����: " + (String) msg.getMsg());
			break;
		case "File":
			File received = (File) msg.getMsg();
			textarea.append("�������: " + msg.getSender() + "\n ���� ����: " + received.getName());
			break;
		default:
			System.out.println("default");
		}
	}
}
