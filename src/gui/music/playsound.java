package gui.music;

//import sun.audio.*;

/**
 * 如果使用 java8以上版本(>jdk1.8), 那么在编译时需要加下面参数
 * --add-exports java.desktop/com.sun.media.sound=ALL-UNNAMED
 */
import com.sun.media.sound.WaveFileReader;

import javax.sound.sampled.*;
import java.io.*;

public class playsound {
    public static void main(String[] args) {
        try {
            FileInputStream fileau = new FileInputStream("audio/test.wav");
//			AudioStream as = new AudioStream(fileau);
//			AudioPlayer.player.start(as);
            AudioInputStream auInput = new WaveFileReader().getAudioInputStream(fileau);
//			AudioInputStream auInput = AudioSystem.getAudioInputStream(fileau);
            Clip clip = AudioSystem.getClip();
            clip.open(auInput);
            System.out.println(clip.getFrameLength());
            System.out.println(clip.getMicrosecondLength());
            clip.start();
            clip.setFramePosition(0);
//            clip.setMicrosecondPosition(0);
            while (clip.isRunning()) {
                Thread.sleep(1000);
            }
            clip.close();
        } catch (Exception e) {
            System.out.println("失败咯！");
            e.printStackTrace();
        }
    }
}
