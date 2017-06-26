package client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.color.ColorSpace;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import client.impl.ClientUtil;

public class LoginGUI extends JFrame {
	private File infoprop = new File("files", "info.prop");
	private Properties prop = null;

	private ImageIcon img = new ImageIcon("image/normcore.png");
	private JLabel logo = new JLabel(img);

	private JLabel lbId = new JLabel("IDENTITY　   : ");
	private JTextField id = new JTextField(""); // 아이디 (전화번호)
	private JLabel lbPw = new JLabel("PASSWORD : ");
	private JPasswordField pw = new JPasswordField(""); // 비밀번호
	private JLabel lbAddr = new JLabel("ADDRESS : ");

	private String ip = "";
	private JTextField address = new JTextField(ip); // 서버 주소

	private JLabel info = new JLabel("ID : 2-10자 PASSWORD : 6-20자 (특수문자 제외)", JLabel.RIGHT);

	private JButton login = new JButton("LOGIN");
	private JButton join = new JButton("JOIN");
	
	private void display() {
		this.setContentPane(new JLabel(new ImageIcon("image/b3.jpg")));
		super.setTitle("NORMCORE TALK");
		super.setSize(400, 500);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		super.setVisible(true);
		
		
		Container con = getContentPane();
		con.setLayout(null);
		con.setBackground(Color.white);
		
		
		logo.setBounds(35, 20, 330, 210);
		logo.setOpaque(true);

		Font font = new Font("", Font.PLAIN, 15);
		Font font2 = new Font("", Font.BOLD, 15);
		
		// 아이디
		lbId.setBounds(20, 260, 90, 30);
		lbId.setForeground(Color.white);
		
		id.setBounds(105, 260, 270, 30);
		id.setFont(font);
		
		// 비밀번호
		lbPw.setBounds(20, 305, 90, 30);
		lbPw.setForeground(Color.white);
		
		pw.setBounds(105, 305, 270, 30);
		pw.setFont(font);

		// 서버주소
		lbAddr.setBounds(20, 350, 90, 30);
		lbAddr.setForeground(Color.white);
		address.setBounds(105, 350, 270, 30);
		address.setFont(font);

		info.setBounds(23, 390, 350, 20);
		info.setOpaque(true);
		info.setForeground(Color.ORANGE);
		info.setOpaque(false);
		
		
		join.setBounds(180, 420, 100, 30);
		join.setBackground(Color.black);
		join.setForeground(Color.white);
		join.setFont(font2);

		login.setBounds(285, 420, 90, 30);
		login.setBackground(Color.black);
		login.setForeground(Color.white);
		login.setFont(font2);

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

		loadProp();
	}

	private void imageUpdate(ImageIcon imageIcon, Container con, int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		
	}

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// placeholder 설정 제거, 그냥 라벨로 앞에 써줌

		join.addActionListener(e -> {
			if (regex(id.getText(), pw.getText())) {
				try {
					boolean check = ClientUtil.joinNlogin(id.getText(), pw.getText(), address.getText(),
							ClientUtil.JOIN);

					if (check) {
						JOptionPane.showMessageDialog(this, "가입 완료되었습니다");

						prop.setProperty("id", id.getText());
						prop.setProperty("pw", pw.getText());
						prop.setProperty("addr", address.getText());
						saveProp();

						Client.identity = id.getText();
						this.dispose();
						Client.currentMainGUI = new FriendsListGUI();
					} else {
						JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다");
						pw.setText("");
						if (Client.conn != null && !Client.conn.getSocket().isClosed()) {
							Client.conn.close();
							Client.conn = null;
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(this, "형식에 맞지 않습니다");
		});

		login.addActionListener(e -> {
			try {
				boolean check = ClientUtil.joinNlogin(id.getText(), pw.getText(), address.getText(),
						ClientUtil.LOGIN);

				if (check) {
					JOptionPane.showMessageDialog(this, "로그인에 성공했습니다");

					prop.setProperty("id", id.getText());
					prop.setProperty("pw", pw.getText());
					prop.setProperty("addr", address.getText());
					saveProp();

					Client.identity = id.getText();
					this.dispose();
					Client.currentMainGUI = new FriendsListGUI();
				} else {
					JOptionPane.showMessageDialog(this, "일치하는 정보가 없습니다");
					pw.setText("");
					if (Client.conn != null && !Client.conn.getSocket().isClosed()) {
						Client.conn.close();
						Client.conn = null;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private void menu() {
	}

	private boolean regex(String id, String pw) {
		String idRegex = "^[a-zA-Z0-9-_]{2,10}$";
		String pwRegex = "^[a-zA-Z0-9-_]{6,20}$";

		if (!Pattern.matches(idRegex, id) || !Pattern.matches(pwRegex, pw))
			return false;
		else
			return true;
	}

	public LoginGUI() {
		display();
		event();
		menu();
		
	}

	private void saveProp() {
		try {
			FileOutputStream out = new FileOutputStream(infoprop);
			prop.store(out, "information");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadProp() {
		if (prop == null)
			prop = new Properties();
		try {
			if (!infoprop.exists()) {
				infoprop.getParentFile().mkdirs();
				infoprop.createNewFile();
			}
			FileInputStream in = new FileInputStream(infoprop);
			prop.load(in);
			id.setText(prop.getProperty("id", "aaa"));
			pw.setText(prop.getProperty("pw", "aaaaaa"));
			address.setText(prop.getProperty("addr", "warrock.iptime.org"));
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
