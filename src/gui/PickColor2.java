package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class PickColor2 extends JFrame {
	
	private static boolean isActive = true;
	private static Object lock = new Object();
	
	public static void main(String[] args) {
		PickColor2 pc = new PickColor2();
		// Color color = pc.pickColor();
		// System.out.println("color = "+color);
	}

	public PickColor2() {
		super("Pick Color");
		setSize(200, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel p = new JPanel();
		getContentPane().add(p);
		// this mouse listener only is limited in the java desktop region
//		p.addMouseMotionListener(new PickColorMouesMotionListener(p));
		
		this.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowLostFocus");
				isActive = false;
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowGainedFocus");
				isActive = true;
				synchronized (lock) {
					try{
						lock.notifyAll();
					}catch (Exception ce) {
						// TODO: handle exception
					}
				}
			}
		});
		
		setVisible(true);
		// this thread is really effected!
		new PickColorThread(p).start();
	}

	/**
	 * Mouse Motion Listener,when mouse are moving, then set corresping screens
	 * color to the JPanels background Color.
	 */
	class PickColorMouesMotionListener extends MouseMotionAdapter {
		private JPanel p = null;

		PickColorMouesMotionListener(JPanel p) {
			this.p = p;
		}

		public void mouseMoved(MouseEvent e) {
			Color c = pickColor();
			this.p.setBackground(c);
			// System.out.println (c);
		}
	}

	class PickColorThread extends Thread {
		private JPanel p = null;

		PickColorThread(JPanel p) {
			this.p = p;
		}

		public void run() {
			while (true) {
				try {
					Thread.currentThread().sleep(10);
					
					synchronized (lock) {
						while (!isActive ){
							try{
								lock.wait();
							}catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					
					if (isActive) {
						Color c = pickColor();
	
						this.p.setBackground(c);
	
						// try change the foreground when background s r <= 50 or g
						// <= 50 or b <= 50
						Graphics g = p.getGraphics();
						if (c.getRed() <= 50 || c.getGreen() <= 50 || c.getBlue() <= 50) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.BLACK);
						}
						g.drawString(c.toString(), 0, 100);
						g = null;
					}
					// System.out.println (c);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}

	/** Get Screen Color */
	public Color pickColor() {
		Color pixel = new Color(0, 0, 0);
		Robot robot = null;
		Point mousepoint;
		int R, G, B;
		// MouseInfo mouseinfo = new MouseInfo();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.exit(1);
		}
		mousepoint = MouseInfo.getPointerInfo().getLocation();
		System.out.println("("+mousepoint.x+","+mousepoint.y+")");
		pixel = robot.getPixelColor(mousepoint.x, mousepoint.y);
		System.out.println("color = " + pixel);
		R = pixel.getRed();
		G = pixel.getGreen();
		return pixel;
	}

}
