package com.notebook;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
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

    // 保存文件的监听
    class savel implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser jf = new JFileChooser();
            jf.showSaveDialog(NotePad.this);

        }
    }

    // 打印的监听 ?
    class printl implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // PrintWriter p = new PrintWriter(NotePad.this);
        }
    }

    // 退出记事本的监听
    class exitl implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);// 退出
        }
    }

    // 拷贝的监听
    class copyl implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            jta.copy();
        }
    }

    // 粘贴的监听
    class pastel implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            jta.paste();
        }
    }

    // 剪切的监听
    class cutl implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            jta.cut();
        }
    }

    // 查找的监听

    // 添加日期的监听
    class datel implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Date d = new Date();
            jta.append(d.toString());
        }
    }

    // 构造函数
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

        mFile.addSeparator(); // 添加分割线

        JMenuItem mPrint = new JMenuItem("Print");
        mPrint.addActionListener(new printl());
        mFile.add(mPrint);

        mFile.addSeparator(); // 添加分割线

        JMenuItem mExit = new JMenuItem("Exit");
        mExit.addActionListener(new exitl());
        mFile.add(mExit);
        mFile.setMnemonic(KeyEvent.VK_F);

        // 编辑菜单的子菜单的处理
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

        mEdit.addSeparator(); // 添加分割线

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

    //主函数，程序入口点
    public static void main(String s[]) {
        new NotePad();
    }

}
