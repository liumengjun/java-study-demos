package gui.music;

import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.swing.JFrame;

public class MediaPlayer extends JFrame implements ActionListener, ItemListener, ControllerListener {
	String title;

	Player player;

	boolean first = true, loop = false;

	Component vc, cc;

	String currentDirectory = null;

	// 构造函数，其中包括了设置响应窗口事件的监听器。
	MediaPlayer(String title) {

		super(title);
		/* 关闭按钮的实现。。 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}

			public void windowClosed(WindowEvent e) {
				if (player != null)
					player.close();
				System.exit(0);
			}

		});
		// 调用程序菜单栏的方法成员完成菜单的布置
		setupMenu();
		setSize(400, 400);
		setVisible(true);
	}

	// 本方法用以设置程序菜单栏
	public void setupMenu() {
		// 设置一个菜单
		Menu f = new Menu("文件");
		// 往设置的菜单添加菜单项
		MenuItem mi = new MenuItem("打开");
		f.add(mi);
		mi.addActionListener(this);
		f.addSeparator();
		CheckboxMenuItem cbmi = new CheckboxMenuItem("循环", false);
		cbmi.addActionListener(this);
		f.add(cbmi);
		f.addSeparator();
		MenuItem ee = new MenuItem("退出");
		ee.addActionListener(this);
		f.add(ee);

		f.addSeparator();

		Menu l = new Menu("播放列表");
		Menu c = new Menu("播放控制");
		MenuItem move = new MenuItem("播放");
		move.addActionListener(this);
		c.add(move);
		c.addSeparator();
		MenuItem pause = new MenuItem("暂停");
		pause.addActionListener(this);
		c.add(pause);
		c.addSeparator();
		MenuItem stop = new MenuItem("停止");
		stop.addActionListener(this);
		c.add(stop);
		c.addSeparator();
		// 设置一个菜单栏
		MenuBar mb = new MenuBar();
		mb.add(f);
		mb.add(c);
		mb.add(l);
		// 将构造完成的菜单栏交给当前程序的窗口;
		setMenuBar(mb);
	}

	// 动作时间响应成员；捕捉发送到本对象的各种事件;
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cufile, selectfile, currentDirectory;
		if (e.getActionCommand().equals("退出")) {
			// 调用dispose以便执行windowClosed
			dispose();
			return;
		}
		// 此事表明拥护选择了“播放”命令;
		// 如果当前有一个文件可以播放则执行播放命令;

		if (e.getActionCommand().equals("播放")) {
			if (player != null) {
				player.start();
			}
			return;
		}
		// 如果当前正在播放某一文件，则执行暂停;
		if (e.getActionCommand().equals("暂停")) {
			if (player != null) {
				player.stop();
			}
			return;
		}
		// 停止命令的响应;
		if (e.getActionCommand().equals("停止")) {
			if (player != null) {
				player.stop();
				player.setMediaTime(new Time(0));
			}
			return;
		}
		// 用户选择要播放的媒体文件
		if (e.getActionCommand().equals("打开")) {
			FileDialog fd = new FileDialog(this, "打开媒体文件", FileDialog.LOAD);
			// fd.setDirectory(currentDirectory);

			fd.setVisible(true);
			// 如果用户放弃选择文件，则返回
			if (fd.getFile() == null) {
				return;
			}
			// 保存了所选文件的名称及其路径名称已被稍后使用
			// 同时设置当前文件夹路径
			selectfile = fd.getFile();
			currentDirectory = fd.getDirectory();
			cufile = currentDirectory + selectfile;
			// 将用户选择的文件作为一个菜单项加入播放列表,该菜单项名为该文件名;
			// 被点击后给出的命令串是该文件的全路径名
			MenuItem mi = new MenuItem(selectfile);
			mi.setActionCommand(cufile);
			MenuBar mb = getMenuBar();
			Menu m = mb.getMenu(2);
			mi.addActionListener(this);
			m.add(mi);
		} else {
			// 程序逻辑运行到次表示用户选择了一个“播放列表”中的媒体文件
			// 此时可以通过如下动作获得该文件的全路径名
			cufile = e.getActionCommand();
			selectfile = cufile;
		}

		// 如果存在一个播放器，则先将其关闭，稍后再重新创建
		// 创建播放器时需要捕捉一些异常
		if (player != null) {
			player.close();
		}
		try {
			player = Manager.createPlayer(new MediaLocator("file:" + cufile));
		} catch (Exception e2) {
			System.out.println(e2);
			return;
		}/*
		 * catch(NoPlayerException e2){ System.out.println("不能找到播放器"); return ; }
		 */
		if (player == null) {
			System.out.println("无法创建播放器");
			return;
		}
		first = false;
		setTitle(selectfile);
		// 设置处理播放控制器实际的对象；
		/**/
		player.addControllerListener(this);
		player.prefetch();
	}

	// 菜单状态改变事件的响应函数；
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MediaPlayer("播放器");
	}

	// 调用绘图函数进行界面的绘制
	// public void update() {

	// }

	// 绘图函数成员
	// public void paint(Graphics g) {

	// }

	public void controllerUpdate(ControllerEvent e) {
		// TODO Auto-generated method stub
		Container tainer = getContentPane();
		// 调用player.close()时ControllerClosedEvent事件出现
		// 如果存在视觉部件，则该部件应该拆除（为了一致起见，我们对控制面版部件也执行同样的操作，下一次需要时再构造)
		if (e instanceof ControllerClosedEvent) {
			if (vc != null) {
				remove(vc);
				vc = null;
			}
			if (cc != null) {
				remove(cc);
				cc = null;
			}
		}

		// 播放结束时，将播放指针置于文件之首，如果设定了循环播放，则再次启动播放器；
		if (e instanceof EndOfMediaEvent) {
			player.setMediaTime(new Time(0));
			if (loop) {
				player.start();
			}
			return;
		}

		// PrefetchCompletEvent事件发生后调用start,正式启动播放
		if (e instanceof PrefetchCompleteEvent) {
			player.start();
			return;
		}

		// 本事件表示由于播放的资源已经确定；此时要将媒体的图形conmopnent
		// 如果有显示出来，同时将播放器player的控制显示到窗口里；
		if (e instanceof RealizeCompleteEvent) {
			// 如果媒体中有图像，将对应图像component载入窗体；
			vc = player.getVisualComponent();
			if (vc != null)
				tainer.add(vc, BorderLayout.CENTER);
			// 将对应控制器component载入窗体;
			cc = player.getControlPanelComponent();
			cc.setBackground(Color.blue);
			if (cc != null)
				tainer.add(cc, BorderLayout.SOUTH);
			// 有一些特殊媒体在播放时提供另外的控制手段，将控制器一并加入窗口；
			/*
			 * gc=player.getGainControl(); gcc=gc.getControlComponent(); if(gcc!=null)
			 * tainer.add(gcc,BorderLayout.NORTH);
			 */
			// 根据媒体文件中是否有图像，设定相应的窗口大小
			if (vc != null) {
				pack();
				return;
			} else {
				setSize(300, 75);
				setVisible(true);
				return;
			}
		}

	}
}