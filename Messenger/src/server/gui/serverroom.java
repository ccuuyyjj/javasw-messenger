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
	private JButton upload = new JButton("파일 올리기");
	// private JButton insert = new JButton("대화 입력");
	private JButton chatexit = new JButton("대화 종료");
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
		// 서버 준비
		ServerSocket server = new ServerSocket(20000);
		// 사용자 목록
		List<Connection> userlist = new ArrayList<>();
		Map<String, String> join = new HashMap<>();
		String msg = null;
		// Listening(반복)
		while (true) {
			Thread.sleep(1500);
			System.out.println("서버가 구동중입니다");
			Socket socket = server.accept();
			// 사용자가 접속했다면 이 정보를 어딘가에 저장
			userlist.add(new Connection(socket));
			textarea.append("사용자 접속:" + userlist.size() + "명 있음 IP주소: " + socket.getInetAddress()+"\n");
			InputStreamReader in = new InputStreamReader(socket.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			if (buffer != null)
				msg = buffer.readLine();
			StringTokenizer st = new StringTokenizer(msg, " ");
			this.id = st.nextToken();
			this.pw = st.nextToken();
			this.login = st.nextToken();
			textarea.append("ID:"+this.id+"\nPW:"+this.pw+"\n접속 1or0:"+this.login);
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
