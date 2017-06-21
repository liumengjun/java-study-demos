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
	Menu m1 = new Menu("文件");
	Menu m2 = new Menu("编辑");
	Menu m3 = new Menu("工具");
	MenuItem cut2 = new MenuItem("剪切");
	MenuItem copy2 = new MenuItem("复制");
	MenuItem paste2 = new MenuItem("粘贴");

	MenuItem cut = new MenuItem("剪切");
	MenuItem copy = new MenuItem("复制");
	MenuItem paste = new MenuItem("粘贴");
	MenuItem computer = new MenuItem("计算器");

	MenuItem open = new MenuItem("打开");
	MenuItem close = new MenuItem("关闭");
	MenuItem save = new MenuItem("保存");
	MenuItem exit = new MenuItem("推出");

	yangweitao() {
		setTitle("简易文件编辑器");
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
			fileDlg = new FileDialog(this, "打开文件");
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
			fileDlg = new FileDialog(this, "保存文件", FileDialog.SAVE);
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
		} else if (e.getActionCommand() == "剪切") {
			String text = ta.getSelectedText();
			StringSelection selection = new StringSelection(text);
			clipBoard.setContents(selection, null);
			ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
		} else if (e.getActionCommand() == "复制") {
			String text = ta.getSelectedText();
			StringSelection selection = new StringSelection(text);
			clipBoard.setContents(selection, null);
		} else if (e.getActionCommand() == "粘贴") {
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
		} else if (e.getActionCommand() == "计算器") {
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
