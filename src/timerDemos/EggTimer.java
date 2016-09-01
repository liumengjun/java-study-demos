package timerDemos;
import java.util.Timer;
import java.util.TimerTask;

public class EggTimer {
    private final Timer timer = new Timer();
    private final int seconds;

    public EggTimer(int seconds) {
        this.seconds = seconds;
    }

    public void start() {
        timer.schedule(new TimerTask() {
            public void run() {
                playSound();
                timer.cancel();
            }
            private void playSound() {
                System.out.println("Your egg is ready!");
                // Start a new thread to play a sound...
            }
        }, seconds * 1000,1000);
    }

    public static void main(String[] args) {
        EggTimer eggTimer = new EggTimer(2);//—” ±2√Î
        eggTimer.start();
    }

}
