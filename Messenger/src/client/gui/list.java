package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

class JFrameList extends JFrame implements MouseListener{
	private String a="������\n�䳢\n�����";
	private String id="���̵�";
	private String idname="���̵�";
	private JLabel name=new JLabel("�ǳ�");
	private JLabel ss = new JLabel("<html>�̸� : "+id+"<br>���̵� : "+idname+"</html>");
	
	private DefaultMutableTreeNode list=new DefaultMutableTreeNode("ģ�� ���"); 
	private JTree tree=new JTree(list);
	private DefaultMutableTreeNode online=new DefaultMutableTreeNode("������");
	private DefaultMutableTreeNode offline=new DefaultMutableTreeNode("��������");
	private DefaultMutableTreeNode f1=new DefaultMutableTreeNode("1");
	private DefaultMutableTreeNode f2=new DefaultMutableTreeNode("2");
	private DefaultMutableTreeNode f3=new DefaultMutableTreeNode("3");
	private DefaultMutableTreeNode f4=new DefaultMutableTreeNode("4");
	private JScrollPane scroll = new JScrollPane(tree);
	private JButton logout=new JButton("�α׾ƿ�");
	
	private JPopupMenu pop=new JPopupMenu();
	private JMenuItem delete,open;

	private void display(){
		//���� â�� ��ġ�ϴ� ���� �ƴ϶� �̹� ��ġ�Ǿ� �ִ� ������ ������ �����ͼ� �װ��� ��ġ
		Container con=super.getContentPane();
		con.setLayout(null);
		Border b1=new LineBorder(Color.black,3);
		con.add(ss,BorderLayout.NORTH);
		con.add(scroll,BorderLayout.CENTER);
		con.add(logout, BorderLayout.SOUTH);
		
		scroll.setBounds(12, 105, 370, 257);
		scroll.setViewportView(tree);
		list.add(online);
		list.add(offline);
		
		online.add(f1);
		online.add(f2);
		offline.add(f3);
		offline.add(f4);
		
		delete=new JMenuItem("����");
		open=new JMenuItem("����");
		pop.add(delete);
		pop.add(open);
		add(pop);
		logout.setBounds(12, 636, 370, 35);
	//	con.setLayout(new GridLayout(8, 1));
		Border b2=BorderFactory.createTitledBorder("�α��� ����");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);
	
	}
	private void event(){
		//super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//�������
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//super.setDefaultCloseOperation(HIDE_ON_CLOSE);//����
		tree.addMouseListener(this);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println(e.getOldLeadSelectionPath());
				
				
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
		display();
		event();
		menu();
		super.setVisible(true);
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		TreePath path=tree.getPathForLocation(e.getX(), e.getY());
		Object obj=path.getLastPathComponent();
		DefaultMutableTreeNode dmtn=(DefaultMutableTreeNode)obj;
		String gg=dmtn.getUserObject().toString();
		if(e.getButton()==3)
		pop.show(tree, e.getX(), e.getY());
			System.out.println();
			//System.out.println(dmtn);
		}
		
		
		
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

}
public class list {
	public static void main(String[] args) {
		JFrameList window=new JFrameList();
	}
}
