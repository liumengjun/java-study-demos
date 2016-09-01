package frame;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

public class ijhi extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JButton jButton = null;

	/**
	 * This is the default constructor
	 */
	public ijhi() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getJScrollPane());
			jSplitPane.setContinuousLayout(true);
			jSplitPane.setOneTouchExpandable(true);//此方法比较重要
			jSplitPane.setRightComponent(getJTable());
			jSplitPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
				public void mouseDragged(java.awt.event.MouseEvent e) {
					System.out.println("mouseDragged()"); // TODO Auto-generated Event stub mouseDragged()
				}
			});
			jSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
				public void propertyChange(java.beans.PropertyChangeEvent e) {
					if ((e.getPropertyName().equals("enabled"))) {
						System.out.println("propertyChange(enabled)"); // TODO Auto-generated property Event stub "enabled" 
					}
				}
			});
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJButton());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return jButton;
	}

}
