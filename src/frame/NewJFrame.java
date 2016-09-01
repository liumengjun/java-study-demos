package frame;
import java.awt.BorderLayout;
import javax.swing.JInternalFrame;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

public class NewJFrame extends javax.swing.JFrame {
	private JInternalFrame jInternalFrame1;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		// 风格设定
		// try {
		// String
		// styleName="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";//
		// Windows风格
		// UIManager.setLookAndFeel(styleName);
		// styleName可以传入以下参数
		// javax.swing.plaf.metal.MetalLookAndFeel; Metal外观
		// com.sun.java.swing.plaf.motif.MotifLookAndFeel; Motif外观
		// com.sun.java.swing.plaf.windows.WindowsLookAndFeel; windows外观
		// 更新
		// SwingUtilities.updateComponentTreeUI(getContentPane());
		// SwingUtilities.updateComponentTreeUI(this);
		// } catch (Exception e) {}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NewJFrame inst = new NewJFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public NewJFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jInternalFrame1 = new JInternalFrame("Internal Frame",true,true,true,true);
				getContentPane().add(jInternalFrame1, BorderLayout.CENTER);
				jInternalFrame1.setVisible(true);
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
