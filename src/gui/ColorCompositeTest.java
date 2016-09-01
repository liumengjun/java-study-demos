package gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorCompositeTest extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame frame = new ColorCompositeTest();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(200, 300);
	}

	public ColorCompositeTest() {
		setTitle("ColorCompositeTest");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// the main panel, it shows the graphics
		canvas = new CompositePanel();
		add(canvas, BorderLayout.CENTER);

		// create buttons
		JButton buttonA = new JButton("ColorA");
		buttonA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// in the following procedure, we will choose colorB, because a
				// modal-less dialog, here the colorAEditing boolean flag assure
				// only one dialog is open
				if (colorAEditing) {
					return;
				}
				colorAEditing = true;
				// create color chooser
				final Color oldColorA = canvas.getColorA();
				final JColorChooser colorChooser = new JColorChooser(oldColorA);
				colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						Color a = colorChooser.getColor();
						canvas.setColorA(a);
					}
				});
				// create dialog containing upper color chooser
				JDialog jColorDialog = JColorChooser.createDialog(ColorCompositeTest.this, "ColorA", false,
						colorChooser, new ActionListener() {
							public void actionPerformed(ActionEvent e2) {
								Color a = colorChooser.getColor();
								canvas.setColorA(a);
								colorAEditing = false;
							}
						}, new ActionListener() {
							public void actionPerformed(ActionEvent e3) {
								canvas.setColorA(oldColorA);
								colorAEditing = false;
							}
						});
				jColorDialog.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						colorAEditing = false;
					}
				});
				jColorDialog.setVisible(true);
				// Color a = JColorChooser.showDialog(ColorCompositeTest.this,
				// "ColorA", canvas.getColorA());
				// canvas.setColorA(a);
			}
		});

		JButton buttonB = new JButton("ColorB");
		buttonB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// in the following procedure, we will choose colorB, because a
				// modal-less dialog, here the colorBEditing boolean flag assure
				// only one dialog is open
				if (colorBEditing) {
					return;
				}
				colorBEditing = true;
				// create color chooser
				final Color oldColorB = canvas.getColorB();
				final JColorChooser colorChooser = new JColorChooser(oldColorB);
				colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						Color a = colorChooser.getColor();
						canvas.setColorB(a);
					}
				});
				// create dialog containing upper color chooser
				JDialog jColorDialog = JColorChooser.createDialog(ColorCompositeTest.this, "ColorB", false,
						colorChooser, new ActionListener() {
							public void actionPerformed(ActionEvent e2) {
								Color a = colorChooser.getColor();
								canvas.setColorB(a);
								colorBEditing = false;
							}
						}, new ActionListener() {
							public void actionPerformed(ActionEvent e3) {
								canvas.setColorB(oldColorB);
								colorBEditing = false;
							}
						});
				jColorDialog.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						colorBEditing = false;
					}
				});
				jColorDialog.setVisible(true);
			}
		});

		JButton buttonS = new JButton("Switch Color");
		buttonS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.switchColor();
			}
		});

		alphaSliderA = new JSlider(0, 100, 75);
		alphaSliderA.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				canvas.setAlphaA(alphaSliderA.getValue());
			}
		});

		JPanel panel = new JPanel();
		panel.add(buttonA);
		panel.add(buttonB);
		panel.add(new JLabel("Alpha A"));
		panel.add(alphaSliderA);
		panel.add(buttonS);
		add(panel, BorderLayout.NORTH);

		canvas.setAlphaA(alphaSliderA.getValue());
	}

	private boolean colorAEditing = false;
	private boolean colorBEditing = false;
	private CompositePanel canvas;
	private JSlider alphaSliderA;
	private static final int DEFAULT_WIDTH = 700;
	private static final int DEFAULT_HEIGHT = 300;

	/**
	 * the main panel, it shows the graphics
	 */
	private class CompositePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public CompositePanel() {
			shape1 = new Ellipse2D.Double(30, 30, 100, 100);
			shape2 = new Rectangle2D.Double(80, 80, 100, 100);
			rule = new int[] { AlphaComposite.SRC_OVER, AlphaComposite.SRC_ATOP, AlphaComposite.DST_OVER,
					AlphaComposite.DST_ATOP };
			ruleName = new String[] { "SRC_OVER", "SRC_ATOP", "DST_OVER", "DST_ATOP" };
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			for (int i = 0; i < rule.length; i++) {
				// paint the graphics
				AlphaComposite composite = AlphaComposite.getInstance(rule[i], alphaA);
				BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D gImage = image.createGraphics();
				gImage.setPaint(colorA);
				gImage.fill(shape1);
				gImage.setComposite(composite);
				gImage.setPaint(colorB);
				gImage.fill(shape2);
				g2.drawImage(image, null, 160 * i, 0);
				// save it into file
				if (changeFlag) {
					File tempDir = new File("temp" + File.separator);
					tempDir.mkdirs();
					String name = "temp" + File.separator + "Color" + Integer.toHexString(colorA.getRGB()) + "_Color"
							+ Integer.toHexString(colorB.getRGB()) + "(Composite" + ruleName[i] + "_Alpha"
							+ (int) (alphaA * 100) + "%).png";
					File pngFile = new File(name);
					if (pngFile.exists()) {
						continue;
					}
					try {
						ImageWriter writer = ImageIO.getImageWritersBySuffix("png").next();
						ImageOutputStream imageOut = ImageIO.createImageOutputStream(pngFile);
						writer.setOutput(imageOut);
						// writer.write(image);
						writer.write(image.getSubimage(0, 0, 200, 200));
						writer.dispose();
						imageOut.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			changeFlag = false;
		}

		public void setAlphaA(int a) {
			alphaA = (float) a / 100.0F;
			repaint();
		}

		public Color getColorA() {
			return colorA;
		}

		public void setColorA(Color a) {
			changeFlag = !colorA.equals(a);
			colorA = a;
			repaint();
		}

		public Color getColorB() {
			return colorB;
		}

		public void setColorB(Color b) {
			changeFlag = !colorB.equals(b);
			colorB = b;
			repaint();
		}

		public void switchColor() {
			changeFlag = !colorA.equals(colorB);
			Color temp = colorA;
			colorA = colorB;
			colorB = temp;
			repaint();
		}

		private Shape shape1;
		private Shape shape2;
		private int[] rule;
		private String[] ruleName;
		private Color colorA = Color.red;
		private Color colorB = Color.green;
		private float alphaA;
		private boolean changeFlag = true;
	}

}
