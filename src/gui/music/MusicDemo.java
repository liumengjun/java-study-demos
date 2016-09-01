package gui.music;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.*;

public class MusicDemo extends JFrame implements ActionListener {
	Player player = null;
	String tilte = "音乐播放器";

	public MusicDemo(String title) {
		super(title); // 设置显示标题（必须）
		setResizable(false);// 设置是否可以拖放窗口大小
		File mufile = new File("audio/xznhxqc.mp3");
		try {
			if (player == null) {
				if (mufile.exists()) {
					MediaLocator locator = new MediaLocator("file:" + mufile.getAbsolutePath());
					player = Manager.createRealizedPlayer(locator);
					player.prefetch();
				}
			}
			// player.addControllerListener(this);
			player.start();// 开始播放

			add(player.getControlPanelComponent(), "South");
			double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			setLocation((int) lx / 2 - 200, (int) ly / 2 - 150);// 设置显示位置（必须）
			// 设置frame的大小（必须）
			setSize(400, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭按钮退出程序（必须）
			setVisible(true);// (必须)
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public String getTilte() {
		return tilte;
	}

	public void setTilte(String tilte) {
		this.tilte = tilte;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		MusicDemo d = new MusicDemo("播放音乐");
	}
}
