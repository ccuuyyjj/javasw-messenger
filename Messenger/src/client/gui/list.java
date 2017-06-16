package client.gui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.tree.TreePath;

import client.Client;
import general.container.Friends;

class JFrameList extends JFrame {
	//�� ����â
	private String id;						//��� �󺧿� ǥ�õ� �ڱ� ���̵�
	private String idname;					//��� �󺧿� ǥ�õ� �ڱ� �г���
	private String ip;	//��� �󺧿� ǥ�õ� �ڱ� ip
	private JLabel ss = new JLabel("<html>�̸� : " + id + "<br>���̵� : " + idname + "<br>������ : " + ip + "</html>");

	private DefaultMutableTreeNode list = new DefaultMutableTreeNode("ȸ�� ���");
	private JTree tree = new JTree(list);
	private DefaultMutableTreeNode online = new DefaultMutableTreeNode("������");
	private DefaultMutableTreeNode offline = new DefaultMutableTreeNode("��������");
	
	private List<String> listname = new ArrayList<>();		//��ü ȸ���� �̸�
	private List<String> nickname = new ArrayList<>();	//��ü ȸ���� �г���
	private List<String> listip = new ArrayList<>();			//��ü ȸ���� ip
	//�˾�
	private JPopupMenu pop = new JPopupMenu();		//Ʈ�� ��Ʈ���� ��Ŭ���� ��Ÿ�� �˾��޴�
	private JMenuItem start = new JMenuItem("����");
	private JMenuItem end = new JMenuItem("����");


	private JLabel sname = new JLabel("�̸�");				//Ʈ�� ��� Ŭ���� �Ʒ��� ��ȭ�� ����� ����
	private JTextField snames = new JTextField();
	private JLabel sip = new JLabel("������");
	private JTextField sips = new JTextField();				//Ʈ�� ��� Ŭ���� �Ʒ��� ��ȭ�� ����� ����

	private JScrollPane scroll = new JScrollPane(tree);		//Ʈ�� ��� ��ũ��

	private JButton logout = new JButton("�α׾ƿ�");		//�α׾ƿ� ��ư

	private void display() {
		Container con = super.getContentPane();
		con.setLayout(null);
		con.add(ss, BorderLayout.NORTH);
		con.add(scroll, BorderLayout.CENTER);
		con.add(logout, BorderLayout.SOUTH);
		con.add(sname, BorderLayout.CENTER);
		con.add(snames, BorderLayout.CENTER);
		con.add(sip, BorderLayout.CENTER);
		con.add(sips, BorderLayout.CENTER);
		
		//�ֻ�� �α��� ����â
		Border b2 = BorderFactory.createTitledBorder("�α��� ����");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);
		
		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);
		
		//�¶��ΰ� �������� ����
		list.add(online);
		list.add(offline);
		
		//Ʈ��â���� ��� Ŭ���� �ش� �г��ӿ� ���Ե� �̸��� �����Ǹ� ǥ�����ִ� â
		sname.setBounds(12, 368, 57, 28);
		snames.setBounds(81, 372, 116, 21);
		sip.setBounds(12, 402, 57, 28);
		sips.setBounds(81, 403, 116, 21);

		snames.setEditable(false);		//��� Ŭ���� ��Ÿ���� ��� ����â�� �ؽ�Ʈ �ʵ��̹Ƿ�
		sips.setEditable(false);			//���� ���ϰ� ������Ű��

		pop.add(start);			//�˾��޴�
		pop.add(end);
		add(pop);

	//	System.out.println(online.getChildCount());
	//	System.out.println(online.getChildAt(0));
		
		//�α׾ƿ� ��ư
		logout.setBounds(12, 636, 370, 35);

	}

	DefaultMutableTreeNode node;//��� Ŭ���� �ش� ����� �̸��� �˷��ִ� �ڵ�

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (e.getButton() == 1) {
					if (node == null)
						return;
					//System.out.println("node = " + node);
				}
				for (int i = 0; i < nickname.size(); i++) {	//��� Ŭ����
					String nodes = node.toString();			//����� ������
					if (nodes.equals(nickname.get(i))) {		//�г��� �迭�� �ִ� ����� ���Ͽ� ���� ���� �ִٸ�
						snames.setText(listname.get(i));		//�ش� �ּҰ��� �ش��ϴ� �̸��� �����Ǹ� ���
						if (listip.get(i) == null)					//���� ���������̶�� 
							sips.setText("�������Դϴ�.");		//������ �κп��� �������Դϴ�.�� ���
						else										//
							sips.setText(listip.get(i));			//
					}
				}
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());	//Ʈ�� ��� ��� ex) [ȸ�� ���, ������, �г���2]
				if (e.getButton() == 3) {												//���콺 ��Ŭ���� �ϸ�						
					int iRow = tree.getRowForLocation(e.getX(), e.getY());		//�ش� ���콺 ��ǥ�� ��ġ��
					tree.setSelectionRow(iRow);										//Ʈ�� ��带 ��Ŭ��
					if (path != null)														//Ʈ�� ��ΰ� ���� �ƴ϶��(���� �ִٸ�)
						pop.show(tree, e.getX(), e.getY());							//�˾� �޴�â ���
					else	return;														//�ٵ� ���ٸ� ����
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
		super.setVisible(true);

	}

	private void save() {		//ģ�� �߰� �� �����ÿ� ���� ���� �޼ҵ�
									//listip�� �α��ΰ� �α׾ƿ� �� �����ǰ� �ٲ� �� �����Ƿ� 
									//������� ���� �����°� ������?
		listname.add(0, "��");
		listname.add(1, "��");
		listname.add(2, "�츮");
		listname.add(3, "���");
		nickname.add(0, "�г���1");
		nickname.add(1, "�г���2");
		nickname.add(2, "�г���3");
		nickname.add(3, "�г���4");
		listip.add(0, null);
		listip.add(1, "192.168.0.2");
		listip.add(2, "192.168.0.3");
		listip.add(3, "192.168.0.4");

	}
	private Friends f = null;
	private void load() {
		f = Client.friends;
		f.getFriends().get(1);
		for (int i = 0; i < nickname.size(); i++) {		//�α��ν� ģ�� ��� �ҷ����� �޼ҵ�
			String name = nickname.get(i);				//������ ģ�����(ip�� �����Ѵٸ�)
			// System.out.println(listip.get(i)!=null);		//�¶��ο� �ְ�����
			if (listip.get(i) != null) {						//���� �׷��� �ʴٸ�(ip�� null) �������ο� ��½�Ű��
				online.add(new DefaultMutableTreeNode(name));
			} else {
				offline.add(new DefaultMutableTreeNode(name));
			}
		}

	}

}
