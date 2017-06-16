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

	private JTextField id = new JTextField("aaa"); // ("���̵� (��ȭ��ȣ)");
	private JPasswordField pw = new JPasswordField("aaaaaa"); // ("��й�ȣ");
	private JTextField address = new JTextField("192.168.0.10"); // ("���� �ּ�");

	private JLabel info = new JLabel("���̵� : 2-10��, ��й�ȣ : 6-20�� (Ư������ ����)", JLabel.RIGHT);

	private JButton login = new JButton("�α���");
	private JButton join = new JButton("ȸ������");

	private void display() {
		Container con = super.getContentPane();
		con.setLayout(null);
		// con.setBackground(Color.lightGray);

		logo.setBounds(20, 20, 355, 230);
		logo.setOpaque(true);

		Font font = new Font("����", Font.PLAIN, 15);

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

		login.requestFocusInWindow(); // �ʱ� ��Ŀ�� �α��� ��ư���� ����
	}

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// placeholder ����
		id.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if (id.getText().equals("���̵� (��ȭ��ȣ)")) {
					id.setText("");
					id.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (id.getText().isEmpty()) {
					id.setForeground(Color.GRAY);
					id.setText("���̵� (��ȭ��ȣ)");
				}
			}
		});

		pw.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if (pw.getText().equals("��й�ȣ")) {
					pw.setText("");
					pw.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (pw.getText().isEmpty()) {
					pw.setForeground(Color.GRAY);
					pw.setText("��й�ȣ");
				}
			}
		});

		address.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
				if (address.getText().equals("���� �ּ�")) {
					address.setText("");
					address.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (address.getText().isEmpty()) {
					address.setForeground(Color.GRAY);
					address.setText("���� �ּ�");
				}
			}
		});

		join.addActionListener(e -> {
			boolean check = false;//LoginImpl.join(id.getText(), pw.getText(), address.getText());

			if (check)
				JOptionPane.showMessageDialog(this, "���� �Ϸ�Ǿ����ϴ�");
			else
				JOptionPane.showMessageDialog(this, "�̹� �����ϴ� ���̵��̰ų� ���Ŀ� ���� �ʽ��ϴ�");
		});

		login.addActionListener(e -> {
			Boolean check = false;//JoinNLogin.login(id.getText(), pw.getText(), address.getText());

			if (check) {
				JOptionPane.showMessageDialog(this, "�α��ο� �����߽��ϴ�");

			} else
				JOptionPane.showMessageDialog(this, "��ġ�ϴ� ������ �����ϴ�");
		});
	}

	private void menu() {
	}

	public LoginGUI() {
		super.setTitle("�޽���");
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
