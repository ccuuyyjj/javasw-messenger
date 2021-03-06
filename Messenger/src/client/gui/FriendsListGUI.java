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

	// 내 정보창
	private String id = Client.identity;// 상단 라벨에 표시될 자기 아이디
	private int allfriend; // 오프라인+온라인 친구
	private int connecting; // 온라인친구
	private JLabel ss = new JLabel("<html>이름 : " + id + "<br>전체 친구 : " + allfriend + "명" + "<br>접속중인 친구 : "
			+ connecting + "명" + "</html>");

	private DefaultMutableTreeNode friendlist = new DefaultMutableTreeNode("회원 목록");
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
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("접속중");

	// 팝업
	private JPopupMenu pop = new JPopupMenu(); // 트리 노트에서 우클릭시 나타날 팝업메뉴
	private JMenuItem start = new JMenuItem("대화시작");
	private JMenuItem end = new JMenuItem("친구삭제");

	// 메세지 수신확인 팝업
	private JPopupMenu msgpop = new JPopupMenu();
	private JMenuItem yes = new JMenuItem("대화시작");
	private JMenuItem no = new JMenuItem("취소");

	private JLabel sname = new JLabel("이름"); // 트리 노드 클릭시 아래에 대화할 상대의 정보
	private JTextField snames = new JTextField();
	private JLabel snick = new JLabel("닉네임");
	private JTextField snicks = new JTextField(); // 트리 노드 클릭시 아래에 대화할 상대의 정보

	private JScrollPane scroll = new JScrollPane(tree); // 트리 노드 스크롤

	private JButton logout = new JButton("로그아웃"); // 로그아웃 버튼
	private JButton addfriend = new JButton("친구 추가");

	Container con = super.getContentPane();

	// 심심한창
	private JLabel label = new JLabel("전체 대화");
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

		// 최상단 로그인 정보창
		Border b2 = BorderFactory.createTitledBorder("내 로그인 정보");
		Border b3 = BorderFactory.createTitledBorder("");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);

		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);

		// 온라인과 오프라인 구별
		friendlist.add(online);

		multichat.setEditable(false);// 퀴즈창에 수정금지

		pop.add(start); // 팝업메뉴
		pop.add(end);
		add(pop);

		msgpop.add(yes);
		msgpop.add(no);
		add(msgpop);

		// 로그아웃 버튼
		logout.setBounds(12, 636, 370, 35);
		// 친구 추가 버튼
		addfriend.setBounds(213, 30, 158, 52);
		// 심심한 창
		label.setBounds(12, 365, 370, 29);
		label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		label.setBorder(b3);

		DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
		render.setOpenIcon(new ImageIcon("image/sampleicon2.jpg"));
		render.setLeafIcon(new ImageIcon("image/sampleicon1.jpg"));
		render.setClosedIcon(new ImageIcon("image/sampleicon2.jpg"));

		tree.setCellRenderer(render);
		multichatscroll.setBounds(12, 401, 370, 180);
		multichattext.setBounds(12, 591, 370, 35);

	}

	DefaultMutableTreeNode node;// 노드 클릭시 해당 노드의 이름을 알려주는 코드

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					System.out.println("새로고침");
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
				// 트리 경로 출력ex)[회원,목록,접속중,닉네임2]
				if (e.getButton() == 3) { // 마우스 우클릭을 하면
					int iRow = tree.getRowForLocation(e.getX(), e.getY()); // 해당
					// 마우스
					// 좌표
					// 위치
					tree.setSelectionRow(iRow); // 트리 노드를 좌클릭

					if (path != null) // 트리 경로가 널이 아니라면(값이 있다면)
						for (String id : Client.friends.getFriendsList().keySet()) {
							if (node.toString().contains(id)) {
								pop.show(tree, e.getX(), e.getY()); // 팝업
								// 메뉴창
								// 출력
							}
						}
				}
			}
		});

		addfriend.addActionListener(e -> {
			String listname = JOptionPane.showInputDialog("친구의 아이디를 입력하세요.");
			for (String id : Client.friends.getFriendsList().keySet()) {
				if (listname.equals(id)) {
					listname = null;
					break;
				}
			}
			if (listname == null || listname.isEmpty()) {
				JOptionPane.showMessageDialog(null, "입력이 올바르지 않습니다.");
			} else if (Client.identity.equals(listname)) {
				JOptionPane.showMessageDialog(null, "자기자신은 추가할 수 없습니다.");
			} else {
				// System.out.println("들어온거" + listname);
				String nickname = JOptionPane.showInputDialog("친구의 이름을 설정하세요");
				if (nickname == null | nickname.isEmpty()) {
					nickname = listname;
				}
				Client.friends.add(listname, nickname);
			}
			save();
		});
		start.addActionListener(e -> {// 채팅방

			ChatRoomGUI room = Client.chatList.get(node.toString());
			if (room == null) {
				room = new ChatRoomGUI(node.toString());
				Client.chatList.put(node.toString(), room);
			}
			room.setVisible(true);
		});
		logout.addActionListener(e -> {// 로그아웃
			dispose();
			Client.currentMainGUI = new LoginGUI();
		});
		end.addActionListener(e -> {// 친구삭제
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
		super.setTitle("친구목록");
		super.setSize(400, 710);
		// 운영체제의 창 배치 형식에 따라 배치
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
		ss.setText("<html>이름 : " + id + "<br>전체 친구 : " + allfriend + "명" + "<br>접속중인 친구 : " + connecting + "명"
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
