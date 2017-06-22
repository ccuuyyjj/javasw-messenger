package client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import client.Client;
import client.impl.ClientUtil;

public class LoginGUI extends JFrame {

	private ImageIcon img = new ImageIcon("image/normcore.png");
	private JLabel logo = new JLabel(img);

	private JLabel lbId = new JLabel("IDENTITY��   : ");
	private JTextField id = new JTextField("aaa"); // ���̵� (��ȭ��ȣ)
	private JLabel lbPw = new JLabel("PASSWORD : ");
	private JPasswordField pw = new JPasswordField("aaaaaa"); // ��й�ȣ
	private JLabel lbAddr = new JLabel("ADDRESS : ");

	private String ip = "192.168.0.16";
	private JTextField address = new JTextField(ip); // ���� �ּ�
	
	private JLabel info = new JLabel("ID : 2-10�� PASSWORD : 6-20�� (Ư������ ����)", JLabel.RIGHT);

	private JButton login = new JButton("LOGIN");
	private JButton join = new JButton("JOIN");

	private void display() {
		super.setTitle("NORMCORE TALK");
		super.setSize(400, 500);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		super.setVisible(true);

		Container con = super.getContentPane();
		con.setLayout(null);
		con.setBackground(Color.white);

		logo.setBounds(20, 20, 355, 230);
		logo.setOpaque(true);

		Font font = new Font("", Font.PLAIN, 15);
		Font font2 = new Font("", Font.BOLD, 15);

		// ���̵�
		lbId.setBounds(20, 260, 90, 30);

		id.setBounds(105, 260, 270, 30);
		id.setFont(font);

		��ó: http://luvstudy.tistory.com/37 [�Ķ��ϴ��� ����â��]

		// ��й�ȣ
		lbPw.setBounds(20, 305, 90, 30);

		pw.setBounds(105, 305, 270, 30);
		pw.setFont(font);

		// �����ּ�
		lbAddr.setBounds(20, 350, 90, 30);

		address.setBounds(105, 350, 270, 30);
		address.setFont(font);

		info.setBounds(23, 390, 350, 20);
		info.setOpaque(true);
		info.setBackground(Color.white);

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
	}

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// placeholder ���� ����, �׳� �󺧷� �տ� ����

		join.addActionListener(e -> {
			if (regex(id.getText(), pw.getText())) {
				try {
					boolean check = ClientUtil.joinNlogin(id.getText(), pw.getText(), address.getText(),
							ClientUtil.JOIN);

					if (check) {
						JOptionPane.showMessageDialog(this, "���� �Ϸ�Ǿ����ϴ�");

						File target = new File("files", "info.prop");
						if (!target.exists()) {
							target.getParentFile().mkdirs();
							target.createNewFile();
						}
						FileOutputStream out = new FileOutputStream(target);
						Properties prop = new Properties();
						prop.setProperty("id", id.getText());
						prop.setProperty("pw", pw.getText());
						prop.setProperty("addr", address.getText());
						prop.store(out, "information");

						Client.identity = id.getText();
						this.dispose();
						Client.currentMainGUI = new FriendsListGUI();
					} else {
						JOptionPane.showMessageDialog(this, "�̹� �����ϴ� ���̵��Դϴ�");
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
				JOptionPane.showMessageDialog(this, "���Ŀ� ���� �ʽ��ϴ�");
		});

		login.addActionListener(e -> {
			try {
				boolean check = ClientUtil.joinNlogin(id.getText(), pw.getText(), address.getText(),
						ClientUtil.LOGIN);

				if (check) {
					JOptionPane.showMessageDialog(this, "�α��ο� �����߽��ϴ�");

					File target = new File("files", "info.prop");
					if (!target.exists()) {
						target.getParentFile().mkdirs();
						target.createNewFile();
					}
					FileOutputStream out = new FileOutputStream(target);
					Properties prop = new Properties();
					prop.setProperty("id", id.getText());
					prop.setProperty("pw", pw.getText());
					prop.setProperty("addr", address.getText());
					prop.store(out, "information");

					Client.identity = id.getText();
					this.dispose();
					Client.currentMainGUI = new FriendsListGUI();
				} else {
					JOptionPane.showMessageDialog(this, "��ġ�ϴ� ������ �����ϴ�");
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

}
