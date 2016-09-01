package gui.music;

import javax.swing.*;
import javax.sound.midi.*;
import java.awt.GridLayout;
import java.io.File;

public class PlayMidi extends JFrame {
	PlayMidi(String song) {
		super("Play MIDI Files");
		setSize(180, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MidiPanel midi = new MidiPanel(song);
		JPanel pane = new JPanel();
		pane.add(midi);
		setContentPane(pane);
		show();
	}

	public static void main(String[] arguments) {
		if (arguments.length != 1) {
			PlayMidi pm = new PlayMidi("audio/town.mid");
			System.out.println("Usage: java PlayMidi filename");
		} else {
			PlayMidi pm = new PlayMidi(arguments[0]);
		}
	}
}

class MidiPanel extends JPanel implements Runnable {
	Thread runner;
	JProgressBar progress = new JProgressBar();
	Sequence currentSound;// 音序
	Sequencer player;// 默认音序器
	String songFile;// 歌曲

	MidiPanel(String song) {
		super();
		songFile = song;
		JLabel label = new JLabel("Playing file...");
		setLayout(new GridLayout(2, 1));
		add(label);
		add(progress);
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}

	public void run() {

		try {
			System.out.println("11111");
			System.out.println(songFile);
			File file = new File(songFile);
			System.out.println("22222");
			currentSound = MidiSystem.getSequence(file);// 获取音序文件
			System.out.println("33333");
			player = MidiSystem.getSequencer();// 获取音序器
			System.out.println("4444");
			player.open();
			player.setSequence(currentSound);// 社定音序器播放指定音乐文件
			progress.setMinimum(0);
			System.out.println("!!!!!!!!!!1！");
			progress.setMaximum((int) player.getMicrosecondLength());// 设置最大位歌曲时间
			player.start();
			while (player.isRunning()) {
				progress.setValue((int) player.getMicrosecondPosition());// 设置播放文件显示当前播放进度
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("失败咯！11");
				}
			}
			progress.setValue((int) player.getMicrosecondPosition());// 同上
			player.close();
		} catch (Exception ex) {
			System.out.println("失败咯！22");
			System.out.println(ex.toString());
		}
	}

}
