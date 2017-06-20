package client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import client.Client;
import general.container.Friends;
import general.container.Message;

class JFrameList extends JFrame {
	// 내 정보창
	private String id=Client.identity;// 상단 라벨에 표시될 자기 아이디
	private String idname; // 상단 라벨에 표시될 자기 닉네임
	private String ip; // 상단 라벨에 표시될 자기 ip
	private JLabel ss = new JLabel("<html>이름 : " + id + "<br>아이디 : " + idname + "<br>아이피 : " + ip + "</html>");

	private DefaultMutableTreeNode list = new DefaultMutableTreeNode("회원 목록");
	private JTree tree = new JTree(list);
	private DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("접속중");
	private DefaultMutableTreeNode offline = new DefaultMutableTreeNode("오프라인");
	
	// 팝업
	private JPopupMenu pop = new JPopupMenu(); // 트리 노트에서 우클릭시 나타날 팝업메뉴
	private JMenuItem start = new JMenuItem("대화시작");
	private JMenuItem end = new JMenuItem("친구삭제");

	private JLabel sname = new JLabel("이름"); // 트리 노드 클릭시 아래에 대화할 상대의 정보
	private JTextField snames = new JTextField();
	private JLabel snick = new JLabel("닉네임");
	private JTextField snicks = new JTextField(); // 트리 노드 클릭시 아래에 대화할 상대의 정보

	private JScrollPane scroll = new JScrollPane(tree); // 트리 노드 스크롤

	private JButton logout = new JButton("로그아웃"); // 로그아웃 버튼
	private JButton addfriend = new JButton("친구 추가");
	private String resultStr = null;

