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
	// �� ����â
	private String id; // ��� �󺧿� ǥ�õ� �ڱ� ���̵�
	private String idname; // ��� �󺧿� ǥ�õ� �ڱ� �г���
	private String ip; // ��� �󺧿� ǥ�õ� �ڱ� ip
	private JLabel ss = new JLabel("<html>�̸� : " + id + "<br>���̵� : " + idname + "<br>������ : " + ip + "</html>");

	private DefaultMutableTreeNode list = new DefaultMutableTreeNode("ȸ�� ���");
	private JTree tree = new JTree(list);
	private DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("������");
	private DefaultMutableTreeNode offline = new DefaultMutableTreeNode("��������");

	private Friends friend;

	// private List<String> listname = new ArrayList<>(); //��ü ȸ���� �̸�
	// private List<String> nickname = new ArrayList<>(); //��ü ȸ���� �г���
	// private List<String> listip = new ArrayList<>(); //��ü ȸ���� ip
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

		// �ֻ�� �α��� ����â
		Border b2 = BorderFactory.createTitledBorder("�α��� ����");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);

		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);

		// �¶��ΰ� �������� ����
		list.add(online);
		list.add(offline);

		// Ʈ��â���� ��� Ŭ���� �ش� �г��ӿ� ���Ե� �̸��� �����Ǹ� ǥ�����ִ� â
		sname.setBounds(12, 368, 57, 28);
		snames.setBounds(81, 372, 116, 21);
		snick.setBounds(12, 402, 57, 28);
		snicks.setBounds(81, 403, 116, 21);

		snames.setEditable(false); // ��� Ŭ���� ��Ÿ���� ��� ����â�� �ؽ�Ʈ �ʵ��̹Ƿ�
		snicks.setEditable(false); // ���� ���ϰ� ������Ű��

		pop.add(start); // �˾��޴�
		pop.add(end);
		add(pop);

		// System.out.println(online.getChildCount());
		// System.out.println(online.getChildAt(0));

		// �α׾ƿ� ��ư
		logout.setBounds(12, 636, 370, 35);

	}

	DefaultMutableTreeNode node;// ��� Ŭ���� �ش� ����� �̸��� �˷��ִ� �ڵ�

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
				for (int i = 0; i < friend.getListname().size(); i++) { // ���
																		// Ŭ����
					String nodes = node.toString(); // ����� ������
					if (nodes.equals(friend.getNickname().get(i))) { // �г��� �迭��
																		// �ִ�
																		// �����
																		// ���Ͽ�
																		// ���� ����
																		// �ִٸ�
						snames.setText(friend.getListname().get(i)); // �ش� �ּҰ���
																		// �ش��ϴ�
																		// �̸���
																		// �����Ǹ�
																		// ���
						snicks.setText(friend.getNickname().get(i));
						// if (listip.get(i) == null) //���� ���������̶��
						// sips.setText("�������Դϴ�."); //������ �κп��� �������Դϴ�.�� ���
						// else //
						// sips.setText(listip.get(i)); //
					}
				}

				TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // Ʈ��
																				// ���
																				// ���
																				// ex)
																				// [ȸ��
																				// ���,
																				// ������,
																				// �г���2]
				if (e.getButton() == 3) { // ���콺 ��Ŭ���� �ϸ�
					int iRow = tree.getRowForLocation(e.getX(), e.getY()); // �ش�
																			// ���콺
																			// ��ǥ��
																			// ��ġ��
					tree.setSelectionRow(iRow); // Ʈ�� ��带 ��Ŭ��
					if (path != null) // Ʈ�� ��ΰ� ���� �ƴ϶��(���� �ִٸ�)
						if (friend.getNickname().contains(node.toString())) {
							pop.show(tree, e.getX(), e.getY()); // �˾� �޴�â ���
						} else
							return; // �ٵ� ���ٸ� ����
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
		super.setTitle("��������");
		super.setSize(400, 700);
		// �ü���� â ��ġ ���Ŀ� ���� ��ġ
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

	private void chat() {// ä�� ����

	}

	private void save() { // ģ�� �߰� �� �����ÿ� ���� ���� �޼ҵ�
							// listip�� �α��ΰ� �α׾ƿ� �� �����ǰ� �ٲ� �� �����Ƿ�
							// ������� ���� �����°� ������?

		friend = new Friends();
		friend.setListname("�̸�");
		friend.setNickname("�г���");

		friend.setListname("�̸�1");
		friend.setNickname("�г���1");

		friend.setListname("�̸�2");
		friend.setNickname("�г���2");

	}

	private void load() {

		for (int i = 0; i < friend.getListname().size(); i++) { // �α��ν� ģ�� ���
																// �ҷ����� �޼ҵ�

			String name = friend.getNickname().get(i); // ������ ģ�����(ip�� �����Ѵٸ�)
			// System.out.println("name : "+name);
			// System.out.println(listip.get(i)!=null); //�¶��ο� �ְ�����
			// if (listip.get(i) != null) { //���� �׷��� �ʴٸ�(ip�� null) �������ο� ��½�Ű��
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
