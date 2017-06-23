package client.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import client.Client;
import general.container.Message;

class CaptureNoteGUI extends JFrame {
	private MenuBar mb = new MenuBar();
	private Menu menu = new Menu("메뉴");
	private MenuItem save = new MenuItem("저장");
	private MenuItem send = new MenuItem("보내기");

	private File capture = new File("files", "capture.png");
	private BufferedImage image = ImageIO.read(capture);

	private MyCan can = new MyCan();
	private int width;
	private int height;
	
	private Point fpoint;
	private Point lpoint;
	
	private String myid;
	private String youid;
	
	class MyCan extends Canvas implements MouseMotionListener, MouseListener {
		public MyCan() {
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		
		public void paint(Graphics g) {
			
		    g.drawImage(image, 0, 0, this);
		}

		public void mouseDragged(MouseEvent e) {
			lpoint = e.getPoint();
			if (fpoint != null && lpoint != null) {
				Graphics g = getGraphics();
				g.drawLine(fpoint.x, fpoint.y, lpoint.x,lpoint.y);
				fpoint = lpoint;
			}
		}

		public void mouseMoved(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			fpoint = e.getPoint();
		}

		public void mouseReleased(MouseEvent e) {
			lpoint = e.getPoint();
			if (fpoint != null && lpoint != null) {
				Graphics g = getGraphics();
				g.drawLine(fpoint.x, fpoint.y, lpoint.x,lpoint.y);
				fpoint = lpoint;
			}
		}
	};
	
	private void display() throws IOException {
		setMenuBar(mb);
		mb.add(menu);
		menu.add(save);
		menu.add(send);
		
		can.setVisible(true);
		this.add(can);
	}
	
	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		save.addActionListener(e ->{
			try { Thread.sleep(1000); } catch (Exception e2) {}
			Point current = this.getLocation();
			Rectangle area = new Rectangle(current.x + 3, current.y + 47, width, height);
			try {
				Robot robot = new Robot();
				BufferedImage bufferedImage = robot.createScreenCapture(area);
				ImageIO.write(bufferedImage, "png", capture);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		send.addActionListener(e -> {
			save.getActionListeners();
			
			Message msg = new Message(myid, youid, capture);
			
			try {
				Client.conn.sendObject(msg);
				Client.conn.sendFile(capture);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			Client.chatList.get(youid).getMsgset().add(msg);
			Client.chatList.get(youid).loadMessage();
			
			dispose();
		});
	}
	
	private void menu() {}
	
	public CaptureNoteGUI(int width, int height, String myid, String youid) throws IOException {
		super.setTitle("캡쳐 이미지");
		super.setSize(width+5, height+50);
		super.setLocation(0, 0);
		super.setResizable(false);
		this.width = width;
		this.height = height;
		this.myid = myid;
		this.youid = youid;
		display();
		event();
		menu();
		super.setVisible(true);
		can.repaint();
	}
}
