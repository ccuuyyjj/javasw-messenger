package client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import client.impl.LoginImpl;

public class LoginGUI extends JFrame {

	private ImageIcon img = new ImageIcon("projectImage.png");
	private JLabel logo = new JLabel(img);

	private JLabel lbId = new JLabel("아  이  디 : ");
	private JTextField id = new JTextField("aaa"); // 아이디 (전화번호)
	private JLabel lbPw = new JLabel("비밀번호 : ");
	private JPasswordField pw = new JPasswordField("aaaaaa"); // 비밀번호
	private JLabel lbAddr = new JLabel("서버주소 : ");
	private JTextField address = new JTextField("warrock.iptime.org"); // 서버 주소

	private JLabel info = new JLabel("아이디 : 2-10자, 비밀번호 : 6-20자 (특수문자 제외)", JLabel.RIGHT);

	private JButton login = new JButton("로그인");
	private JButton join = new JButton("회원가입");

	private void display() {
		super.setTitle("메신저");
		super.setSize(400, 500);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		super.setVisible(true);
		
		Container con = super.getContentPane();
		con.setLayout(null);
		// con.setBackground(Color.lightGray);

		logo.setBounds(20, 20, 355, 230);
		logo.setOpaque(true);

		Font font = new Font("굴림", Font.PLAIN, 15);
		
//		아이디
		lbId.setBounds(23, 270, 70, 30);

		id.setBounds(90, 270, 285, 30);
		id.setFont(font);
		
//		비밀번호
		lbPw.setBounds(23, 315, 70, 30);

		pw.setBounds(90, 315, 285, 30);
		pw.setFont(font);
		
//		서버주소
		lbAddr.setBounds(23, 360, 70, 30);
		
		address.setBounds(90, 360, 285, 30);
		address.setFont(font);

		info.setBounds(23, 395, 350, 20);
		info.setOpaque(true);

		login.setBounds(285, 420, 90, 30);
		login.setBackground(Color.gray);
		login.setForeground(Color.white);
		login.setFont(font);

		join.setBounds(190, 420, 90, 30);
		join.setBackground(Color.gray);
		join.setForeground(Color.white);
		join.setFont(font);

		con.add(logo);
		con.add(lbId);
		con.add(id);
		con.add(lbPw);
		con.add(pw);
		con.add(lbAddr);
		con.add(address);
		con.add(info);
		con.add(login);
		con.add(join);

		login.requestFocusInWindow(); // 초기 포커스 로그인 버튼으로 설정
	}

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// placeholder 설정 제거, 그냥 라벨로 앞에 써줌

		join.addActionListener(e -> {
			try {
				boolean check = LoginImpl.join(id.getText(), pw.getText(), address.getText());

				if (check) {
					JOptionPane.showMessageDialog(this, "가입 완료되었습니다");
					pw.setText("");
				}
				else {
					JOptionPane.showMessageDialog(this, "이미 존재하는 아이디이거나 형식에 맞지 않습니다");
					pw.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		login.addActionListener(e -> {
			try {
				Boolean check = LoginImpl.login(id.getText(), pw.getText(), address.getText());

				if (check) {
					JOptionPane.showMessageDialog(this, "로그인에 성공했습니다");
					
					Client.currentGUI = new JFrameList();
					dispose();
				} else
					JOptionPane.showMessageDialog(this, "일치하는 정보가 없습니다");
					pw.setText("");
				}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private void menu() {
	}

	public LoginGUI() {
		
		display();
		event();
		menu();
	}

}
