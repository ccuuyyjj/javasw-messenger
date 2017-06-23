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
//		직접 창에 배치하는 것이 아니라 이미 설치되어 있는 투명한 유리를 가져와서 그 곳에 배치
		con = super.getContentPane();
		con.setLayout(null);
		
		img = new ImageIcon(image);
		lb = new JLabel(img);
		lb.setOpaque(true);
		Dimension r = Toolkit.getDefaultToolkit().getScreenSize();
		lb.setBounds(0, 0, (int)r.getWidth(), (int)r.getHeight());
		con.add(lb);
	}
	
	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		// 현재 창만 종료
		
		MouseListener mListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				release = MouseInfo.getPointerInfo().getLocation();
				System.out.println("뗄때" + release);
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
				press = MouseInfo.getPointerInfo().getLocation();
				System.out.println("눌렀을때" + press);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		lb.addMouseListener(mListener);
	}
	
	private void menu() {}
	
	private void capture() throws AWTException, IOException {
		Robot robot = new Robot();
		int width = release.x - press.x;
		int height = release.y - press.y;
		if (width > 0 && height > 0) {
			area = new Rectangle(press.x, press.y, width, height);
		    Graphics g = getGraphics();
		    g.drawRect(press.x, press.y, width, height);
		    BufferedImage bufferedImage = robot.createScreenCapture(area);
		    
		    File capture = new File("files", "capture.png");
		    if (!capture.exists()) capture.getParentFile().mkdirs();
		    try {
				ImageIO.write(bufferedImage, "png", capture);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    CaptureNoteGUI t = new CaptureNoteGUI(width, height, myid, youid);
		    dispose();
//		    LoadImageApp.main(null);
		} else JOptionPane.showMessageDialog(lb, "범위가 잘못 설정되었습니다");
		
//		Runtime.getRuntime().exec("c:\\windows\\system32\\mspaint.exe");
	}
	
	public CaptureGUI(Image img, String myid, String youid) {
		super.setTitle("캡쳐 스크린샷");
		super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//		운영체제(window)의 창 배치 형식에 따라 배치
		super.setLocation(0, 0);		// 각각의 운영체제에 따라
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
