package client.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.Client;
import general.container.Message;

public class CaptureNoteGUI extends JFrame {
	private Container con = new Container();
	
	private JPanel panel = new JPanel();
	private JButton color = new JButton("색 변경");
	private JButton save = new JButton("저장");
	private JButton send = new JButton("보내기");

	private File capture = new File("files", "capture.png");
	private BufferedImage image = ImageIO.read(capture);

	private MyCan can = new MyCan();
	private int width;
	private int height;
	private Color penColor;
	
	private Point fpoint;
	private Point lpoint;
	
	private String myid;
	private String youid;
	
	private final int SAVE = 0;
	private final int SEND = 1;
	
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
				g.setColor(penColor);
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
				g.setColor(penColor);
				g.drawLine(fpoint.x, fpoint.y, lpoint.x, lpoint.y);
				fpoint = lpoint;
			}
		}
	};
	
	private void display(){
		can.setVisible(true);
		this.add(can);

		this.add(panel, BorderLayout.NORTH);
		panel.add(color);
		panel.add(save);
		panel.add(send);
	}
	
	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		color.addActionListener(e -> {
			 Color selCr = JColorChooser.showDialog(null, "색선정", Color.blue);
			 penColor = selCr;
		});
		
		save.addActionListener(e -> {
			save();
		});
		
		send.addActionListener(e -> {
			save();
			
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
	
	private void save() {
		Point current = this.getLocation();
		Rectangle area = new Rectangle(current.x + 3, current.y + 57, width, height+3);
		try {
			Robot robot = new Robot();
			BufferedImage bufferedImage = robot.createScreenCapture(area);
			ImageIO.write(bufferedImage, "png", capture);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public CaptureNoteGUI(int width, int height, String myid, String youid) throws IOException {
		super.setTitle("캡쳐 이미지");
		super.setSize(width+5, height+60);
		Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		super.setLocation(area.width - (width + 5), 0);
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
