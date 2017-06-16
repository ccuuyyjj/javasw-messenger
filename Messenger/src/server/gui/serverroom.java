package server.gui;

import java.awt.Container;
import java.awt.Event;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

import general.container.Connection;

class JFrameserverroom extends JFrame {

	private JTextArea textarea = new JTextArea();
	private JTextArea textfield = new JTextArea();
	private JButton upload = new JButton("���� �ø���");
	// private JButton insert = new JButton("��ȭ �Է�");
	private JButton chatexit = new JButton("��ȭ ����");
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane();

	String id;
	String pw;
	String login;
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
		});
		textfield.addKeyListener(new KeyListener() {
			// Enter �Է½� ���۽���
			public void keyTyped(KeyEvent e) {

				if (e.getKeyChar() == 10) {
					textfield.setText("");
					// �ؽ�Ʈ �����ڵ� �Է� ����
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

	public JFrameserverroom() {
		super.setTitle("Serverroom");
		super.setSize(450, 700);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		display();
		event();
		menu();
		super.setVisible(true);
		try {
			serveron();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void serveron() throws Exception {
		// ���� �غ�
		ServerSocket server = new ServerSocket(20000);
		// ����� ���
		List<Connection> userlist = new ArrayList<>();
		Map<String, String> join = new HashMap<>();
		String msg = null;
		// Listening(�ݺ�)
		while (true) {
			Thread.sleep(1500);
			System.out.println("������ �������Դϴ�");
			Socket socket = server.accept();
			// ����ڰ� �����ߴٸ� �� ������ ��򰡿� ����
			userlist.add(new Connection(socket));
			textarea.append("����� ����:" + userlist.size() + "�� ���� IP�ּ�: " + socket.getInetAddress()+"\n");
			InputStreamReader in = new InputStreamReader(socket.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			if (buffer != null)
				msg = buffer.readLine();
			StringTokenizer st = new StringTokenizer(msg, " ");
			this.id = st.nextToken();
			this.pw = st.nextToken();
			this.login = st.nextToken();
			textarea.append("ID:"+this.id+"\nPW:"+this.pw+"\n���� 1or0:"+this.login);
			System.out.println(this.id+" "+this.pw+" "+this.login);
			boolean check = false;//JoinNLogin.login(this.id, this.pw,this.login);
			String check2 = String.valueOf(check);
			System.out.println(check2);
			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
			BufferedWriter buffer2 = new BufferedWriter(out);
			PrintWriter writer = new PrintWriter(buffer2);
			writer.print(check2);
			writer.flush();
		}

	}
}

public class serverroom {
	public static void main(String[] args) throws Exception {
		JFrameserverroom window = new JFrameserverroom();

	}
}
