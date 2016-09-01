package gui.swing.combo;

import java.awt.Color;

import javax.swing.JFrame;

public class TestComboFocus2 extends javax.swing.JFrame {
	private javax.swing.JComboBox combo;

	public TestComboFocus2(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setLayout(new java.awt.FlowLayout());

		this.getContentPane()
				.add(combo = new javax.swing.JComboBox(new String[] {
						"one person in this city", "two persons in this city" }));
		combo.getEditor().getEditorComponent().setBackground(Color.YELLOW);

		this.setBounds(100, 100, 400, 300);

		this.setVisible(true);

		this.disableFocusBackground(combo);
	}

	private void disableFocusBackground(javax.swing.JComboBox combo) {
		if (combo == null) {
			return;
		}

		java.awt.Component comp = combo.getEditor().getEditorComponent();

		if (comp instanceof javax.swing.JTextField) {
			javax.swing.JTextField field = (javax.swing.JTextField) comp;

			field.setEditable(false);

//			field.setSelectionColor(field.getBackground());
			field.setSelectionColor(java.awt.Color.WHITE);

			combo.setEditable(true);
		}
	}

	public static void main(String[] args) {
		new TestComboFocus2("Test ComboFocus2");
	}
}

class NoBackgroundComboBoxUI extends javax.swing.plaf.basic.BasicComboBoxUI {
	protected java.awt.Rectangle rectangleForCurrentValue() {
		java.awt.Rectangle rect = super.rectangleForCurrentValue();

		// The root cause is that this method return an invalid Rectangle, so it
		// limited the bounds of Graphics

		return rect;
	}
}
