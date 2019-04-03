import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*; //Date needed
import java.io.PrintWriter;

public class NotePad extends JFrame {
	JTextArea jta;

	class newl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jta.setText("");
		}
	}

	class openl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser jf = new JFileChooser();
			jf.showOpenDialog(NotePad.this);

		}

	}

	// �����ļ��ļ���
	class savel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser jf = new JFileChooser();
			jf.showSaveDialog(NotePad.this);

		}
	}

	// ��ӡ�ļ��� ?
	class printl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// PrintWriter p = new PrintWriter(NotePad.this);
		}
	}

	// �˳����±��ļ���
	class exitl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);// �˳�
		}
	}

	// �����ļ���
	class copyl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jta.copy();
		}
	}

	// ճ���ļ���
	class pastel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jta.paste();
		}
	}

	// ���еļ���
	class cutl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jta.cut();
		}
	}

	// ���ҵļ���

	// ������ڵļ���
	class datel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Date d = new Date();
			jta.append(d.toString());
		}
	}

	// ���캯��
	public NotePad() {
		jta = new JTextArea("", 24, 40);
		JScrollPane jsp = new JScrollPane(jta);
		JMenuBar jmb = new JMenuBar();
		JMenu mFile = new JMenu("File");
		JMenu mEdit = new JMenu("Edit");

		JMenuItem mNew = new JMenuItem("New", KeyEvent.VK_N);
		mNew.addActionListener(new newl());
		mFile.add(mNew);

		JMenuItem mOpen = new JMenuItem("Open", KeyEvent.VK_O);
		mOpen.addActionListener(new openl());
		mFile.add(mOpen);

		JMenuItem mSave = new JMenuItem("Save");
		mSave.addActionListener(new savel());
		mFile.add(mSave);

		mFile.addSeparator(); // ��ӷָ���

		JMenuItem mPrint = new JMenuItem("Print");
		mPrint.addActionListener(new printl());
		mFile.add(mPrint);

		mFile.addSeparator(); // ��ӷָ���

		JMenuItem mExit = new JMenuItem("Exit");
		mExit.addActionListener(new exitl());
		mFile.add(mExit);
		mFile.setMnemonic(KeyEvent.VK_F);

		// �༭�˵����Ӳ˵��Ĵ���
		JMenuItem jmi;
		jmi = new JMenuItem("Copy");
		jmi.addActionListener(new copyl());
		mEdit.add(jmi);

		jmi = new JMenuItem("Cut");
		jmi.addActionListener(new cutl());
		mEdit.add(jmi);

		jmi = new JMenuItem("Paste");
		jmi.addActionListener(new pastel());
		mEdit.add(jmi);

		mEdit.addSeparator(); // ��ӷָ���

		jmi = new JMenuItem("Find");

		mEdit.add(jmi);

		jmi = new JMenuItem("FindNext");
		mEdit.add(jmi);
		mEdit.addSeparator();
		jmi = new JMenuItem("Select All");
		mEdit.add(jmi);
		jmi = new JMenuItem("Date/Time");
		jmi.addActionListener(new datel());
		mEdit.add(jmi);

		jmb.add(mFile);
		jmb.add(mEdit);

		this.setJMenuBar(jmb);

		this.getContentPane().add(jsp);
		this.setSize(200, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	//��������������ڵ�
	public static void main(String s[]) {
		new NotePad();
	}

}
