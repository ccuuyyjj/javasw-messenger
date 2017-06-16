package client.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

class JFrameList extends JFrame implements MouseListener{
	private String id="���̵�";
	private String idname="���̵�";
	private String ip="192.168.0.1";
	private JLabel ss = new JLabel("<html>�̸� : "+id+"<br>���̵� : "+idname+"<br>������ : "+ip+"</html>");
	
	private DefaultMutableTreeNode list=new DefaultMutableTreeNode("ȸ�� ���"); 
	private JTree tree=new JTree(list);
	private DefaultMutableTreeNode online=new DefaultMutableTreeNode("������");
	private DefaultMutableTreeNode offline=new DefaultMutableTreeNode("��������");
	private List<String> listname=new ArrayList<>();
	private List<String> nickname=new ArrayList<>();
	private List<String> listip=new ArrayList<>();
	
	private JPopupMenu pop=new JPopupMenu();
	private JMenuItem start=new JMenuItem("����");
	private JMenuItem end=new JMenuItem("����");
	
	//�߹ٲ�
	private JLabel sname=new JLabel("�̸�");
	private JTextField snames=new JTextField();
	private JLabel sip=new JLabel("������");
	private JTextField sips=new JTextField();
	
	private JScrollPane scroll = new JScrollPane(tree);
	
	//private JButton start=new JButton("��ȸ����");
	private JButton logout=new JButton("�α׾ƿ�");

	private void display(){
		//���� â�� ��ġ�ϴ� ���� �ƴ϶� �̹� ��ġ�Ǿ� �ִ� ������ ������ �����ͼ� �װ��� ��ġ
		Container con=super.getContentPane();
		con.setLayout(null);
		con.add(ss,BorderLayout.NORTH);
		con.add(scroll,BorderLayout.CENTER);
		con.add(logout, BorderLayout.SOUTH);
		con.add(sname, BorderLayout.CENTER);
		con.add(snames, BorderLayout.CENTER);
		con.add(sip, BorderLayout.CENTER);
		con.add(sips, BorderLayout.CENTER);
		
		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);
		list.add(online);
		list.add(offline);
		
		sname.setBounds(12, 368, 57, 28);
		snames.setBounds(81, 372, 116, 21);
		sip.setBounds(12, 402, 57, 28);
		sips.setBounds(81, 403, 116, 21);
		
		snames.setEditable(false);
		sips.setEditable(false);
		
		pop.add(start);
		pop.add(end);
		add(pop);
		
		System.out.println(online.getChildCount());
		System.out.println(online.getChildAt(0));
	
		

		logout.setBounds(12, 636, 370, 35);
	//	con.setLayout(new GridLayout(8, 1));
		Border b2=BorderFactory.createTitledBorder("�α��� ����");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);
	
	}
	DefaultMutableTreeNode node;
	private void event(){
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(e.getButton()==1){
					if(node==null) return;
					System.out.println("node = "+node);
				}
				for(int i=0;i<nickname.size();i++){
					String nodes=node.toString();
					if(nodes.equals(nickname.get(i))){
						snames.setText(listname.get(i));
						if(listip.get(i)==null) sips.setText("�������Դϴ�.");
						else sips.setText(listip.get(i));
					}
				}
				TreePath path=tree.getPathForLocation(e.getX(), e.getY());
				if(e.getButton()==3){
					
							int iRow = tree.getRowForLocation(e.getX(), e.getY()); 
							tree.setSelectionRow(iRow);
							pop.show(tree, e.getX(), e.getY());		
						
							if(path!=null)
								pop.show(tree, e.getX(), e.getY());
								if(pop==null)
									return;
							
	

				}			
				
			}			
		});
		
		
		
	}
	private void menu(){}
	public JFrameList(){
		super.setTitle("��������");
		super.setSize(400,700);
		//�ü���� â ��ġ ���Ŀ� ���� ��ġ
		super.setLocationByPlatform(true);
		super.setResizable(false);
		save();
		load();
		display();
		event();
		menu();
		super.setVisible(true);
		
	}

	private void save() {
		listname.add(0, "��");
		listname.add(1, "��");
		listname.add(2, "�츮");
		listname.add(3, "���");
		nickname.add(0,"�г���1");
		nickname.add(1, "�г���2");
		nickname.add(2, "�г���3");
		nickname.add(3, "�г���4");
		listip.add(0,null);
		listip.add(1,"192.168.0.2");
		listip.add(2,"192.168.0.3");
		listip.add(3,"192.168.0.4");
		
	}
	
	private void load() {
		for(int i=0;i<nickname.size();i++){
			String name=nickname.get(i);
			//System.out.println(listip.get(i)!=null);
			if(listip.get(i)!=null){
			online.add(new DefaultMutableTreeNode(name));
			}else{
				offline.add(new DefaultMutableTreeNode(name));
			}
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Ŭ��");
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("����");
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("����");
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("����");
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("����");
		
	}
}

	

public class list {
	public static void main(String[] args) {
		JFrameList window=new JFrameList();
		
	}
}
