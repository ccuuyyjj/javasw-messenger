package client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


import general.container.Friends;

class JFrameList extends JFrame {
	// 내 정보창
	private String id; // 상단 라벨에 표시될 자기 아이디
	private String idname; // 상단 라벨에 표시될 자기 닉네임
	private String ip; // 상단 라벨에 표시될 자기 ip
	private JLabel ss = new JLabel("<html>이름 : " + id + "<br>아이디 : " + idname + "<br>아이피 : " + ip + "</html>");

	private DefaultMutableTreeNode list = new DefaultMutableTreeNode("회원 목록");
	private JTree tree = new JTree(list);
	private DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("접속중");
	private DefaultMutableTreeNode offline = new DefaultMutableTreeNode("오프라인");

	private Friends friend;

	// private List<String> listname = new ArrayList<>(); //전체 회원의 이름
	// private List<String> nickname = new ArrayList<>(); //전체 회원의 닉네임
	// private List<String> listip = new ArrayList<>(); //전체 회원의 ip
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

		// System.out.println(online.getChildCount());
		// System.out.println(online.getChildAt(0));

		// 로그아웃 버튼
		logout.setBounds(12, 636, 370, 35);

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
				for (int i = 0; i < friend.getListname().size(); i++) { // 노드
																		// 클릭시
					String nodes = node.toString(); // 노드의 내용이
					if (nodes.equals(friend.getNickname().get(i))) { // 닉네임 배열에
																		// 있는
																		// 값들과
																		// 비교하여
																		// 같은 값이
																		// 있다면
						snames.setText(friend.getListname().get(i)); // 해당 주소값에
																		// 해당하는
																		// 이름과
																		// 아이피를
																		// 출력
						snicks.setText(friend.getNickname().get(i));
						// if (listip.get(i) == null) //만약 오프라인이라면
						// sips.setText("미접속입니다."); //아이피 부분에는 미접속입니다.를 출력
						// else //
						// sips.setText(listip.get(i)); //
					}
				}

				TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 트리
																				// 경로
																				// 출력
																				// ex)
																				// [회원
																				// 목록,
																				// 접속중,
																				// 닉네임2]
				if (e.getButton() == 3) { // 마우스 우클릭을 하면
					int iRow = tree.getRowForLocation(e.getX(), e.getY()); // 해당
																			// 마우스
																			// 좌표에
																			// 위치한
					tree.setSelectionRow(iRow); // 트리 노드를 좌클릭
					if (path != null) // 트리 경로가 널이 아니라면(값이 있다면)
						if (friend.getNickname().contains(node.toString())) {
							pop.show(tree, e.getX(), e.getY()); // 팝업 메뉴창 출력
						} else
							return; // 근데 없다면 리턴
				}
			}
		});
		start.addActionListener(e -> {
			JFrameChatroom room = new JFrameChatroom();
			room.setVisible(true);

		});
		logout.addActionListener(e -> {
			
			LoginGUI login=new LoginGUI();
			
			login.setVisible(true);
			
		});
		end.addActionListener(e -> {
			int num = 0;
			for (int i = 0; i < friend.getListname().size(); i++) {
				if (node.toString().equals(friend.getNickname().get(i))) {
						num = i;						
						friend.getNickname().remove(num);
						friend.getListname().remove(num);
					
						online.remove(num);				
						model.reload();
						break;
					
				}
			}
		});
	}

	private void menu() {
	}

	public JFrameList() {
		super.setTitle("스윙예제");
		super.setSize(400, 700);
		// 운영체제의 창 배치 형식에 따라 배치
		super.setLocationByPlatform(true);
		super.setResizable(false);
		save();
		load();
		display();
		event();
		menu();
		chat();
		super.setVisible(true);

	}

	private void chat() {// 채팅 시작

	}

	private void save() { // 친구 추가 및 변동시에 생길 저장 메소드
							// listip는 로그인과 로그아웃 시 아이피가 바뀔 수 있으므로
							// 스레드로 따로 돌리는게 좋겠지?

		friend = new Friends();
		friend.setListname("이름");
		friend.setNickname("닉네임");

		friend.setListname("이름1");
		friend.setNickname("닉네임1");

		friend.setListname("이름2");
		friend.setNickname("닉네임2");

	}

	private void load() {

		for (int i = 0; i < friend.getListname().size(); i++) { // 로그인시 친구 목록
																// 불러오는 메소드

			String name = friend.getNickname().get(i); // 접속한 친구라면(ip가 존재한다면)
			// System.out.println("name : "+name);
			// System.out.println(listip.get(i)!=null); //온라인에 넣겠지만
			// if (listip.get(i) != null) { //만약 그렇지 않다면(ip가 null) 오프라인에 출력시키기
			// online.add(new DefaultMutableTreeNode(name));
			// } else {
			// offline.add(new DefaultMutableTreeNode(name));
			// }
			online.add(new DefaultMutableTreeNode(friend.getNickname().get(i)));
		}
		// Iterator name=friend.getFriends().entrySet().iterator();
		// while(name.hasNext()){
		// Entry out=(Entry)name.next();
		// online.add(new DefaultMutableTreeNode(out.getKey()));

	}

}

public class list {
	public static void main(String[] args) {
		JFrameList window = new JFrameList();

	}
}
