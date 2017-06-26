package client.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import client.Client;
import client.Client.ClientReceiver;
import general.container.Message;

public class FriendsListGUI extends JFrame {

	// �� ����â
	private String id = Client.identity;// ��� �󺧿� ǥ�õ� �ڱ� ���̵�
	private int friendscount; // ��������+�¶��� ģ��
	private int onlinecount; // �¶���ģ��
	private JLabel ss = new JLabel("<html>�̸� : " + id + "<br>��ü ģ�� : " + friendscount + "��" + "<br>�������� ģ�� : "
			+ onlinecount + "��" + "</html>");

	
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
				nick = Client.friends.getFriendsList().get(id) + "(" + id + ")";
			if (nick != null)
				return nick;
			return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}
	};
	private DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	private DefaultMutableTreeNode node;// ��� Ŭ���� �ش� ����� �̸��� �˷��ִ� �ڵ�
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("�¶���");
	private DefaultMutableTreeNode offline = new DefaultMutableTreeNode("��������");

	// �˾�
	private JPopupMenu pop = new JPopupMenu(); // Ʈ�� ��Ʈ���� ��Ŭ���� ��Ÿ�� �˾��޴�
	private JMenuItem start = new JMenuItem("��ȭ����");
	private JMenuItem end = new JMenuItem("ģ������");

	private JLabel sname = new JLabel("�̸�"); // Ʈ�� ��� Ŭ���� �Ʒ��� ��ȭ�� ����� ����
	private JTextField snames = new JTextField();
	private JLabel snick = new JLabel("�г���");
	private JTextField snicks = new JTextField(); // Ʈ�� ��� Ŭ���� �Ʒ��� ��ȭ�� ����� ����

	private JScrollPane scroll = new JScrollPane(tree); // Ʈ�� ��� ��ũ��

	private JButton logout = new JButton("�α׾ƿ�"); // �α׾ƿ� ��ư
	private JButton addfriend = new JButton("ģ�� �߰�");

	//�ý��� Ʈ����
	private SystemTray tray;
	private PopupMenu traypop = new PopupMenu();
	private Image trayimage = Toolkit.getDefaultToolkit().getImage("image/micon.png");
	private TrayIcon trayIcon = new TrayIcon(trayimage,"NCTok",traypop);
	private MenuItem open = new MenuItem("����");
	private MenuItem close = new MenuItem("����");
	
	Font font = new Font("", Font.PLAIN, 15);
	Font font2 = new Font("", Font.BOLD, 15);

	// �ɽ���â
	private JLabel label = new JLabel("��ü ��ȭ");
	private JTextArea multichat = new JTextArea();
	private JTextField multichattext = new JTextField();
	private JScrollPane multichatscroll = new JScrollPane(multichat);

	private void display() {
		this.setContentPane(new JLabel(new ImageIcon("image/b3.jpg")));
		Container con = getContentPane();
		con.setBackground(Color.WHITE);
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

		// tree.setOpaque(false);
		// �ֻ�� �α��� ����â
		TitledBorder b2 = BorderFactory.createTitledBorder("�� ����â");
		Border b3 = BorderFactory.createTitledBorder("");
		b2.setTitleFont(font2);
		b2.setTitleColor(Color.WHITE);
		ss.setForeground(Color.WHITE);
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);
		ss.setFont(font);
		
		
		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);

		// �¶��ΰ� �������� ����
		friendlist.add(online);
		friendlist.add(offline);

		multichat.setEditable(false);// ����â�� ��������

		pop.add(start); // �˾��޴�
		pop.add(end);
		add(pop);


		// �α׾ƿ� ��ư
		logout.setBounds(12, 636, 370, 35);
		logout.setBackground(Color.BLACK);
		logout.setForeground(Color.WHITE);
		logout.setFont(font2);

		// ģ�� �߰� ��ư
		addfriend.setBounds(213, 30, 158, 52);
		addfriend.setBackground(Color.BLACK);
		addfriend.setForeground(Color.WHITE);
		addfriend.setFont(font2);
		// ��ü ä��â
		label.setBounds(12, 365, 370, 29);
		label.setFont(new Font("���� ���", Font.BOLD, 12));
		label.setBorder(b3);
		label.setForeground(Color.WHITE);

		DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
		render.setOpenIcon(new ImageIcon("image/flist.png"));
		render.setLeafIcon(new ImageIcon("image/f.png"));
		render.setClosedIcon(new ImageIcon("image/home.png"));
		render.setFont(font);
		tree.setCellRenderer(render);
		tree.setExpandsSelectedPaths(true);
		multichatscroll.setBounds(12, 401, 370, 180);
		multichattext.setBounds(12, 591, 370, 35);

		//Ʈ���̾�����
		traypop.add(open);
		traypop.add(close);
		add(traypop);
		trayIcon.setImageAutoSize(true);
		
		tray = SystemTray.getSystemTray();
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void event() {
		//super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					System.out.println("���ΰ�ħ");
					save();
				}
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (e.getButton() == 1) {
					if (node == null || !Client.online.contains(node.toString()))
						return;
					else if (e.getClickCount() == 2) {
						ChatRoomGUI room = Client.chatList.get(node.toString());
						if (room == null) {
							room = new ChatRoomGUI(node.toString());
							Client.chatList.put(node.toString(), room);
						}
						room.setVisible(true);
					}
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
			if(Client.online.contains(node.toString())){
				ChatRoomGUI room = Client.chatList.get(node.toString());
				if (room == null) {
					room = new ChatRoomGUI(node.toString());
					Client.chatList.put(node.toString(), room);
				}
				room.setVisible(true);
			}
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
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
					setVisible(true);
			}
		});
		
		open.addActionListener(e->{
			this.setVisible(true);
		});
		close.addActionListener(e->{
			dispose();
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
			Client.online = null;
			Client.identity = null;
			for (ChatRoomGUI gui : Client.chatList.values()) {
				gui.dispose();
			}
			Client.chatList = null;
			Client.conn.close();
			Client.conn = null;
			Client.receiver = null;
			tray.remove(trayIcon);
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
		Client.chatList = new HashMap<>();
		display();
		event();
		menu();
		load();
		super.setVisible(true);
		Client.receiver = new ClientReceiver();
		Client.receiver.setRunning(true);
		Client.receiver.start();
	}

	private void count() {
		friendscount = Client.friends.getFriendsList().size();
		onlinecount = Client.online.size();
		ss.setText("<html>�̸� : " + id + "<br>��ü ģ�� : " + friendscount + "��" + "<br>�������� ģ�� : "
				+ onlinecount + "��" + "</html>");
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
		while(Client.friends == null || Client.online == null){
			try{Thread.sleep(100);}catch(Exception e){}
		}
		online.removeAllChildren();
		offline.removeAllChildren();
		count();
		for (String id : Client.friends.getFriendsList().keySet()) {
			if(Client.online != null && Client.online.contains(id))
				online.add(new DefaultMutableTreeNode(id));
			else
				offline.add(new DefaultMutableTreeNode(id));
		}
		model.reload();
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
	
	public void selectUser(String fid){
		TreePath path = findPath(friendlist, fid);
		for(int i=0; i<4; i++){
			tree.setSelectionPath(path);
			try{Thread.sleep(500);}catch(Exception e){}
			tree.clearSelection();
			try{Thread.sleep(500);}catch(Exception e){}
		}
	}
	
	private TreePath findPath(DefaultMutableTreeNode root, String s) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			System.out.println(node.toString());
			if (node.toString().equalsIgnoreCase(s)) {
				TreePath path = new TreePath(node.getPath());
				System.out.println(path.toString());
				return path;
			}
		}
		return null;
	}
}
