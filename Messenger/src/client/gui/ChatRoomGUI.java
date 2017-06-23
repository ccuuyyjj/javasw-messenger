package client.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import client.Client;
import general.container.Message;

public class ChatRoomGUI extends JFrame implements DropTargetListener {
	private File chatDB = null;
	private TreeSet<Message> msgset = null;
	private JEditorPane msgview = new JEditorPane();
	private StringBuilder msgviewhtml = null;
	private JPanel panel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(msgview);
	private JTextArea textfield = new JTextArea();
	private JButton upload = new JButton("파일 올리기");
	private JButton send = new JButton("전송");

	private String myid = Client.identity; // 내 아이디
	private String youid = null; // 상대방 아이디
	private String younick = null;

	public File target = null;
	// 드로그 앤 드롭 변수
	DropTarget dt;
	JTextArea ta;
	
	public TreeSet<Message> getMsgset() {
		return msgset;
	}

	private void display() {

		Container con = getContentPane();
		con.setLayout(null);

		panel.setBounds(10, 10, 420, 500);
		upload.setBounds(10, 520, 420, 30);
		textfield.setBounds(10, 560, 340, 60);
		send.setBounds(362, 560, 68, 60);

		con.add(panel);
		panel.setLayout(new GridLayout());
		panel.setBackground(Color.WHITE);

		panel.add(scrollPane);
		scrollPane.setBorder(new LineBorder(Color.BLACK));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		msgview.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
		msgview.setEditable(false);
		StyleSheet style = new StyleSheet();
		try {
			style.importStyleSheet(new URL("http://fonts.googleapis.com/earlyaccess/nanumgothic.css"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((HTMLEditorKit) msgview.getEditorKit()).setStyleSheet(style);

		con.add(upload);
		upload.setBorder(new LineBorder(new Color(0, 0, 0)));

		con.add(textfield);
		textfield.setBorder(new LineBorder(new Color(0, 0, 0)));
		textfield.setLineWrap(true);

		send.setBorder(new LineBorder(new Color(0, 0, 0)));
		con.add(send);

		ta = this.textfield;
		dt = new DropTarget(ta, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);

		loadMessage();
	}

	private boolean scrolling = false;

	private void event() {
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
			if (!e.getValueIsAdjusting() && !scrolling) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
			scrolling = e.getValueIsAdjusting();
		});
		msgview.addHyperlinkListener(e -> {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				if (e.getDescription().startsWith("file:")) {
					long msgtime = Long.parseLong(e.getDescription().substring(5).trim());
					for (Message msg : msgset) {
						if (msg.getTime_created() == msgtime) {
							String filename = ((File) msg.getMsg()).getName();
							FileDialog fd = new FileDialog(this, "파일 저장", FileDialog.SAVE);
							fd.setFile(filename);
							fd.setVisible(true);

							if (fd.getFile() != null) {
								
								target = new File(fd.getDirectory(), fd.getFile());
								String filepath = target.getAbsolutePath().replace("\\", "/");
								
								try {
									Client.conn.sendObject(msg);
									while(target != null)
										Thread.sleep(100);
								} catch (IOException | InterruptedException e1) {
									e1.printStackTrace();
								}
								System.out.println("\"file:///" + filepath + "\"");
								if(fd.getFile().toLowerCase().endsWith(".png") || fd.getFile().toLowerCase().endsWith(".jpg") || fd.getFile().toLowerCase().endsWith(".gif"))
									msg.setMsg(filename + "<img src=\"file:///" + filepath + "\" width=400px> <a href=\"folder:"
											+ fd.getDirectory()
											+ "\">폴더 열기</a>");
								else
									msg.setMsg(filename + " <a href=\"folder:"
										+ fd.getDirectory()
										+ "\">폴더 열기</a>");
							}
							break;
						}
					}
					loadMessage();
				} else if (e.getDescription().startsWith("folder:")) {
					try {
						Desktop.getDesktop()
								.browse(new URI(URLDecoder.decode("file:///"
										+ e.getDescription().substring(7).replace("\\", "/"),
										"UTF-8")));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		textfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (textfield.getText().trim().equals("캡쳐")) {
						try {
							Robot robot = new Robot();
							Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()); //전체화면 해상도 구하기
						    BufferedImage img = robot.createScreenCapture(area); //전체화면 스크린샷
						    CaptureGUI t = new CaptureGUI((Image)img, myid, youid);
						} catch (AWTException e1) {
							e1.printStackTrace();
						}
					}
					
					Message msg = new Message(myid, youid, textfield.getText().trim());
					textfield.setText("");

					try {
						Client.conn.sendObject(msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					msgset.add(msg);
					loadMessage();
				}
			}
		});
		upload.addActionListener(e -> {
			FileDialog fd = new FileDialog(this, "업로드 파일", FileDialog.LOAD);
			fd.setVisible(true);
			if (fd.getFile() != null) {
				File upfile = new File(fd.getDirectory(), fd.getFile());
				Message msg = new Message(myid, youid, upfile);

				try {
					Client.conn.sendObject(msg);
					Client.conn.sendFile(upfile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				msgset.add(msg);
				loadMessage();
			}
		});
		// 임시폴더에 파일 저장 , 파일 저장 위치 지정
	}

	private void menu() {

	}

	@Override
	public void dispose() {
		Client.chatList.remove(youid);
		saveMessage();
		super.dispose();
	}

	public ChatRoomGUI(String youid) {
		this.youid = youid;
		if (!new File("db/" + myid).exists())
			new File("db/" + myid).mkdirs();
		chatDB = new File("db/" + myid, "chat_from_" + youid + ".db");
		if (chatDB.exists()) {
			try {
				ObjectInputStream oin = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(chatDB)));
				msgset = (TreeSet<Message>) oin.readObject();
				oin.close();
			} catch (IOException | ClassNotFoundException e) {
			}
		} else
			msgset = new TreeSet<>();
		younick = Client.friends.getFriendsList().get(youid);
		super.setTitle(this.younick + "님과의 채팅");
		super.setSize(450, 700);
		super.setLocationByPlatform(true);
		super.setResizable(false);
		display();
		event();
		menu();
		super.setVisible(false);
	}

	public void messageHandler(Message msg) {
		System.out.println(Client.identity + "가 받음 : " + msg.getMsg().getClass().getSimpleName());
		msgset.add(msg);
		loadMessage();
	}

	private void saveMessage() {
		try {
			if (chatDB.exists()) {
				chatDB.delete();
				chatDB.createNewFile();
			}
			ObjectOutputStream out = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(chatDB)));
			out.writeObject(msgset);
			out.close();
		} catch (IOException e) {
		}
	}

