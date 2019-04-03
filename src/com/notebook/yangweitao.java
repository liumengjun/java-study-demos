package com.notebook;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

@SuppressWarnings("serial")
public class yangweitao extends Frame implements ActionListener, MouseListener {
	FileDialog fileDlg;
	String str, fileName;
	byte byteBuf[] = new byte[10000];
	Toolkit toolKit = Toolkit.getDefaultToolkit();
	Clipboard clipBoard = toolKit.getSystemClipboard();
	TextArea ta = new TextArea();
	PopupMenu pm = new PopupMenu();
	MenuBar mb = new MenuBar();
	Menu m1 = new Menu("�ļ�");
	Menu m2 = new Menu("�༭");
	Menu m3 = new Menu("����");
	MenuItem cut2 = new MenuItem("����");
	MenuItem copy2 = new MenuItem("����");
	MenuItem paste2 = new MenuItem("ճ��");

	MenuItem cut = new MenuItem("����");
	MenuItem copy = new MenuItem("����");
	MenuItem paste = new MenuItem("ճ��");
	MenuItem computer = new MenuItem("������");

	MenuItem open = new MenuItem("��");
	MenuItem close = new MenuItem("�ر�");
	MenuItem save = new MenuItem("����");
	MenuItem exit = new MenuItem("�Ƴ�");

	yangweitao() {
		setTitle("�����ļ��༭��");
		setSize(400, 280);
		add("Center", ta);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		m1.add(open);
		m1.add(close);
		m1.add(save);
		m1.addSeparator();

		m1.add(exit);
		m2.add(cut);
		m2.add(paste);
		m2.add(copy);
		m3.add(computer);

		open.addActionListener(this);
		close.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);

		cut.addActionListener(this);
		copy.addActionListener(this);
		paste.addActionListener(this);

		cut2.addActionListener(this);
		copy2.addActionListener(this);
		paste2.addActionListener(this);

		computer.addActionListener(this);
		mb.add(m1);
		mb.add(m2);
		mb.add(m3);
		pm.add(cut2);
		pm.add(paste2);
		pm.add(copy2);
		ta.add(pm);
		this.setMenuBar(mb);
		ta.addMouseListener(this);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exit)
			System.exit(0);
		else if (e.getSource() == close)
			ta.setText(null);
		else if (e.getSource() == open) {
			fileDlg = new FileDialog(this, "���ļ�");
			fileDlg.setVisible(true);
			fileName = fileDlg.getFile();
			try {
				FileInputStream in = new FileInputStream(fileName);
				in.read(byteBuf);
				in.close();
				str = new String(byteBuf);
				ta.setText(str);
				setTitle("adsad" + fileName);
			} catch (IOException ioe) {
			}
		} else if (e.getSource() == save) {
			fileDlg = new FileDialog(this, "�����ļ�", FileDialog.SAVE);
			fileDlg.setVisible(true);
			fileName = fileDlg.getFile();
			str = ta.getText();
			byteBuf = str.getBytes();
			try {
				FileOutputStream out = new FileOutputStream(fileName);
				out.write(byteBuf);
				out.close();
			} catch (IOException ioe) {
			}
		} else if (e.getActionCommand() == "����") {
			String text = ta.getSelectedText();
			StringSelection selection = new StringSelection(text);
			clipBoard.setContents(selection, null);
			ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
		} else if (e.getActionCommand() == "����") {
			String text = ta.getSelectedText();
			StringSelection selection = new StringSelection(text);
			clipBoard.setContents(selection, null);
		} else if (e.getActionCommand() == "ճ��") {
			Transferable contents = clipBoard.getContents(this);
			if (contents == null)
				return;
			String text;
			text = "";
			try {
				text = (String) contents
						.getTransferData(DataFlavor.stringFlavor);
			} catch (Exception exception) {
			}
			ta.replaceRange(text, ta.getSelectionStart(), ta.getSelectionEnd());
		} else if (e.getActionCommand() == "������") {
			Runtime r = Runtime.getRuntime();
			try {
				r.exec("calc");
			} catch (IOException e1) {
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			pm.show(this, e.getX(), e.getX());
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public static void main(String args[]) {
		new yangweitao();
	}
}
