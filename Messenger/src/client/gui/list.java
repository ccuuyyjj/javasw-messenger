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
	private String a="강아지\n토끼\n고양이";
	private String id="아이디";
	private String idname="아이디";
	private JLabel name=new JLabel("되냐");
	private JLabel ss = new JLabel("<html>이름 : "+id+"<br>아이디 : "+idname+"</html>");
	
	private DefaultMutableTreeNode list=new DefaultMutableTreeNode("친구 목록"); 
	private JTree tree=new JTree(list);
	private DefaultMutableTreeNode online=new DefaultMutableTreeNode("접속중");
	private DefaultMutableTreeNode offline=new DefaultMutableTreeNode("오프라인");
	private DefaultMutableTreeNode f1=new DefaultMutableTreeNode("1");
	private DefaultMutableTreeNode f2=new DefaultMutableTreeNode("2");
	private DefaultMutableTreeNode f3=new DefaultMutableTreeNode("3");
	private DefaultMutableTreeNode f4=new DefaultMutableTreeNode("4");
	private JScrollPane scroll = new JScrollPane(tree);
	private JButton logout=new JButton("로그아웃");
	
	private JPopupMenu pop=new JPopupMenu();
	private JMenuItem delete,open;

	private void display(){
		//직접 창에 배치하는 것이 아니라 이미 설치되어 있는 투명한 유리를 가져와서 그곳에 배치
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
		
		delete=new JMenuItem("삭제");
		open=new JMenuItem("열기");
		pop.add(delete);
		pop.add(open);
		add(pop);
		logout.setBounds(12, 636, 370, 35);
	//	con.setLayout(new GridLayout(8, 1));
		Border b2=BorderFactory.createTitledBorder("로그인 정보");
		ss.setBorder(b2);
		ss.setBounds(12, 10, 370, 85);
	
	}
	private void event(){
		//super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//종료금지
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//super.setDefaultCloseOperation(HIDE_ON_CLOSE);//숨김
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
		super.setTitle("스윙예제");
		super.setSize(400,700);
		//운영체제의 창 배치 형식에 따라 배치
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
