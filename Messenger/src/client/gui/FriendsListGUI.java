package client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
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
import client.Client.ClientReceiver;
import general.container.Message;

public class FriendsListGUI extends JFrame {

	// �� ����â
	private String id = Client.identity;// ��� �󺧿� ǥ�õ� �ڱ� ���̵�
	private int allfriend; // ��������+�¶��� ģ��
	private int connecting; // �¶���ģ��
	private JLabel ss = new JLabel("<html>�̸� : " + id + "<br>��ü ģ�� : " + allfriend + "��" + "<br>�������� ģ�� : "
			+ connecting + "��" + "</html>");

	private DefaultMutableTreeNode friendlist = new DefaultMutableTreeNode("ȸ�� ���");
	private JTree tree = new JTree(friendlist) {
		@Override
		public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			String id = null;
			String nick = null;
			if (value != null && value.getClass().getSimpleName().equals("DefaultMutableTreeNode"))
				id = ((DefaultMutableTreeNode) value).toString();
			if (id != null && Client.friends.getFriendsList().get(id) != null)
				nick = id + "(" + Client.friends.getFriendsList().get(id) + ")";
			if (nick != null)
				return nick;
			return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}

	};
	private DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("������");

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
	private JLabel label = new JLabel("��ü ��ȭ");
	private JTextArea multichat = new JTextArea();
	private JTextField multichattext = new JTextField();
	private JScrollPane multichatscroll = new JScrollPane(multichat);

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
		// con.add(multichat, BorderLayout.CENTER);
		con.add(multichatscroll, BorderLayout.CENTER);
		con.add(multichattext, BorderLayout.CENTER);
		con.add(label, BorderLayout.CENTER);

		// �ֻ�� �α��� ����â
		Border b2 = BorderFactory.createTitledBorder("�� �α��� ����");
		Border b3 = BorderFactory.createTitledBorder("");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);

		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);

		// �¶��ΰ� �������� ����
		friendlist.add(online);

		multichat.setEditable(false);// ����â�� ��������

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

		DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
		render.setOpenIcon(new ImageIcon("image/sampleicon2.jpg"));
		render.setLeafIcon(new ImageIcon("image/sampleicon1.jpg"));
		render.setClosedIcon(new ImageIcon("image/sampleicon2.jpg"));

		tree.setCellRenderer(render);
		multichatscroll.setBounds(12, 401, 370, 180);
		multichattext.setBounds(12, 591, 370, 35);

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
			dispose();
			Client.currentMainGUI = new LoginGUI();
		});
		end.addActionListener(e -> {// ģ������
			Client.friends.getFriendsList().remove(node.toString());
			save();
		});
		multichattext.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String key = multichattext.getText();
					Message msg = new Message(Client.identity, "=[BROADCAST]=", key);
					multichattext.setText("");
					try {
						Client.conn.sendObject(msg);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
		System.gc();
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
		Client.chatList = new HashMap<>();
		Client.receiver = new ClientReceiver();
		Client.receiver.setRunning(true);
		Client.receiver.start();
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
			online.add(new DefaultMutableTreeNode(id));
		}
	}

	public DefaultTreeModel getModel() {
		return model;
	}

	public void msgcheck() {
		msgpop.show(this, 10, 10);
	}

	public void messageHandler(Message msg) {
		String sender = msg.getSender();
		if (Client.friends.getFriendsList().get(sender) != null)
			sender = Client.friends.getFriendsList().get(sender) + "(" + sender + ")";
		String text = (String) msg.getMsg();
		if (!multichat.getText().isEmpty())
			multichat.append("\n");
		multichat.append(sender + " : " + text);
		JScrollBar scroll = multichatscroll.getVerticalScrollBar();
		scroll.setValue(scroll.getMaximum());
	}
}
