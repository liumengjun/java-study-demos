package gui;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

public class PickColor {
	public static void main(String[] args) {
		PickColor pc = new PickColor();
		Color color = pc.pickColor();
		System.out.println("color = " + color);

		JFrame f = new JFrame();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(f);

//		GraphicsEnvironment ge3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice[] gs = ge3.getScreenDevices();
//		for (GraphicsDevice curGs : gs) {
//			GraphicsConfiguration[] gc = curGs.getConfigurations();
//			for (GraphicsConfiguration curGc : gc) {
//				Rectangle bounds = curGc.getBounds();
//				System.out.println(bounds.getX() + "," + bounds.getY() + " " + bounds.getWidth() + "x"
//						+ bounds.getHeight());
//			}
//		}

//		GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice[] gs2 = ge2.getScreenDevices();
//		for (int j = 0; j < gs2.length; j++) {
//			GraphicsDevice gd2 = gs2[j];
//			GraphicsConfiguration[] gc = gd2.getConfigurations();
//			for (int i = 0; i < gc.length; i++) {
//				JFrame f2 = new JFrame(gs2[j].getDefaultConfiguration());
//				Canvas c = new Canvas(gc[i]);
//				Rectangle gcBounds = gc[i].getBounds();
//				int xoffs = gcBounds.x;
//				int yoffs = gcBounds.y;
//				f2.getContentPane().add(c);
//				f2.setLocation((i * 50) + xoffs, (i * 60) + yoffs);
//				f2.setVisible(true);
//			}
//		}
	}

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
		System.out.println("(" + mousepoint.x + "," + mousepoint.y + ")");
		System.out.println(MouseInfo.getPointerInfo().getDevice());
		pixel = robot.getPixelColor(mousepoint.x, mousepoint.y);
		R = pixel.getRed();
		G = pixel.getGreen();
		B = pixel.getBlue();
		return pixel;
	}

}