	private void display() {
		Container con = super.getContentPane();
		con.setLayout(null);
		con.add(ss, BorderLayout.NORTH);
		con.add(scroll, BorderLayout.CENTER);
		con.add(logout, BorderLayout.SOUTH);
		con.add(sname, BorderLayout.CENTER);
		con.add(snames, BorderLayout.CENTER);
		con.add(snick, BorderLayout.CENTER);
		con.add(snicks, BorderLayout.CENTER);
		con.add(addfriend, BorderLayout.NORTH);

		// 최상단 로그인 정보창
		Border b2 = BorderFactory.createTitledBorder("로그인 정보");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);

		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);

		// 온라인과 오프라인 구별
		list.add(online);
		list.add(offline);

		// 트리창에서 노드 클릭시 해당 닉네임에 포함된 이름과 아이피를 표시해주는 창
		sname.setBounds(12, 368, 57, 28);
		snames.setBounds(81, 372, 116, 21);
		snick.setBounds(12, 402, 57, 28);
		snicks.setBounds(81, 403, 116, 21);

		snames.setEditable(false); // 노드 클릭시 나타내는 상대 정보창이 텍스트 필드이므로
		snicks.setEditable(false); // 수정 못하게 금지시키기

		pop.add(start); // 팝업메뉴
		pop.add(end);
		add(pop);
		// 로그아웃 버튼
		logout.setBounds(12, 636, 370, 35);
		// 친구 추가 버튼
		addfriend.setBounds(224, 372, 158, 52);

	}

	DefaultMutableTreeNode node;// 노드 클릭시 해당 노드의 이름을 알려주는 코드

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (e.getButton() == 1) {
					if (node == null)
						return;
					// System.out.println("node = " + node);
				}
				for (int i = 0; i < Client.friends.getListname().size(); i++) { // 노드
															// 클릭시
					String nodes = node.toString(); // 노드의 내용이
					if (nodes.equals(Client.friends.getNickname().get(i))) { // 닉네임
															// 배열에
						// 있는 값들과 비교하여 같은 값이 있다면 해당 주소값에 해당하는 이름과
						// 아이디를 출력
						snames.setText(Client.friends.getListname().get(i));
						snicks.setText(Client.friends.getNickname().get(i));
					}
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
						if (Client.friends.getNickname().contains(node.toString())) {
							pop.show(tree, e.getX(), e.getY()); // 팝업
													// 메뉴창
													// 출력
						} else
							return; // 근데 없다면 리턴
				}
			}
		});
		addfriend.addActionListener(e -> {

			resultStr = JOptionPane.showInputDialog("친구의 아이디를 입력하세요.");

			if (Client.identity.equals(resultStr)) {
				JOptionPane.showMessageDialog(null, "자기자신은 추가할 수 없습니다.");

			} else {
				System.out.println("들어온거" + resultStr);
				String addname = JOptionPane.showInputDialog("친구의 이름을 설정하세요");
				if(resultStr==null) return;
				Client.friends.setListname(resultStr);
				Client.friends.setNickname(addname);
				
				System.out.println(Client.friends.getListname().toString());
				System.out.println(Client.friends.getNickname().toString());
				System.out.println(Client.friends.getListname().size());
				save();
				load();
				model.reload();
			}
		});
		start.addActionListener(e -> {// 채팅방
			
			ChatRoomGUI room = Client.chatList.get(node.toString());
			if(room == null){
				room = new ChatRoomGUI(node.toString());
				Client.chatList.put(node.toString(), room);
			}
//			room.setVisible(true);
//			
//			Client.friends.setTarget(node.toString());
//			chat();
		});
		logout.addActionListener(e -> {// 로그아웃
			if (Client.conn != null && !Client.conn.getSocket().isClosed()) {
				Client.conn.close();
				Client.conn = null;
				Client.friends = null;
				Client.identity = null;
				for(ChatRoomGUI gui : Client.chatList.values()){
					gui.dispose();
				}
				Client.chatList = null;
				Client.receiver.setRunning(false);
			}
			Client.currentMainGUI = new LoginGUI();
			dispose();
		});
		end.addActionListener(e -> {// 친구삭제

			for (int i = 0; i < Client.friends.getListname().size(); i++) {

				if (node.toString().equals(Client.friends.getNickname().get(i))) {

					Client.friends.getNickname().remove(i);
					Client.friends.getListname().remove(i);

				}
				save();
				load();
				model.reload();
				break;
			}
		});
	}

	private void chat() {//채팅 시작시 상대방에게 채팅에 응하겠냐는 창을 띄우는 메소드
		if(Client.friends.getTarget()!=Client.identity) {
			int i=JOptionPane.showConfirmDialog(null,Client.friends.getTarget()+
				"님이 당신과 대화하기를 원합니다. 하시겠습니까?",null, JOptionPane.YES_NO_OPTION);
			if(i==0){
				ChatRoomGUI room = new ChatRoomGUI(Client.friends.getTarget());
				room.setVisible(true);
			}else return;
		}
		
		
	}

	private void menu() {
	}

	public JFrameList() {
		super.setTitle("친구목록");
		super.setSize(400, 700);
		// 운영체제의 창 배치 형식에 따라 배치
		super.setLocationByPlatform(true);
		super.setResizable(false);
		load();
		display();
		event();
		menu();
		super.setVisible(true);
		Client.receiver.start();
	}
	private void save() {
		try {
			Message msg = new Message(Client.identity, "=[SERVER]=", Client.friends);
			Client.conn.sendObject(msg);
			while (true) {
				String[] header = Client.conn.getHeader();
				if (header != null) {
					Friends newFriends = (Friends) Client.conn.getObject(Integer.parseInt(header[2]));
					if (!Client.friends.equals(newFriends)) {
						Client.friends = newFriends;

					}
					break;
				}
			}
		} catch (IOException | NumberFormatException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void load() {
		online.removeAllChildren();
		for (int i = 0; i < Client.friends.getListname().size(); i++) { // 로그인시
													// 친구
													// 목록
													// 불러오는
													// 메소드
			String name = Client.friends.getNickname().get(i); // 접속한
												// 친구라면(ip가
												// 존재한다면)
			online.add(new DefaultMutableTreeNode(name));
		}
	}
	
}
