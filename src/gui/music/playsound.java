package gui.music;

import sun.audio.*;
import java.io.*;

public class playsound {
	public static void main(String[] args) {
		try {
			FileInputStream fileau = new FileInputStream("audio/test.wav");
			AudioStream as = new AudioStream(fileau);
			AudioPlayer.player.start(as);
		} catch (Exception e) {
			System.out.println("失败咯！");
		}
	}
}
