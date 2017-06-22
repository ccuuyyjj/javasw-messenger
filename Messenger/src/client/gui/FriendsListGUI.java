package client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import client.Client;
import general.container.Broadcast;
import general.container.Message;

public class FriendsListGUI extends JFrame {

	Broadcast bc = new Broadcast();
	// �� ����â
	private String id = Client.identity;// ��� �󺧿� ǥ�õ� �ڱ� ���̵�
	private int allfriend; // ��������+�¶��� ģ��
	private int connecting; // �¶���ģ��
	private JLabel ss = new JLabel(
			"<html>�̸� : " + id + "<br>��ü ģ�� : " + allfriend + "��" + "<br>�������� ģ�� : " + connecting + "��" + "</html>");

	private DefaultMutableTreeNode list = new DefaultMutableTreeNode("ȸ�� ���");
	private JTree tree = new JTree(list) {
		@Override
		public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			String id = null;
			String nick = null;
			if (value != null && value.getClass().getSimpleName().equals("DefaultMutableTreeNode"))
				id = ((DefaultMutableTreeNode) value).toString();
			if (id != null)
				nick = Client.friends.getFriendsList().get(id);
			if (nick != null)
				return nick;
			return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}

	};
	private DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("������");
	// private DefaultMutableTreeNode offline = new
	// DefaultMutableTreeNode("��������");

	// �˾�
	private JPopupMenu pop = new JPopupMenu(); // Ʈ�� ��Ʈ���� ��Ŭ���� ��Ÿ�� �˾��޴�
	private JMenuItem start = new JMenuItem("��ȭ����");
	private JMenuItem end = new JMenuItem("ģ������");

	// �޼��� ����Ȯ�� �˾�
	private JPopupMenu msgpop = new JPopupMenu();
	private JMenuItem yes = new JMenuItem("��ȭ����");
	private JMenuItem no = new JMenuItem("���");

	private JLabel sname = new JLabel("�̸�"); // Ʈ�� ��� Ŭ���� �Ʒ��� ��ȭ�� ����� ����
	private JTextField snames = new JTextField();
	private JLabel snick = new JLabel("�г���");
	private JTextField snicks = new JTextField(); // Ʈ�� ��� Ŭ���� �Ʒ��� ��ȭ�� ����� ����

	private JScrollPane scroll = new JScrollPane(tree); // Ʈ�� ��� ��ũ��

	private JButton logout = new JButton("�α׾ƿ�"); // �α׾ƿ� ��ư
	private JButton addfriend = new JButton("ģ�� �߰�");

	Container con = super.getContentPane();

	// �ɽ���â
	private JLabel label = new JLabel("�ɽ��� â");
	private JTextArea quiz = new JTextArea("?�� �Է��Ͻø� ������ �� �� �ֽ��ϴ�.");
	private JTextField answer = new JTextField();
	private JScrollPane quizscroll = new JScrollPane(quiz);

	private void display() {

		con.setLayout(null);
		con.add(ss, BorderLayout.NORTH);
		con.add(scroll, BorderLayout.CENTER);
		con.add(logout, BorderLayout.SOUTH);
		con.add(sname, BorderLayout.CENTER);
		con.add(snames, BorderLayout.CENTER);
		con.add(snick, BorderLayout.CENTER);
		con.add(snicks, BorderLayout.CENTER);
		con.add(addfriend, BorderLayout.NORTH);
		con.add(quiz, BorderLayout.CENTER);
		con.add(answer, BorderLayout.CENTER);
		con.add(quizscroll, BorderLayout.CENTER);
		con.add(label, BorderLayout.CENTER);

		// �ֻ�� �α��� ����â
		Border b2 = BorderFactory.createTitledBorder("�� �α��� ����");
		Border b3 = BorderFactory.createTitledBorder("");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);

		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);

		// �¶��ΰ� �������� ����
		list.add(online);
		// list.add(offline);

		// // Ʈ��â���� ��� Ŭ���� �ش� �г��ӿ� ���Ե� �̸��� �����Ǹ� ǥ�����ִ� â
		// sname.setBounds(12, 368, 57, 28);
		// snames.setBounds(81, 372, 116, 21);
		// snick.setBounds(12, 402, 57, 28);
		// snicks.setBounds(81, 403, 116, 21);
		//
		// snames.setEditable(false); // ��� Ŭ���� ��Ÿ���� ��� ����â�� �ؽ�Ʈ �ʵ��̹Ƿ�
		// snicks.setEditable(false); // ���� ���ϰ� ������Ű��
		quiz.setEditable(true);// ����â�� ��������

		pop.add(start); // �˾��޴�
		pop.add(end);
		add(pop);

		msgpop.add(yes);
		msgpop.add(no);
		add(msgpop);

		// �α׾ƿ� ��ư
		logout.setBounds(12, 636, 370, 35);
		// ģ�� �߰� ��ư
		addfriend.setBounds(213, 30, 158, 52);
		// �ɽ��� â
		label.setBounds(12, 365, 370, 29);
		label.setFont(new Font("���� ���", Font.BOLD, 12));
		label.setBorder(b3);

		quiz.setBounds(12, 400, 370, 180);
		answer.setBounds(12, 591, 370, 35);

		quizscroll.setBounds(12, 400, 370, 180);
		quizscroll.setViewportView(quiz);

		DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
		render.setOpenIcon(new ImageIcon("image/����.jpg"));
		render.setLeafIcon(new ImageIcon("image/���뺸��.jpg"));
		render.setClosedIcon(new ImageIcon("image/����.jpg"));

		tree.setCellRenderer(render);

	}

	DefaultMutableTreeNode node;// ��� Ŭ���� �ش� ����� �̸��� �˷��ִ� �ڵ�

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					System.out.println("���ΰ�ħ");
					save();
					load();
				}
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (e.getButton() == 1) {
					if (node == null)
						return;
				}

				// for (String id : Client.friends.getFriendsList().keySet()) {
				// // ���
				// // Ŭ����
				// String nodes = node.toString(); // ����� ������
				// if (nodes.contains(id)) { // �г���
				// // �ִ� ����� ���Ͽ� ���� ���� �ִٸ� �ش� �ּҰ��� �ش��ϴ� �̸���
				// // ���̵� ���
				// snames.setText(id);
				// snicks.setText(Client.friends.getFriendsList().get(id));
				// break;
				// }else{
				// snames.setText(null);
				// snicks.setText(null);
				// }
				// }

				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				// Ʈ�� ��� ���ex)[ȸ��,���,������,�г���2]
				if (e.getButton() == 3) { // ���콺 ��Ŭ���� �ϸ�
					int iRow = tree.getRowForLocation(e.getX(), e.getY()); // �ش�
					// ���콺
					// ��ǥ
					// ��ġ
					tree.setSelectionRow(iRow); // Ʈ�� ��带 ��Ŭ��

					if (path != null) // Ʈ�� ��ΰ� ���� �ƴ϶��(���� �ִٸ�)
						for (String id : Client.friends.getFriendsList().keySet()) {
							if (node.toString().contains(id)) {
								pop.show(tree, e.getX(), e.getY()); // �˾�
								// �޴�â
								// ���
							}
						}
				}
			}
		});

		addfriend.addActionListener(e -> {
			String listname = JOptionPane.showInputDialog("ģ���� ���̵� �Է��ϼ���.");
			for (String id : Client.friends.getFriendsList().keySet()) {
				if (listname.equals(id)) {
					listname = null;
					break;
				}
			}
			if (listname == null || listname.isEmpty()) {
				JOptionPane.showMessageDialog(null, "�Է��� �ùٸ��� �ʽ��ϴ�.");
			} else if (Client.identity.equals(listname)) {
				JOptionPane.showMessageDialog(null, "�ڱ��ڽ��� �߰��� �� �����ϴ�.");
			} else {
				// System.out.println("���°�" + listname);
				String nickname = JOptionPane.showInputDialog("ģ���� �̸��� �����ϼ���");
				if (nickname == null | nickname.isEmpty()) {
					nickname = listname;
				}
				Client.friends.add(listname, nickname);
			}
			save();
		});
		start.addActionListener(e -> {// ä�ù�

			ChatRoomGUI room = Client.chatList.get(node.toString());
			if (room == null) {
				room = new ChatRoomGUI(node.toString());
				Client.chatList.put(node.toString(), room);
			}
			room.setVisible(true);
		});
		logout.addActionListener(e -> {// �α׾ƿ�
			Client.currentMainGUI = new LoginGUI();
			dispose();
		});
		end.addActionListener(e -> {// ģ������
			Client.friends.getFriendsList().remove(node.toString());
			save();
		});
		answer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String key = answer.getText();
					answer.setText("");
					bc.caster(key);
				}
			}
		});

	}

	private void menu() {
	}

	@Override
	public void dispose() {
		if (Client.conn != null && !Client.conn.getSocket().isClosed()) {
			Client.receiver.setRunning(false);
			Client.friends.getFriendsList().clear();
			Client.friends = null;
			Client.identity = null;
			for (ChatRoomGUI gui : Client.chatList.values()) {
				gui.dispose();
			}
			Client.chatList = null;
			Client.conn.close();
			Client.conn = null;
			Client.receiver = null;
		}
		super.dispose();
	}

	public FriendsListGUI() {
		super.setTitle("ģ�����");
		super.setSize(400, 710);
		// �ü���� â ��ġ ���Ŀ� ���� ��ġ
		super.setLocationByPlatform(true);
		super.setResizable(false);
		load();
		display();
		event();
		menu();
		count();
		super.setVisible(true);
		Client.receiver.start();
		BcReceiver br = new BcReceiver();
		br.setDaemon(true);
		br.start();
		bc.setDaemon(true);
		bc.start();
	}

	private void count() {
		int i = online.getChildCount();
		// i+=offline.getChildCount();
		allfriend = i;
		ss.setText("<html>�̸� : " + id + "<br>��ü ģ�� : " + allfriend + "��" + "<br>�������� ģ�� : " + connecting + "��"
				+ "</html>");
	}

	private void save() {
		try {
			Message msg = new Message(Client.identity, "=[SERVER]=", Client.friends);
			Client.conn.sendObject(msg);
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void load() {
		online.removeAllChildren();
		// offline.removeAllChildren();
		for (String id : Client.friends.getFriendsList().keySet()) {
			online.add(new DefaultMutableTreeNode(id + "(" + Client.friends.getFriendsList().get(id) + ")"));

		}
	}

	public DefaultTreeModel getModel() {
		return model;
	}

	public void msgcheck() {
		msgpop.show(this, 10, 10);
	}

	private void country() {
		Map<Integer, String> q = new HashMap<Integer, String>();
		q.put(0, "������ ������");
		q.put(1, "�̱��� ������");
		q.put(2, "�߱��� ������");
		q.put(3, "�ѱ��� ������");
		q.put(4, "�Ϻ��� ������");
		q.put(5, "������� ������");
		q.put(6, "�츮����");
		List<String> a = new ArrayList<>();
		a.add(0, "����");
		a.add(1, "������");
		a.add(2, "����¡");
		a.add(3, "����");
		a.add(4, "����");
		a.add(5, "���������");
		a.add(6, "��õ");
		int count = 0;
		int i = 0;
		while (true) {
			int num = (int) (Math.random() * a.size());
			quiz.append(q.get(num) + "?\n");
			String an = JOptionPane.showInputDialog(q.get(num));
			count++;
			if (a.get(num).toString().equals(an)) {
				quiz.append("�����Դϴ�.\n\n");
				i++;
			} else if (an.equals("����")) {
				count--;
				quiz.append("\n" + "===���� ���� ���===" + "\n" + "�� ���� �� : " + count + "��\n���� ���� �� : " + i + "��\nƲ�� ���� �� : "
						+ (count - i) + "��\n=================\n");
				break;
			} else {
				quiz.append("�����Դϴ�." + q.get(num) + " " + a.get(num) + "�Դϴ�.\n");
			}

		}

	}

	private void history() {

	}

	private void plus() {

	}

	private class BcReceiver extends Thread {
		@Override
		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(20000);
			} catch (SocketException e) {
				e.printStackTrace();
			}

			byte[] buffer = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

			while (true) {
				try {
					ds.receive(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String s = new String(dp.getData(), 0, dp.getLength());
				quiz.append(s+"\n");
			}
		}
	}
}