	public void loadMessage() {
		msgviewhtml = new StringBuilder();
		msgviewhtml.append("<div style=\"font-family: 'Nanum Gothic'; font-size: 10px;\">");
		Message prev = null;
		for (Message msg : msgset) {
			if (prev != null)
				msgviewhtml.append("</br>\n");

			if (msg.getSender().equals(myid)) {
				msgviewhtml.append("<div style=\"text-align:right\">");
			} else if (msg.getSender().equals(youid)) {
				msgviewhtml.append("<div style=\"text-align:left\">");
			}

			if (((prev != null && !msg.getSender().equals(prev.getSender())) || prev == null)
					&& msg.getSender().equals(myid))
				msgviewhtml.append("<div><font color=#000000>" + myid + "</font></div>");
			else if (((prev != null && !msg.getSender().equals(prev.getSender())) || prev == null)
					&& msg.getSender().equals(youid))
				msgviewhtml.append("<div><font color=#000000>" + younick + "</font></div>");

			switch (msg.getMsg().getClass().getSimpleName()) {
			case "String":
				msgviewhtml.append("<div><font color=#101010>: " + (String) msg.getMsg() + "</font></div>");
				break;
			case "File":
				File received = (File) msg.getMsg();
				if (msg.getReceiver().equals(myid))
					msgviewhtml.append("<div><a href=\"file:" + msg.getTime_created()
							+ "\"><font color=#101010>파일: " + received.getName() + "</font></a></div>");
				else

					msgviewhtml.append(
							"<div><font color=#101010>파일: " + received.getName() + "</font></div>");
				break;
			}

			msgviewhtml.append("</div>");
			prev = msg;
		}
		msgviewhtml.append("</div>");
		msgview.setText(msgviewhtml.toString());
		msgview.repaint();
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// System.out.println("dragEnter");

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// System.out.println("dragExit");

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// System.out.println("dragOver");
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		// System.out.println("dragDrop");
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
			dtde.acceptDrop(dtde.getDropAction());
			Transferable tr = dtde.getTransferable();
			try {
				// 파일명 얻어오기
				@SuppressWarnings("unchecked")
				List<File> list = (List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);

				// 파일명 출력
				for (int i = 0; i < list.size(); i++) {
					File Df = list.get(i);
					Message msg = new Message(myid, youid, Df);
					try {
						Client.conn.sendObject(msg);
						Client.conn.sendFile(Df);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					msgset.add(msg);
					loadMessage();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// System.out.println("dragActionChanged");
	}
}