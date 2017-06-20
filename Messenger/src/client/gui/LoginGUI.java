package client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
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

import client.Client;
import client.impl.ClientUtil;

public class LoginGUI extends JFrame {

	private ImageIcon img = new ImageIcon("projectImage.png");
	private JLabel logo = new JLabel(img);

	private JLabel lbId = new JLabel("��  ��  �� : ");
	private JTextField id = new JTextField("aaa"); // ���̵� (��ȭ��ȣ)
	private JLabel lbPw = new JLabel("��й�ȣ : ");
	private JPasswordField pw = new JPasswordField("aaaaaa"); // ��й�ȣ
	private JLabel lbAddr = new JLabel("�����ּ� : ");
	private JTextField address = new JTextField("warrock.iptime.org"); // ����
													// �ּ�

	private JLabel info = new JLabel("���̵� : 2-10��, ��й�ȣ : 6-20�� (Ư������ ����)", JLabel.RIGHT);

	private JButton login = new JButton("�α���");
	private JButton join = new JButton("ȸ������");

	private void display() {
		super.setTitle("�޽���");
		super.setSize(400, 500);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		super.setVisible(true);

		Container con = super.getContentPane();
		con.setLayout(null);
		// con.setBackground(Color.lightGray);

		logo.setBounds(20, 20, 355, 230);
		logo.setOpaque(true);

		Font font = new Font("����", Font.PLAIN, 15);

		// ���̵�
		lbId.setBounds(23, 270, 70, 30);

		id.setBounds(90, 270, 285, 30);
		id.setFont(font);

		// ��й�ȣ
		lbPw.setBounds(23, 315, 70, 30);

		pw.setBounds(90, 315, 285, 30);
		pw.setFont(font);

		// �����ּ�
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

		login.requestFocusInWindow(); // �ʱ� ��Ŀ�� �α��� ��ư���� ����
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
						FileOutputStream out = new FileOutputStream(target);
						Properties prop = new Properties();
						prop.setProperty("id", id.getText());
						prop.setProperty("pw", pw.getText());
						prop.setProperty("addr", address.getText());
						prop.store(out, "information");

						Client.identity = id.getText();
						this.dispose();
						Client.currentGUI = new JFrameList();
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
					FileOutputStream out = new FileOutputStream(target);
					Properties prop = new Properties();
					prop.setProperty("id", id.getText());
					prop.setProperty("pw", pw.getText());
					prop.setProperty("addr", address.getText());
					prop.store(out, "information");

					Client.identity = id.getText();
					this.dispose();
					Client.currentGUI = new JFrameList();
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
