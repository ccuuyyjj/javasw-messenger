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
	// �� ����â
	private String id=Client.identity;// ��� �󺧿� ǥ�õ� �ڱ� ���̵�
	private String idname; // ��� �󺧿� ǥ�õ� �ڱ� �г���
	private String ip; // ��� �󺧿� ǥ�õ� �ڱ� ip
	private JLabel ss = new JLabel("<html>�̸� : " + id + "<br>���̵� : " + idname + "<br>������ : " + ip + "</html>");

	private DefaultMutableTreeNode list = new DefaultMutableTreeNode("ȸ�� ���");
	private JTree tree = new JTree(list);
	private DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("������");
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
		// �α׾ƿ� ��ư
		logout.setBounds(12, 636, 370, 35);
		// ģ�� �߰� ��ư
		addfriend.setBounds(224, 372, 158, 52);

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
				for (int i = 0; i < Client.friends.getListname().size(); i++) { // ���
															// Ŭ����
					String nodes = node.toString(); // ����� ������
					if (nodes.equals(Client.friends.getNickname().get(i))) { // �г���
															// �迭��
						// �ִ� ����� ���Ͽ� ���� ���� �ִٸ� �ش� �ּҰ��� �ش��ϴ� �̸���
						// ���̵� ���
						snames.setText(Client.friends.getListname().get(i));
						snicks.setText(Client.friends.getNickname().get(i));
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
						if (Client.friends.getNickname().contains(node.toString())) {
							pop.show(tree, e.getX(), e.getY()); // �˾�
													// �޴�â
													// ���
						} else
							return; // �ٵ� ���ٸ� ����
				}
			}
		});
		addfriend.addActionListener(e -> {

			resultStr = JOptionPane.showInputDialog("ģ���� ���̵� �Է��ϼ���.");

			if (Client.identity.equals(resultStr)) {
				JOptionPane.showMessageDialog(null, "�ڱ��ڽ��� �߰��� �� �����ϴ�.");

			} else {
				System.out.println("���°�" + resultStr);
				String addname = JOptionPane.showInputDialog("ģ���� �̸��� �����ϼ���");
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
		start.addActionListener(e -> {// ä�ù�
			
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
		logout.addActionListener(e -> {// �α׾ƿ�
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
		end.addActionListener(e -> {// ģ������

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

	private void chat() {//ä�� ���۽� ���濡�� ä�ÿ� ���ϰڳĴ� â�� ���� �޼ҵ�
		if(Client.friends.getTarget()!=Client.identity) {
			int i=JOptionPane.showConfirmDialog(null,Client.friends.getTarget()+
				"���� ��Ű� ��ȭ�ϱ⸦ ���մϴ�. �Ͻðڽ��ϱ�?",null, JOptionPane.YES_NO_OPTION);
			if(i==0){
				ChatRoomGUI room = new ChatRoomGUI(Client.friends.getTarget());
				room.setVisible(true);
			}else return;
		}
		
		
	}

	private void menu() {
	}

	public JFrameList() {
		super.setTitle("ģ�����");
		super.setSize(400, 700);
		// �ü���� â ��ġ ���Ŀ� ���� ��ġ
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
		for (int i = 0; i < Client.friends.getListname().size(); i++) { // �α��ν�
													// ģ��
													// ���
													// �ҷ�����
													// �޼ҵ�
			String name = Client.friends.getNickname().get(i); // ������
												// ģ�����(ip��
												// �����Ѵٸ�)
			online.add(new DefaultMutableTreeNode(name));
		}
	}
	
}
