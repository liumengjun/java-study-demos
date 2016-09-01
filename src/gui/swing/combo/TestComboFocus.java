package gui.swing.combo;

import javax.swing.JFrame;

public class TestComboFocus extends javax.swing.JFrame implements
		javax.swing.event.PopupMenuListener {
	private javax.swing.JComboBox combo;

	public TestComboFocus(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		combo = new javax.swing.JComboBox(new String[] {
				"one person in this city", "two persons in this city" });

		combo.addPopupMenuListener(this);

		combo.setBackground(java.awt.Color.YELLOW);
//		hideComboFocus(false);

		this.getContentPane().setLayout(new java.awt.FlowLayout());

		this.getContentPane().add(combo);

		this.setBounds(100, 100, 400, 300);

		this.setVisible(true);
	}

	public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
	}

	public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
		System.err.println("popupMenuWillBecomeVisible");

		hideComboFocus(true);
	}

	public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
		System.err.println("popupMenuWillBecomeInvisible");

		hideComboFocus(false);
	}

	private final void hideComboFocus(boolean hidden) {
		if (combo == null) {
			return;
		}

		javax.swing.ListCellRenderer comp = combo.getRenderer();

		if (comp instanceof javax.swing.JComponent) {
			((javax.swing.JComponent) comp).setOpaque(hidden);
		}
	}

	public static void main(String[] args) {
		new TestComboFocus("Test ComboFocus");
	}
}
