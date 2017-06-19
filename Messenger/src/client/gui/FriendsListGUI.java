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
import general.container.Connection;
import general.container.Friends;
import server.Server;


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
	private JButton addfriend=new JButton("ģ�� �߰�");
	private String resultStr = null;
	private int count=0;
	
	

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
		//ģ�� �߰� ��ư
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
					if (nodes.equals(Client.friends.getNickname().get(i))) { // �г��� �迭��
					// �ִ� ����� ���Ͽ� ���� ���� �ִٸ� �ش� �ּҰ��� �ش��ϴ� �̸��� ���̵� ���
						snames.setText(Client.friends.getListname().get(i));
						snicks.setText(Client.friends.getNickname().get(i));
					}
				}

				TreePath path = tree.getPathForLocation(e.getX(), e.getY()); 
				//Ʈ�� ��� ���ex)[ȸ��,���,������,�г���2]																	
				if (e.getButton() == 3) { // ���콺 ��Ŭ���� �ϸ�
					int iRow = tree.getRowForLocation(e.getX(), e.getY()); // �ش� ���콺 ��ǥ ��ġ 
					tree.setSelectionRow(iRow); // Ʈ�� ��带 ��Ŭ��
					if (path != null) // Ʈ�� ��ΰ� ���� �ƴ϶��(���� �ִٸ�)
						if (Client.friends.getNickname().contains(node.toString())) {
							pop.show(tree, e.getX(), e.getY()); // �˾� �޴�â ���
						} else
							return; // �ٵ� ���ٸ� ����
				}
			}
		});
		addfriend.addActionListener(e->{

			resultStr = JOptionPane.showInputDialog("ģ���� ���̵� �Է��ϼ���.");
			if(Client.conn.getIdentity().equals(resultStr)){
			System.out.println("���°�"+resultStr);
				Client.friends.setListname(resultStr);
				String addname=JOptionPane.showInputDialog("ģ���� �̸��� �����ϼ���");
				Client.friends.setNickname(addname);
				System.out.println(Client.friends.getListname().toString());
				System.out.println(Client.friends.getNickname().toString());
				System.out.println(Client.friends.getListname().size());
				load();
				model.reload();
			
			}else{
				JOptionPane.showConfirmDialog(pop, "�ƴϿ���");
			}
					
					

		});
		start.addActionListener(e -> {//ä�ù�
			JFrameChatroom room = new JFrameChatroom();
			room.setVisible(true);
			dispose();

		});
		logout.addActionListener(e -> {//�α׾ƿ�
			if(Client.conn != null && !Client.conn.getSocket().isClosed()){
				try {
					Client.conn.close();
					Client.conn = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			Client.currentGUI=new LoginGUI();
			dispose();
		});
		end.addActionListener(e -> {//ģ������
			int num = 0;
			for (int i = 0; i < Client.friends.getListname().size(); i++) {
				if (node.toString().equals(Client.friends.getNickname().get(i))) {
						num = i;						
						Client.friends.getNickname().remove(num);
						Client.friends.getListname().remove(num);
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
		//save();
		load();
		display();
		event();
		menu();
		super.setVisible(true);

	}
	private void save() { 
		
		
	}
	private Friends f = Client.friends;
	private void load() {
		online.removeAllChildren();
		for (int i = 0; i < Client.friends.getListname().size(); i++) { // �α��ν� ģ�� ���
																// �ҷ����� �޼ҵ�
			String name = Client.friends.getNickname().get(i); // ������ ģ�����(ip�� �����Ѵٸ�)
			online.add(new DefaultMutableTreeNode(name));
		}
	}
}