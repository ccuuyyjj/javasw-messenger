package client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.impl.LoginImpl;

public class LoginGUI extends JFrame {

	private ImageIcon img = new ImageIcon("projectImage.png");
	private JLabel logo = new JLabel(img);

	private JTextField id = new JTextField("aaa"); // ("아이디 (전화번호)");
	private JPasswordField pw = new JPasswordField("aaaaaa"); // ("비밀번호");
	private JTextField address = new JTextField("192.168.0.10"); // ("서버 주소");

	private JLabel info = new JLabel("아이디 : 2-10자, 비밀번호 : 6-20자 (특수문자 제외)", JLabel.RIGHT);

	private JButton login = new JButton("로그인");
	private JButton join = new JButton("회원가입");

	private void display() {
		Container con = super.getContentPane();
		con.setLayout(null);
		// con.setBackground(Color.lightGray);

		logo.setBounds(20, 20, 355, 230);
		logo.setOpaque(true);

		Font font = new Font("굴림", Font.PLAIN, 15);

		id.setBounds(23, 270, 350, 30);
		id.setFont(font);
		id.setForeground(Color.gray);

		pw.setBounds(23, 315, 350, 30);
		pw.setFont(font);
		pw.setForeground(Color.gray);

		address.setBounds(23, 360, 350, 30);
		address.setFont(font);
		address.setForeground(Color.gray);

		info.setBounds(23, 395, 350, 20);
		info.setOpaque(true);

		login.setBounds(295, 420, 90, 30);
		login.setBackground(Color.gray);
		login.setForeground(Color.white);
		login.setFont(font);

		join.setBounds(200, 420, 90, 30);
		join.setBackground(Color.gray);
		join.setForeground(Color.white);
		join.setFont(font);

		con.add(logo);
		con.add(id);
		con.add(pw);
		con.add(address);
		con.add(info);
		con.add(login);
		con.add(join);

		login.requestFocusInWindow(); // 초기 포커스 로그인 버튼으로 설정
	}

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// placeholder 설정
		id.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if (id.getText().equals("아이디 (전화번호)")) {
					id.setText("");
					id.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (id.getText().isEmpty()) {
					id.setForeground(Color.GRAY);
					id.setText("아이디 (전화번호)");
				}
			}
		});

		pw.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if (pw.getText().equals("비밀번호")) {
					pw.setText("");
					pw.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (pw.getText().isEmpty()) {
					pw.setForeground(Color.GRAY);
					pw.setText("비밀번호");
				}
			}
		});

		address.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if (address.getText().equals("서버 주소")) {
					address.setText("");
					address.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (address.getText().isEmpty()) {
					address.setForeground(Color.GRAY);
					address.setText("서버 주소");
				}
			}
		});

		join.addActionListener(e -> {
			boolean check = false;//LoginImpl.join(id.getText(), pw.getText(), address.getText());

			if (check)
				JOptionPane.showMessageDialog(this, "가입 완료되었습니다");
			else
				JOptionPane.showMessageDialog(this, "이미 존재하는 아이디이거나 형식에 맞지 않습니다");
		});

		login.addActionListener(e -> {
			Boolean check = false;//JoinNLogin.login(id.getText(), pw.getText(), address.getText());

			if (check) {
				JOptionPane.showMessageDialog(this, "로그인에 성공했습니다");

			} else
				JOptionPane.showMessageDialog(this, "일치하는 정보가 없습니다");
		});
	}

	private void menu() {
	}

	public LoginGUI() {
		super.setTitle("메신저");
		super.setSize(400, 500);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		super.setVisible(true);
		display();
		event();
		menu();
		// try {
		// test.server.main(null);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

}
