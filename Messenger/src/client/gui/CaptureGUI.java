package client.gui;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class CaptureGUI extends JFrame {
	private Rectangle area;
	
	private Container con;
	private ImageIcon img;
	private JLabel lb;
	
	private Point press;
	private Point release;

	private String myid;
	private String youid;
	
	private void display(Image image) {
//		���� â�� ��ġ�ϴ� ���� �ƴ϶� �̹� ��ġ�Ǿ� �ִ� ������ ������ �����ͼ� �� ���� ��ġ
		con = super.getContentPane();
		con.setLayout(null);
		
		img = new ImageIcon(image);
		lb = new JLabel(img);
		lb.setOpaque(true);
		Dimension r = Toolkit.getDefaultToolkit().getScreenSize();
		lb.setBounds(0, 0, r.width, r.height);
		con.add(lb);
	}
	
	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		MouseListener mListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				release = MouseInfo.getPointerInfo().getLocation(); //���콺 Ŭ�� �� ��ǥ
				try {
					capture();
				} catch (AWTException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				press = MouseInfo.getPointerInfo().getLocation(); //���콺 �� �� ��ǥ
			}
			
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		};
		lb.addMouseListener(mListener);
	}
	
	private void menu() {}
	
	private void capture() throws AWTException, IOException {
		Robot robot = new Robot();
		int width = release.x - press.x; //���� �ʺ�, ���� ���
		int height = release.y - press.y;
		if (width > 0 && height > 0) {
			area = new Rectangle(press.x, press.y, width, height);
		    Graphics g = getGraphics();
		    g.drawRect(press.x, press.y, width, height); //���� ǥ��
		    BufferedImage bufferedImage = robot.createScreenCapture(area);
		    
		    File capture = new File("files", "capture.png");
		    if (!capture.exists()) capture.getParentFile().mkdirs();
		    try {
				ImageIO.write(bufferedImage, "png", capture);
			} catch (IOException e1) {
				e1.printStackTrace();
			} //������ ���� ĸ�� �� ����
		    CaptureNoteGUI t = new CaptureNoteGUI(width, height, myid, youid);
		    dispose();
//		    LoadImageApp.main(null);
		} else JOptionPane.showMessageDialog(lb, "������ �߸� �����Ǿ����ϴ�");
		
//		Runtime.getRuntime().exec("c:\\windows\\system32\\mspaint.exe");
	}
	
	public CaptureGUI(Image img, String myid, String youid) {
		super.setTitle("ĸ�� ��ũ����");
		super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//		�ü��(window)�� â ��ġ ���Ŀ� ���� ��ġ
		super.setLocation(0, 0);		// ������ �ü���� ����
		super.setResizable(false);
		super.setUndecorated(true);
		this.myid = myid;
		this.youid = youid;
		display(img);
		event();
		menu();
		super.setVisible(true);
	}
}
